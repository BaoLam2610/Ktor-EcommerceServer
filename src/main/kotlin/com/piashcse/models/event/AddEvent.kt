package com.piashcse.models.event

import com.papsign.ktor.openapigen.content.type.multipart.FormDataRequest
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import com.papsign.ktor.openapigen.content.type.multipart.PartEncoding
import com.piashcse.models.user.body.MultipartImage
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

@FormDataRequest
data class AddEvent(
    val title: String,
    val content: String,
    @PartEncoding("image/*") override val file: NamedFileInputStream,
) : MultipartImage(file) {
    override fun validation() {
        validate(this) {
            validate(AddEvent::title).isNotNull().isNotEmpty()
            validate(AddEvent::content).isNotNull().isNotEmpty()
        }
    }
}