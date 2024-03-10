package com.piashcse.controller

import com.piashcse.dbhelper.query
import com.piashcse.entities.event.EventEntity
import com.piashcse.entities.product.ProductEntity
import com.piashcse.entities.product.ProductTable
import com.piashcse.models.PagingData
import com.piashcse.models.home.Home
import org.jetbrains.exposed.sql.selectAll

class HomeController {
    suspend fun getHome(pagingData: PagingData) = query {
        val productQuery = ProductTable.selectAll()
        val totalProduct = productQuery.count().toInt()
        val products = productQuery.limit(pagingData.pageSize, pagingData.offset).map {
            ProductEntity.wrapRow(it).response()
        }
        val eventQuery = EventEntity.all()
        val events = eventQuery.map { it.response() }
        Home(
            totalSellingProducts = totalProduct,
            products = products,
            events = events,
        )
    }
}