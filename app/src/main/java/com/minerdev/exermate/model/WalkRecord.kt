package com.minerdev.exermate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class WalkRecord(
    var id: String = "",
    var userId: String = "",
    @SerialName("date") var createdAt: Long = 0,
    @SerialName("walkNum") var stepCount: Int = 0
) {
    fun toJson() = buildJsonObject {
        put("id", id)
        put("userId", userId)
        put("date", createdAt)
        put("walkNum", stepCount)
    }
}