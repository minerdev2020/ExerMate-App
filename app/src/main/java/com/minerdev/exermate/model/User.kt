package com.minerdev.exermate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class User(
    var id: Int = 0,
    var createdAt: Long = 0,
    var email: String = "",
    @SerialName("nickName") var nickname: String = "",
    @SerialName("profileRoute") var profileUrl: String = "",
    var statusMsg: String = ""
) {
    fun toJson() = buildJsonObject {
        put("useremail", email)
        put("nickName", nickname)
        put("profileRoute", profileUrl)
        put("statusMsg", statusMsg)
    }
}