package com.minerdev.exermate.network.service

import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.RetrofitClient
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object UserService {
    private val client = RetrofitClient.getClient()

    fun signUp(id: String, pw: String, nickname: String, callBack: BaseCallBack) {
        val body = buildJsonObject {
            put("useremail", id)
            put("password", pw)
            put("nickName", nickname)
        }
        val call = client?.signUp(body) ?: return
        call.enqueue(callBack)
    }

    fun read(callBack: BaseCallBack) {
        val call = client?.read() ?: return
        call.enqueue(callBack)
    }

    fun updateProfile(path: String, callBack: BaseCallBack) {
        val file = File(path)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profile", file.name, requestFile)
        val call = client?.updateProfile(body) ?: return
        call.enqueue(callBack)
    }

    fun updateStatusMsg(statusMsg: String, callBack: BaseCallBack) {
        val body = buildJsonObject {
            put("statusMsg", statusMsg)
        }
        val call = client?.updateStatusMsg(body) ?: return
        call.enqueue(callBack)
    }

    fun readAllWalkRecords(callBack: BaseCallBack) {
        val call = client?.readAllWalkRecords() ?: return
        call.enqueue(callBack)
    }

    fun addWalkRecord(walkCount: Int, date: Long, callBack: BaseCallBack) {
        val body = buildJsonObject {
            put("walkNum", walkCount)
            put("date", date)
        }
        val call = client?.addWalkRecord(body) ?: return
        call.enqueue(callBack)
    }

    fun readAllJoinedChatRooms(callBack: BaseCallBack) {
        val call = client?.readAllJoinedChatRooms() ?: return
        call.enqueue(callBack)
    }

    fun joinChatRoom(roomId: String, callBack: BaseCallBack) {
        val call = client?.joinChatRoom(roomId) ?: return
        call.enqueue(callBack)
    }

    fun leaveChatRoom(roomId: String, callBack: BaseCallBack) {
        val call = client?.leaveChatRoom(roomId) ?: return
        call.enqueue(callBack)
    }
}