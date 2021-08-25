package com.minerdev.exermate.network.service

import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.RetrofitClient
import com.minerdev.exermate.network.api.ChatRoomApi
import com.minerdev.exermate.utils.Constants

object ChatRoomService {
    private val client =
        RetrofitClient.getClient(Constants.API_CHATROOM)?.create(ChatRoomApi::class.java)

    fun readAllMembers(id: Int, callBack: BaseCallBack) {
        val call = client?.readAllMembers(id) ?: return
        call.enqueue(callBack)
    }
}