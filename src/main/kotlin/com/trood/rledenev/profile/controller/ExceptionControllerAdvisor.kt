package com.trood.rledenev.profile.controller

import com.trood.rledenev.profile.dto.ErrorData
import com.trood.rledenev.profile.exception.ImageNotFoundException
import com.trood.rledenev.profile.exception.ProfileNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionControllerAdvisor {

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ErrorData> {
        exception.printStackTrace()
        val errorData = ErrorData(
            HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
            exception.message, exception.stackTrace.contentToString()
        )
        return ResponseEntity<ErrorData>(errorData, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(
        ProfileNotFoundException::class,
        ImageNotFoundException::class
    )
    fun handleNotFoundException(exception: RuntimeException): ResponseEntity<ErrorData> {
        val errorData = ErrorData(
            HttpStatus.NOT_FOUND, LocalDateTime.now(),
            exception.message, exception.stackTrace.contentToString()
        )
        return ResponseEntity(errorData, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidation(exception: HandlerMethodValidationException): ResponseEntity<ErrorData> {
        val validationMessages = getValidationMessage(exception)
        val errorData = ErrorData(
            HttpStatus.BAD_REQUEST,
            LocalDateTime.now(),
            validationMessages.ifEmpty { "Validation failed" },
            exception.stackTraceToString()
        )
        return ResponseEntity(errorData, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException): ResponseEntity<ErrorData> {
        val message = getValidationMessage(exception)
        val errorModel = ErrorData(
            HttpStatus.BAD_REQUEST, LocalDateTime.now(),
            message, exception.toString()
        )
        return ResponseEntity<ErrorData>(errorModel, HttpStatus.BAD_REQUEST)
    }

    private fun getValidationMessage(exception: HandlerMethodValidationException): String {
        return exception.allValidationResults
            .flatMap { it.resolvableErrors.map { violation -> violation.defaultMessage } }
            .joinToString("; ")
    }

    private fun getValidationMessage(exception: MethodArgumentNotValidException): String {
        return exception.bindingResult.allErrors.joinToString("; ") { it.defaultMessage.orEmpty() }
    }
}