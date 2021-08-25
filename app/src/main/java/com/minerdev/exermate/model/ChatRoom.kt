package com.minerdev.exermate.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class ChatRoom(
    var id: Int = 0,
    var createdAt: Long = 0,
    var users: List<User> = listOf()
) {
    fun toJson() = buildJsonObject {
        put("id", id)
    }
}