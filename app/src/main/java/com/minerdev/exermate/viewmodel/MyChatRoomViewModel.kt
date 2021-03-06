package com.minerdev.exermate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minerdev.exermate.model.ChatRoom
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.service.UserService
import com.minerdev.exermate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class MyChatRoomViewModel : ViewModel() {
    val chatRoomList = MutableLiveData<List<ChatRoom>>()

    fun loadChatRooms() {
        val callBack = BaseCallBack(
            { code, response ->
                val jsonObject = JSONObject(response)
                val result = jsonObject.getString("result")
                val format = Json { ignoreUnknownKeys = true }
                chatRoomList.postValue(format.decodeFromString<List<ChatRoom>>(result))
            }
        )

        if (Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
            CoroutineScope(Dispatchers.IO).launch {
                UserService.readAllJoinedChatRooms(callBack)
            }
        }
    }
}