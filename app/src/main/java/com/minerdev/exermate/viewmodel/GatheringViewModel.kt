package com.minerdev.exermate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.service.PostService
import com.minerdev.exermate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class GatheringViewModel : ViewModel() {
    val postList = MutableLiveData<List<Post>>()

    fun loadPosts() {
        val callBack = BaseCallBack(
            { code, response ->
                val jsonObject = JSONObject(response)
                val result = jsonObject.getString("result")
                val format = Json { ignoreUnknownKeys = true }
                postList.postValue(format.decodeFromString<List<Post>>(result))
            }
        )

        if (Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
            CoroutineScope(Dispatchers.IO).launch {
                PostService.readAll(callBack)
            }
        }
    }
}