package com.trood.rledenev.profile.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorData(
    val httpStatus: HttpStatus,
    val timestamp: LocalDateTime,
    val message: String?,
    val details: String
)
