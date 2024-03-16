package com.piashcse.models.category

import com.papsign.ktor.openapigen.content.type.multipart.FormDataRequest
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import com.papsign.ktor.openapigen.content.type.multipart.PartEncoding
import com.piashcse.models.user.body.MultipartImage
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

@FormDataRequest
data class AddProductCategory(
    val categoryName: String,
    @PartEncoding("image/*") override val file: NamedFileInputStream?,
) : MultipartImage(file) {
    override fun validation() {
        validate(this) {
            validate(AddProductCategory::categoryName).isNotNull().isNotEmpty()
        }
    }
}