package com.minerdev.exermate.model

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

data class ChatLog(
    var roomId: Int = 0,
    var fromId: Int = 0,
    var createdAt: String = "",
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