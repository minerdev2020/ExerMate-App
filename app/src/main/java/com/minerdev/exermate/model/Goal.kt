package com.minerdev.exermate.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class Goal(
    var id: Int = 0,
    var createdAt: String = "",
    var updatedAt: String = "",
    var current: Float = 0F,
    var goal: Float = 0F,
    var state: Byte = 0,
    var type: String = ""
) {
    fun toJson() = buildJsonObject {
        put("id", id)
        put("current", current)
        put("goal", goal)
        put("state", state)
        put("type", type)
    }
}