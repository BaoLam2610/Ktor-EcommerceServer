package com.piashcse.controller

import com.piashcse.dbhelper.query
import com.piashcse.entities.event.EventEntity
import com.piashcse.entities.event.EventTable
import com.piashcse.models.PagingData
import com.piashcse.models.event.AddEvent
import com.piashcse.utils.PageResult
import com.piashcse.utils.extension.getPageResult
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.selectAll

class EventController {

    suspend fun addEvent(userId: String, fileName: String?, addEvent: AddEvent) = query {
        EventEntity.new {
            this.userId = EntityID(userId, EventTable)
            title = addEvent.title
            content = addEvent.content
            bannerUrl = fileName
        }.response()
    }

    suspend fun getEvents(
        pagingData: PagingData,
        onReturnPage: (PageResult) -> Unit
    ) = query {
        val query = EventTable.selectAll()
        onReturnPage.invoke(
            getPageResult(query, pagingData.pageSize, pagingData.currentPage)
        )
        query.limit(pagingData.pageSize, pagingData.offset).map {
            EventEntity.wrapRow(it).response()
        }
    }
}