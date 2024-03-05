package com.piashcse.models.product.request

import com.papsign.ktor.openapigen.content.type.multipart.FormDataRequest
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import com.papsign.ktor.openapigen.content.type.multipart.PartEncoding
import com.piashcse.models.user.body.MultipartImage
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

@FormDataRequest
data class AddProduct(
    val categoryId: String,
    val subCategoryId: String?,
    val brandId: String?,
    val productName: String,
    val productCode: String?,
    val productQuantity: Int,
    val productDetail: String,
    val price: Double,
    val discountPrice: Double?,
    val status: Int?,
    val videoLink: String?,
    val mainSlider: String?,
    val hotDeal: String?,
    val bestRated: String?,
    val midSlider: String?,
    val hotNew: String?,
    val trend: String?,
    val buyOneGetOne: String?,
    val imageOne: String?,
    val imageTwo: String?,
    @PartEncoding("image/*") override val file: NamedFileInputStream,
) : MultipartImage(file) {
    override fun validation() {
        super.validation()
        validate(this) {
            validate(AddProduct::categoryId).isNotNull().isNotEmpty()
            validate(AddProduct::productName).isNotNull().isNotEmpty()
            validate(AddProduct::productDetail).isNotNull().isNotEmpty()
            validate(AddProduct::price).isNotNull().isGreaterThan(0.0)
            validate(AddProduct::productQuantity).isNotNull().isGreaterThan(0)
        }
    }
}
