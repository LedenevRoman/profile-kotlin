package com.trood.rledenev.profile.entity

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL

@Embeddable
data class Link(
    @field:NotBlank(message = "Name of link can not be empty")
    @field:Size(max = 50, message = "Name of link can contain at most 50 characters")
    var name: String,

    @field:NotBlank(message = "URL link can not be empty")
    @field:URL(message = "Invalid URL")
    @field:Size(max = 200, message = "URL link can contain at most 200 characters")
    var url: String
)
