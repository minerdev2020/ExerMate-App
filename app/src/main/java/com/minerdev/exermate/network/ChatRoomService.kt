package com.minerdev.exermate.network

import com.minerdev.exermate.model.ChatRoom
import com.minerdev.exermate.utils.Constants

object ChatRoomService {
    private val client =
        RetrofitClient.getClient(Constants.API_CHATROOM)?.create(ChatRoomApi::class.java)

    fun create(chatRoom: ChatRoom, callBack: BaseCallBack) {
        val call = client?.create(chatRoom.toJson()) ?: return
        call.enqueue(callBack)
    }

    fun read(id: Int, callBack: BaseCallBack) {
        val call = client?.read(id) ?: return
        call.enqueue(callBack)
    }

    fun readAllMembers(id: Int, callBack: BaseCallBack) {
        val call = client?.readAllMembers(id) ?: return
        call.enqueue(callBack)
    }

    fun addMember(userId: String, callBack: BaseCallBack) {
        val call = client?.addMember(userId) ?: return
        call.enqueue(callBack)
    }

    fun deleteMember(userId: String, callBack: BaseCallBack) {
        val call = client?.deleteMember(userId) ?: return
        call.enqueue(callBack)
    }
}