package com.trood.rledenev.profile.validation.annotation

import com.trood.rledenev.profile.validation.MultipartFileValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [MultipartFileValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidImage(
    val message: String = "Image must be png/jpg/jpeg type, and max size 5 MB",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)