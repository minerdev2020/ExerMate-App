package com.minerdev.exermate.network.service

import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.RetrofitClient
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object AuthService {
    private val client = RetrofitClient.getClient()

    fun login(id: String, pw: String, callBack: BaseCallBack) {
        val body = buildJsonObject {
            put("useremail", id)
            put("password", pw)
        }
        val call = client?.login(body) ?: return
        call.enqueue(callBack)
    }

    fun logout(callBack: BaseCallBack) {
        val call = client?.logout() ?: return
        call.enqueue(callBack)
    }
}