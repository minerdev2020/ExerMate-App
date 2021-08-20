package com.minerdev.exermate.network

import com.minerdev.exermate.utils.Constants
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object UserService {
    private val client = RetrofitClient.getClient(Constants.API_USER)?.create(UserApi::class.java)

    fun read(id: String, callBack: BaseCallBack) {
        val call = client?.read(id) ?: return
        call.enqueue(callBack)
    }

    fun modify(id: String, pw: String, nickname: String, callBack: BaseCallBack) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
            put("nickname", nickname)
        }
        val call = client?.modify(user) ?: return
        call.enqueue(callBack)
    }

    fun updateProfile(id: String, callBack: BaseCallBack) {
        val user = buildJsonObject {
            put("email", id)
        }
        val call = client?.updateProfile(user) ?: return
        call.enqueue(callBack)
    }

    fun updateStateMsg(id: String, stateMsg: String, callBack: BaseCallBack) {
        val user = buildJsonObject {
            put("email", id)
            put("stateMsg", stateMsg)
        }
        val call = client?.updateStateMsg(user) ?: return
        call.enqueue(callBack)
    }

    fun readAllWalkRecords(id: String, callBack: BaseCallBack) {
        val call = client?.readAllWalkRecords(id) ?: return
        call.enqueue(callBack)
    }

    fun addWalkRecord(id: String, callBack: BaseCallBack) {
        val call = client?.addWalkRecord(id) ?: return
        call.enqueue(callBack)
    }

    fun readAllJoinedChatRooms(roomId: Int, callBack: BaseCallBack) {
        val call = client?.readAllJoinedChatRooms(roomId) ?: return
        call.enqueue(callBack)
    }

    fun joinChatRoom(roomId: Int, callBack: BaseCallBack) {
        val call = client?.joinChatRoom(roomId) ?: return
        call.enqueue(callBack)
    }

    fun leaveChatRoom(roomId: Int, callBack: BaseCallBack) {
        val call = client?.leaveChatRoom(roomId) ?: return
        call.enqueue(callBack)
    }
}