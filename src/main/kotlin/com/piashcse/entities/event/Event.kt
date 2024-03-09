package com.piashcse.entities.event

import com.piashcse.entities.base.BaseIntEntity
import com.piashcse.entities.base.BaseIntEntityClass
import com.piashcse.entities.base.BaseIntIdTable
import com.piashcse.entities.base.currentUtc
import com.piashcse.entities.user.UserTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.datetime

object EventTable : BaseIntIdTable("event") {
    val userId = reference("user_id", UserTable.id)
    val title = text("title")
    val content = text("content")
    val bannerUrl = text("banner_url").nullable()
    val startAt = datetime("start_at").clientDefault { currentUtc() }
    val endAt = datetime("end_at").nullable()
    override val primaryKey = PrimaryKey(id)
}

class EventEntity(id: EntityID<String>) : BaseIntEntity(id, EventTable) {
    companion object : BaseIntEntityClass<EventEntity>(EventTable)

    var userId by EventTable.userId
    var title by EventTable.title
    var content by EventTable.content
    var bannerUrl by EventTable.bannerUrl
    var startAt by EventTable.startAt
    var endAt by EventTable.endAt
    fun response() = Event(
        id.value,
        title,
        content,
        bannerUrl,
    )
}

data class Event(
    val id: String,
    val title: String,
    val content: String,
    val bannerUrl: String?
)