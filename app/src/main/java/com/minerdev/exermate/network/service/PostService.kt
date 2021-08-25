package com.minerdev.exermate.network.service

import com.minerdev.exermate.model.Post
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.RetrofitClient
import com.minerdev.exermate.network.api.PostApi
import com.minerdev.exermate.utils.Constants

object PostService {
    private val client = RetrofitClient.getClient(Constants.API_POST)?.create(PostApi::class.java)

    fun readAll(callBack: BaseCallBack) {
        val call = client?.readAll() ?: return
        call.enqueue(callBack)
    }

    fun create(post: Post, callBack: BaseCallBack) {
        val call = client?.create(post.toJson()) ?: return
        call.enqueue(callBack)
    }

    fun read(id: Int, callBack: BaseCallBack) {
        val call = client?.read(id) ?: return
        call.enqueue(callBack)
    }

    fun update(post: Post, callBack: BaseCallBack) {
        val call = client?.update(post.toJson()) ?: return
        call.enqueue(callBack)
    }
}