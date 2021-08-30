package com.minerdev.exermate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Post(
    @SerialName("exerPostID") var id: String = "",
    @SerialName("hostemail") var email: String = "",
    @SerialName("uploadTime") var createdAt: Long = 0,
    var updatedAt: Long = 0,
    @SerialName("exerName") var title: String = "",
    @SerialName("exerPlace") var place: String = "",
    @SerialName("exerTime") var exerciseTime: String = "",
    @SerialName("maxNum") var maxMemberNum: Int = 0,
    @SerialName("contents") var text: String = "",
    var chatRoomName: String = "",
    var state: Int = 0
) {
    fun toJson() = buildJsonObject {
        put("exerPostID", id)
        put("hostemail", email)
        put("uploadTime", createdAt)
        put("exerName", title)
        put("exerPlace", place)
        put("exerTime", exerciseTime)
        put("maxNum", maxMemberNum)
        put("contents", text)
        put("chatRoomName", chatRoomName)
    }
}