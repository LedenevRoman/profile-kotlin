package com.trood.rledenev.profile.service.impl

import com.trood.rledenev.profile.dto.ProfileDto
import com.trood.rledenev.profile.entity.Profile
import com.trood.rledenev.profile.exception.ProfileNotFoundException
import com.trood.rledenev.profile.mapper.toDto
import com.trood.rledenev.profile.mapper.update
import com.trood.rledenev.profile.repository.ProfileRepository
import com.trood.rledenev.profile.service.ProfileService
import com.trood.rledenev.profile.service.util.FileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ProfileServiceImpl(
    private val profileRepository: ProfileRepository,
    private val fileService: FileService
) : ProfileService {

    @Transactional(readOnly = true)
    override fun getProfile(): ProfileDto {
        val profile = findProfile()
        return profile.toDto()
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override fun updateProfile(profileDto: ProfileDto): ProfileDto {
        val profile = findProfile()
        profile.update(profileDto)
        return profile.toDto()
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override fun uploadAvatarImage(image: MultipartFile): String {
        val profile = findProfile()
        if (profile.avatarImageName != null) {
            fileService.deleteImage(profile.avatarImageName!!)
        }
        fileService.saveImage(image)
        val imageName = image.originalFilename
        profile.avatarImageName = imageName
        return "http://localhost:8080/images/$imageName"
    }

    private fun findProfile(): Profile {
        return profileRepository.findAll().firstOrNull()
            ?: throw ProfileNotFoundException("Profile not found")
    }
}