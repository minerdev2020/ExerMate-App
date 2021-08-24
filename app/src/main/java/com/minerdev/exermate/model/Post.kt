package com.minerdev.exermate.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Post(
    var id: Int = 0,
    var createdAt: String = "",
    var updatedAt: String = "",
    var title: String = "",
    var place: String = "",
    var exerciseTime: String = "",
    var maxMemberNum: Int = 0,
    var text: String = "",
    var state: Int = 0
) {
    fun toJson() = buildJsonObject {
        put("exerPostID", id)
        put("exerName", title)
        put("exerPlace", place)
        put("exerTime", exerciseTime)
        put("maxNum", maxMemberNum)
        put("contents", text)
    }
}