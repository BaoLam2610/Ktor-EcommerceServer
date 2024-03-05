package com.piashcse.utils.extension

import com.piashcse.models.user.body.MultipartImage
import com.piashcse.utils.AppConstants
import com.piashcse.utils.PageResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import java.io.File
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any> T.nullProperties(callBack: (list: List<String>) -> Unit) {
    val allNullData = mutableListOf<String>()
    for (prop in T::class.memberProperties) {
        if (prop.get(this) == null) {
            allNullData.add(prop.name)
        }
    }
    if (allNullData.size > 0) {
        callBack.invoke(allNullData)
    }
}

fun currentTimeInUTC(): LocalDateTime {
    val currentMoment: Instant = Clock.System.now()
    return currentMoment.toLocalDateTime(TimeZone.UTC)

}

fun datetimeInSystemZone(): LocalDateTime {
    val currentMoment: Instant = Clock.System.now()
    return currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun ResultRow.toMap(): Map<String, Any?> {
    val mutableMap = mutableMapOf<String, Any?>()
    val dataList = this::class.memberProperties.find { it.name == "data" }?.apply {
        isAccessible = true
    }?.call(this) as Array<*>
    fieldIndex.entries.forEach { entry ->
        val column = entry.key as Column<*>
        mutableMap[column.name] = dataList[entry.value]
    }
    return mutableMap
}

fun String.fileExtension(): String {
    return this.substring(this.lastIndexOf("."))
}

fun String.orderStatusCode(): Int {
    return when (this) {
        OrderStatus.PENDING.name.lowercase() -> 0
        OrderStatus.CONFIRMED.name.lowercase() -> 1
        OrderStatus.PAID.name.lowercase() -> 2
        OrderStatus.DELIVERED.name.lowercase() -> 3
        OrderStatus.CANCELED.name.lowercase() -> 4
        OrderStatus.RECEIVED.name.lowercase() -> 5
        else -> 0
    }
}

fun getPageResult(query: Query, pageSize: Int, currentPage: Int): PageResult {
    val result = query.count().toInt()
    var totalPage = (result / pageSize)
    totalPage = if (result % pageSize == 0) totalPage else (totalPage + 1)
    return PageResult(
        totalRecord = result,
        totalPage = totalPage,
        currentPage = currentPage
    )
}

suspend fun fileNameInServer(
    multipartData: MultipartImage,
    location: String = AppConstants.Image.PRODUCT_IMAGE_LOCATION
): String {
    val uuid = UUID.randomUUID()
    val fileLocation = multipartData.file.name?.let {
        "${location}$uuid${it.fileExtension()}"
    }
    fileLocation?.let {
        File(it).writeBytes(withContext(Dispatchers.IO) {
            multipartData.file.readAllBytes()
        })
    }
    return uuid.toString().plus(fileLocation?.fileExtension())
}

enum class OrderStatus {
    PENDING, CONFIRMED, PAID, DELIVERED, CANCELED, RECEIVED
}