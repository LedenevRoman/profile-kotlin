package com.trood.rledenev.profile.validation

import com.trood.rledenev.profile.validation.annotation.ValidImage
import jakarta.activation.MimetypesFileTypeMap
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile

class MultipartFileValidator : ConstraintValidator<ValidImage, MultipartFile> {

    override fun isValid(file: MultipartFile, context: ConstraintValidatorContext): Boolean {
        val fileTypeMap = MimetypesFileTypeMap()
        val mimeType: String = fileTypeMap.getContentType(file.originalFilename)
        val size = file.size
        if (!isSupportedContentType(mimeType)) return false
        if (size > 5 * 1024 * 1024) return false
        return true
    }

    private fun isSupportedContentType(mimeType: String?): Boolean {
        return mimeType != null && (
                        mimeType == "image/png" ||
                        mimeType == "image/jpg" ||
                        mimeType == "image/jpeg"
                        )
    }

}
