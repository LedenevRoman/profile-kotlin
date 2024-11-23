package com.trood.rledenev.profile.dto

import com.trood.rledenev.profile.entity.Link
import com.trood.rledenev.profile.validation.annotation.ValidStringList
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length

data class ProfileDto(
    @field:NotBlank(message = "Name can not be empty")
    @field:Pattern(
        regexp = "^[\\p{L}\\s]+$",
        message = "Name must contain only letters and spaces"
    )
    @field:Length(
        min = 2,
        max = 50,
        message = "Name must be between 2 and 50 characters long"
    )
    var name: String? = null,

    @field:NotBlank(message = "Surname can not be empty")
    @field:Pattern(
        regexp = "^[\\p{L}\\s]+$",
        message = "Surname must contain only letters and spaces"
    )
    @field:Length(
        min = 2,
        max = 50,
        message = "Surname must be between 2 and 50 characters long"
    )
    var surname: String? = null,

    @field:Length(max = 100, message = "Job title max 100 characters long")
    @field:Pattern(
        regexp = "^[\\p{L}\\s\\d]+$",
        message = "Job title must contain only letters, spaces and numbers"
    )
    var jobTitle: String? = null,

    @field:NotBlank(message = "Phone can not be empty")
    @field:Pattern(
        regexp = "^\\+\\d+$",
        message = "The phone number must starts with '+', and contain only numbers"
    )
    @field:Length(
        min = 10,
        max = 15,
        message = "Phone number must be between 10 and 15 numbers long"
    )
    var phone: String? = null,

    @field:NotBlank(message = "Email can not be empty")
    @field:Email(message = "Invalid email")
    var email: String? = null,

    @field:Pattern(
        regexp = "^[\\p{L}\\s\\d\\-.,]+$",
        message = "Address must contain only letters, spaces, numbers, hyphens, dots and comas"
    )
    @field:Length(
        max = 200,
        message = "Address max 200 characters long"
    )
    var address: String? = null,
    var avatarUrl: String? = null,

    @field:Valid
    var links: List<@Valid Link>? = null,

    @field:Size(max = 10, message = "Interests can contain at most 10 elements")
    @field:ValidStringList(
        maxElementSize = 30,
        regexp = "^[\\p{L}\\s\\d\\-.,/\\\\]+$",
        message = "Each interest name can only contain letters, digits, spaces or some divide symbols and must be at most 30 characters long"
    )
    var interests: List<String>? = null,

    @field:NotNull(message = "public flag can not be null")
    var isPublic: Boolean? = null
)
