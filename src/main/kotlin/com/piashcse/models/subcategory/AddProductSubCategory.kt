package com.piashcse.models.subcategory

import com.papsign.ktor.openapigen.content.type.multipart.FormDataRequest
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import com.papsign.ktor.openapigen.content.type.multipart.PartEncoding
import com.piashcse.models.user.body.MultipartImage
import org.valiktor.functions.isNotNull
import org.valiktor.validate

@FormDataRequest
data class AddProductSubCategory(
    val categoryId: String,
    val subCategoryName: String,
    @PartEncoding("image/*") override val file: NamedFileInputStream?,
) : MultipartImage(file) {
    override fun validation() {
        validate(this) {
            validate(AddProductSubCategory::categoryId).isNotNull()
            validate(AddProductSubCategory::subCategoryName).isNotNull()
        }
    }
}