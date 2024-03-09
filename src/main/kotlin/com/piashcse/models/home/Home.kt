package com.piashcse.models.home

import com.piashcse.entities.event.Event
import com.piashcse.entities.product.Product

data class Home(
    val events: List<Event>?,
    val products: List<Product>?
)