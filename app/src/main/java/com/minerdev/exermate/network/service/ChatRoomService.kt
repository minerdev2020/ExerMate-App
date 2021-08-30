package com.minerdev.exermate.network.service

import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object ChatRoomService {
    private val client = RetrofitClient.getClient()

    fun readAllMembers(id: String, callBack: BaseCallBack) {
        val call = client?.readAllMembers(id) ?: return
        call.enqueue(callBack)
    }

    fun sendImage(roomId: String, path: String, callBack: BaseCallBack) {
        val textBody = roomId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val file = File(path)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imageBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = client?.sendImage(textBody, imageBody) ?: return
        call.enqueue(callBack)
    }
}