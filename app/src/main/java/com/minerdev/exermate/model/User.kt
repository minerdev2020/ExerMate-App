package com.minerdev.exermate.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class User(
    var id: Int = 0,
    var createdAt: String = "",
    var email: String = "",
    var nickname: String = "",
    var profileUrl: String = ""
) {
    fun toJson() = buildJsonObject {
        put("id", id)
        put("email", email)
        put("nickname", nickname)
    }
}