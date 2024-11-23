package com.trood.rledenev.profile.service.util

import com.trood.rledenev.profile.exception.ImageNotFoundException
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

private const val IMAGES_FOLDER_PATH = "./images"

@Service
class FileService {

    @PostConstruct
    fun init() {
        val imageDirectory = File(IMAGES_FOLDER_PATH)
        imageDirectory.mkdirs()
    }

    fun saveImage(image: MultipartFile) {
        val imageName = image.originalFilename
        val path: Path = Path.of("$IMAGES_FOLDER_PATH/$imageName")
        Files.write(path, image.bytes)
    }

    fun deleteImage(avatarImageName: String) {
        val path: Path = Path.of("$IMAGES_FOLDER_PATH/$avatarImageName")
        Files.delete(path)
    }

    fun getImage(imageName: String): Resource {
        val imagePath: Path = Path.of("$IMAGES_FOLDER_PATH/$imageName")
        val fileBytes: ByteArray
        try {
            fileBytes = Files.readAllBytes(imagePath)
        } catch (e: IOException) {
            throw ImageNotFoundException("Image with name $imageName not found")
        }

        return object : ByteArrayResource(fileBytes) {
            override fun getFilename(): String {
                return imagePath.fileName.toString()

            }
        }
    }
}