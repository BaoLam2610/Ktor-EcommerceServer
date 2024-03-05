package com.piashcse.models

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isNotZero
import org.valiktor.validate

open class PagingData(
    @QueryParam("pageSize") open val pageSize: Int,
    @QueryParam("currentPage") open val currentPage: Int
) {
    fun validation() {
        validate(this) {
            validate(PagingData::pageSize).isNotNull().isNotZero()
            validate(PagingData::currentPage).isNotNull().isNotZero()
        }
    }

    val offset get() = ((currentPage - 1) * pageSize).toLong()
}
