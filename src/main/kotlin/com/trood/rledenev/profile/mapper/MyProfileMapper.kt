package com.trood.rledenev.profile.mapper

import com.trood.rledenev.profile.dto.ProfileDto
import com.trood.rledenev.profile.entity.Interest
import com.trood.rledenev.profile.entity.Profile

fun Profile.toDto(): ProfileDto {
    return ProfileDto(
        interests = this.interests?.map { it.name },
        links = this.links,
        name = this.name,
        surname = this.surname,
        jobTitle = this.jobTitle,
        phone = this.phone,
        email = this.email,
        address = this.address,
        avatarUrl = if (avatarImageName != null) "http://localhost:8080/images/$avatarImageName" else null,
        isPublic = this.isPublic
    )
}

fun Profile.update(profileDto: ProfileDto): Profile {
    if (profileDto.interests != null) {
        val newInterests = ArrayList<Interest>()
        profileDto.interests!!.forEach { e -> newInterests.add(Interest(e)) }
        this.interests = newInterests
    }
    if (profileDto.links != null) links = profileDto.links
    if (profileDto.name != null) name = profileDto.name
    if (profileDto.surname != null) surname = profileDto.surname
    if (profileDto.jobTitle != null) jobTitle = profileDto.jobTitle
    if (profileDto.phone != null) phone = profileDto.phone
    if (profileDto.email != null) email = profileDto.email
    if (profileDto.address != null) address = profileDto.address
    isPublic = profileDto.isPublic
    return this
}