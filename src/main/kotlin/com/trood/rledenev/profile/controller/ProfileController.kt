package com.trood.rledenev.profile.controller

import com.trood.rledenev.profile.dto.ProfileDto
import com.trood.rledenev.profile.service.ProfileService
import com.trood.rledenev.profile.service.util.FileService
import com.trood.rledenev.profile.validation.annotation.ValidImage
import jakarta.activation.MimetypesFileTypeMap
import jakarta.validation.Valid
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping
class ProfileController(
    private val profileService: ProfileService,
    private val fileService: FileService
) {

    @GetMapping("/profile")
    fun getProfile(): ProfileDto {
        return profileService.getProfile()
    }

    @PutMapping("/profile")
    fun updateProfile(@RequestBody @Valid profileDto: ProfileDto): ProfileDto {
        return profileService.updateProfile(profileDto)
    }

    @PostMapping("/upload-avatar")
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadAvatarImage(@RequestParam("image") @ValidImage image: MultipartFile): String {
        return profileService.uploadAvatarImage(image)
    }

    @GetMapping("/images/{imageName}")
    fun getFile(@PathVariable imageName: String): ResponseEntity<Resource> {
        val file: Resource = fileService.getImage(imageName)
        val fileTypeMap = MimetypesFileTypeMap()
        val mimeType: String = fileTypeMap.getContentType(file.filename)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mimeType))
            .body(file)
    }

}