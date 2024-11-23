package com.trood.rledenev.profile.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.trood.rledenev.profile.dto.ErrorData
import com.trood.rledenev.profile.dto.ProfileDto
import com.trood.rledenev.profile.entity.Link
import com.trood.rledenev.profile.service.util.FileService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/database/schema-cleanup.sql")
@Sql("/database/create_tables.sql")
@Sql("/database/add_test_data.sql")
class ProfileControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @MockBean
    private val fileService: FileService? = null

    @Test
    fun getProfilePositive() {
        // when
        val profileDtoResponseJson: String = mockMvc!!.perform(MockMvcRequestBuilders.get("/profile"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .response
            .contentAsString

        // then
        val actualProfileDto: ProfileDto? = objectMapper?.readValue(profileDtoResponseJson, ProfileDto::class.java)
        Assertions.assertEquals(getInitial(), actualProfileDto)
    }

    @Test
    fun updateProfilePositive() {
        //given
        val updatesProfileDto = getFull()
        val updatesJson: String = objectMapper?.writeValueAsString(updatesProfileDto) ?: ""

        //when
        val profileBeforeUpdateJson: String = mockMvc!!.perform(MockMvcRequestBuilders.get("/profile"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .response
            .contentAsString

        mockMvc.perform(
            MockMvcRequestBuilders.put("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatesJson)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())

        val profileAfterUpdateJson: String = mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .response
            .contentAsString


        //then
        val beforeUpdateProfileDto: ProfileDto? =
            objectMapper?.readValue(profileBeforeUpdateJson, ProfileDto::class.java)
        val afterUpdateProfileDto: ProfileDto? = objectMapper?.readValue(profileAfterUpdateJson, ProfileDto::class.java)
        Assertions.assertEquals(getInitial(), beforeUpdateProfileDto)
        Assertions.assertEquals(getFull(), afterUpdateProfileDto)
    }

    @Test
    fun uploadAvatarImagePositive() {
        //given
        val imageName = "test.jpg"
        val mockImage = MockMultipartFile("image", imageName, "image/jpeg", null as ByteArray?)
        val expectedImageUrl = "http://localhost:8080/images/$imageName"

        //when
        Mockito.doNothing().`when`<FileService>(fileService).saveImage(mockImage)

        val responseImageUrl = mockMvc!!.perform(
            MockMvcRequestBuilders.multipart("/upload-avatar").file(mockImage)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .response
            .contentAsString

        val profileDtoResponseJson: String = mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .response
            .contentAsString

        //then
        val actualProfileDto: ProfileDto? = objectMapper?.readValue(profileDtoResponseJson, ProfileDto::class.java)
        Mockito.verify<FileService>(fileService, Mockito.times(1)).saveImage(mockImage)
        Assertions.assertEquals(expectedImageUrl, responseImageUrl)
        Assertions.assertEquals(expectedImageUrl, actualProfileDto?.avatarUrl)
    }

    @Test
    fun getFilePositive() {
        //given
        val imageName = "test.png"

        //when
        Mockito.`when`(fileService!!.getImage(imageName)).thenReturn(object : ByteArrayResource(ByteArray(0)) {
            override fun getFilename(): String {
                return imageName
            }
        })

        val contentType: String = mockMvc!!.perform(MockMvcRequestBuilders.get("/images/$imageName"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .response
            .getHeaderValue(HttpHeaders.CONTENT_TYPE)
            .toString()

        //then
        Mockito.verify(fileService, Mockito.times(1)).getImage(imageName)
        Assertions.assertEquals("image/png", contentType)
    }

    @Test
    fun updateProfileValidation() {
        //given
        val updatesProfileDto = getFullWithInvalidData()
        val updatesJson: String = objectMapper?.writeValueAsString(updatesProfileDto) ?: ""

        //when

        val errorDataJson: String = mockMvc!!.perform(
            MockMvcRequestBuilders.put("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatesJson)
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andReturn()
            .response
            .contentAsString

        //then
        val errorData: ErrorData? =
            objectMapper?.readValue(errorDataJson, ErrorData::class.java)
        val exceptionMessages = errorData?.message?.split("; ") ?: emptyList()

        Assertions.assertTrue(exceptionMessages.contains("Name must contain only letters and spaces"))
        Assertions.assertTrue(exceptionMessages.contains("Surname must be between 2 and 50 characters long"))
        Assertions.assertTrue(exceptionMessages.contains("Invalid email"))
        Assertions.assertTrue(exceptionMessages.contains("Phone number must be between 10 and 15 numbers long"))
        Assertions.assertTrue(exceptionMessages.contains("Name of link can contain at most 50 characters"))
        Assertions.assertTrue(exceptionMessages.contains("Invalid URL"))
        Assertions.assertTrue(exceptionMessages.contains("Address must contain only letters, spaces, numbers, hyphens, dots and comas"))
        Assertions.assertTrue(exceptionMessages.contains("Job title must contain only letters, spaces and numbers"))
        Assertions.assertTrue(exceptionMessages.contains("Interests can contain at most 10 elements"))
        Assertions.assertTrue(exceptionMessages.contains("Each interest name can only contain letters, digits, spaces or some divide symbols and must be at most 30 characters long"))
        Assertions.assertTrue(exceptionMessages.contains("public flag can not be null"))
    }

    private fun getInitial(): ProfileDto {
        return ProfileDto(
            name = "Roman",
            surname = "Ledenev",
            email = "led.rom.93@gmail.com",
            links = emptyList(),
            interests = emptyList(),
            isPublic = false
        )
    }


    private fun getFull(): ProfileDto {
        return ProfileDto(
            name = "Roman",
            surname = "Ledenev",
            email = "led.rom.93@gmail.com",
            phone = "+123456789",
            links = listOf(
                Link(name = "linkedIn", url = "https://www.linkedin.com/in/rledenev"),
                Link(name = "github", url = "https://github.com/LedenevRoman")
            ),
            address = "Warsaw, Poland",
            jobTitle = "Java Software Engineer",
            interests = listOf("Java", "Spring Boot", "Microservices", "Kafka", "Redis"),
            isPublic = true
        )
    }

    private fun getFullWithInvalidData(): ProfileDto {
        return ProfileDto(
            name = "Roman!",
            surname = "Ledeneeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeev",
            email = "led.rom.@gmail.com",
            phone = "+1234",
            links = listOf(
                Link(name = "linkedIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIn", url = "https://www.linkedin.com/in/rledenev"),
                Link(name = "github", url = "LedenevRoman")
            ),
            address = "Warsaw, Poland !",
            jobTitle = "Java Software Engineer#",
            interests = listOf("Java", "Spring Boot", "Microservices@@@", "Kafkaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Redis",
                "Java", "Spring Boot", "Microservices", "Kafka", "Redis",
                "Java", "Spring Boot", "Microservices", "Kafka", "Redis"),
            isPublic = null
        )
    }
}
