package com.piashcse.models.product.request

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.piashcse.models.PagingData

data class ProductWithFilter(
    @QueryParam("pageSize") override val pageSize: Int,
    @QueryParam("currentPage") override val currentPage: Int,
    @QueryParam("maxPrice") val maxPrice: Double?,
    @QueryParam("minPrice") val minPrice: Double?,
    @QueryParam("categoryId") val categoryId: String?,
    @QueryParam("subCategoryId") val subCategoryId: String?,
    @QueryParam("brandId") val brandId: String?,
) : PagingData(pageSize, currentPage)
