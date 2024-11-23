package com.trood.rledenev.profile.validation

import com.trood.rledenev.profile.validation.annotation.ValidStringList
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class StringListValidator : ConstraintValidator<ValidStringList, List<String>> {
    private var maxElementSize: Int = 0
    private lateinit var regexp: Regex

    override fun initialize(constraintAnnotation: ValidStringList) {
        maxElementSize = constraintAnnotation.maxElementSize
        regexp = Regex(constraintAnnotation.regexp)
    }

    override fun isValid(strings: List<String>?, context: ConstraintValidatorContext): Boolean {
        if (strings == null) return true
        for (element in strings) {
            if (element.length > maxElementSize || !regexp.matches(element)) {
                return false
            }
        }
        return true
    }
}
