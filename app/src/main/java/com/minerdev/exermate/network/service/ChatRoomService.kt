package com.minerdev.exermate.network.service

import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.RetrofitClient

object ChatRoomService {
    private val client = RetrofitClient.getClient()

    fun readAllMembers(id: Int, callBack: BaseCallBack) {
        val call = client?.readAllMembers(id) ?: return
        call.enqueue(callBack)
    }
}