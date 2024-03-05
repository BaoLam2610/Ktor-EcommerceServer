package com.piashcse.models.user.body

import com.papsign.ktor.openapigen.content.type.multipart.FormDataRequest
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import com.papsign.ktor.openapigen.content.type.multipart.PartEncoding
import org.valiktor.functions.isNotNull
import org.valiktor.validate

@FormDataRequest
open class MultipartImage(@PartEncoding("image/*") open val file: NamedFileInputStream) {
    open fun validation() {
        validate(this) {
            validate(MultipartImage::file).isNotNull()
        }
    }
}
//
//@FormDataRequest
//data class MultipartImage(@PartEncoding("image/*") val file: NamedFileInputStream) {
//    fun validation() {
//        validate(this) {
//            validate(MultipartImage::file).isNotNull()
//        }
//    }
//}