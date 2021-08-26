package com.minerdev.exermate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class ChatRoom(
    @SerialName("chatRoomID") var id: String = "",
    var createdAt: Long = 0,
    @SerialName("chatRoomName") var name: String = "",
    var users: List<User> = listOf()
) {
    fun toJson() = buildJsonObject {
        put("chatRoomID", id)
        put("chatRoomName", name)
    }
}