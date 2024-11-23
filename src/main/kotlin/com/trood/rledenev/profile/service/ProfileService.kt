package com.trood.rledenev.profile.service

import com.trood.rledenev.profile.dto.ProfileDto
import org.springframework.web.multipart.MultipartFile

interface ProfileService {
    fun getProfile(): ProfileDto
    fun updateProfile(profileDto: ProfileDto): ProfileDto
    fun uploadAvatarImage(image: MultipartFile): String
}
