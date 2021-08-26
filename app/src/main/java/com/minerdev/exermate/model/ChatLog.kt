package com.minerdev.exermate.model

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

data class ChatLog(
    var roomId: String = "",
    var fromId: String = "",
    var createdAt: Long = 0,
    var text: String = "",
    var type: Byte = 0
) {
    fun toJson() = buildJsonObject {
        put("roomId", roomId)
        put("fromId", fromId)
        put("createdAt", createdAt)
        put("text", text)
        put("type", type)
    }
}