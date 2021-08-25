package com.minerdev.exermate.network.api

import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatRoomApi {
    @GET("get-user-list")
    fun readAllMembers(@Query("chatRoomId") id: Int): Call<JsonObject>
}