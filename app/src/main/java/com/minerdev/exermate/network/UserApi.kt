package com.minerdev.exermate.network

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @GET("data")
    fun read(@Query("email") email: String): Call<JsonObject>

    @POST("modify")
    fun modify(@Body user: JsonElement): Call<JsonObject>

    @POST("set-proflie-url")
    fun updateProfile(@Body user: JsonElement): Call<JsonObject>

    @POST("set-status-msg")
    fun updateStateMsg(@Body user: JsonElement): Call<JsonObject>

    @GET("walk-records")
    fun readAllWalkRecords(@Query("email") email: String): Call<JsonObject>

    @GET("add-walk-records")
    fun addWalkRecord(@Query("email") email: String): Call<JsonObject>

    @GET("get-chat-room")
    fun readAllJoinedChatRooms(@Query("roomId") roomId: Int): Call<JsonObject>

    @GET("join-chat-room")
    fun joinChatRoom(@Query("roomId") roomId: Int): Call<JsonObject>

    @GET("exit-chat-room")
    fun leaveChatRoom(@Query("roomId") roomId: Int): Call<JsonObject>
}