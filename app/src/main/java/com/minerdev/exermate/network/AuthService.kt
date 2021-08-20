package com.minerdev.exermate.network

import com.minerdev.exermate.utils.Constants
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object AuthService {
    private val client = RetrofitClient.getClient(Constants.API_AUTH)?.create(AuthApi::class.java)

    fun login(id: String, pw: String, callBack: BaseCallBack) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
        }
        val call = client?.login(user) ?: return
        call.enqueue(callBack)
    }

    fun logout(id: String, callBack: BaseCallBack) {
        val user = buildJsonObject {
            put("user_id", id)
        }
        val call = client?.logout(user) ?: return
        call.enqueue(callBack)
    }

    fun signUp(id: String, pw: String, nickname: String, callBack: BaseCallBack) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
            put("nickname", nickname)
        }
        val call = client?.signUp(user) ?: return
        call.enqueue(callBack)
    }
}