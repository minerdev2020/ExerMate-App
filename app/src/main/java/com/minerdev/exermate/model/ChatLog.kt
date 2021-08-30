package com.minerdev.exermate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class ChatLog(
    @SerialName("chatRoomID") var roomId: String = "",
    @SerialName("useremail") var fromId: String = "",
    @SerialName("time") var createdAt: Long = 0,
    var text: String = "",
    var url: String = "",
    var type: Byte = 0
) {
    fun toJson() = buildJsonObject {
        put("roomId", roomId)
        put("fromId", fromId)
        put("time", createdAt)
        put("text", text)
        put("url", url)
        put("type", type)
    }
}