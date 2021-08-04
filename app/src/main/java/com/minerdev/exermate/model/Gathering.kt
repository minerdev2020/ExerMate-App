package com.minerdev.exermate.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Gathering(
    var id: Int = 0,
    var createdAt: String = "",
    var updatedAt: String = "",
    var title: String = "",
    var text: String = "",
    var state: Int = 0
) {
    fun toJson() = buildJsonObject {
        put("id", id)
        put("createdAt", createdAt)
        put("updatedAt", updatedAt)
        put("title", title)
        put("text", text)
        put("state", state)
    }
}