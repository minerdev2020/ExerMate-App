package com.minerdev.exermate.network.api

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @POST("signup")
    fun signUp(@Body user: JsonElement): Call<JsonObject>

    @GET("get-data")
    fun read(): Call<JsonObject>

    @Multipart
    @POST("set-proflie")
    fun updateProfile(@Part imageFile: MultipartBody.Part): Call<JsonObject>

    @POST("set-status-msg")
    fun updateStateMsg(@Body user: JsonElement): Call<JsonObject>

    @GET("get-walk-records")
    fun readAllWalkRecords(): Call<JsonObject>

    @POST("add-walk-record")
    fun addWalkRecord(@Body walkRecord: JsonElement): Call<JsonObject>

    @GET("get-chat-room")
    fun readAllJoinedChatRooms(): Call<JsonObject>

    @GET("join-chat-room")
    fun joinChatRoom(@Query("chatRoomID") roomId: Int): Call<JsonObject>

    @GET("exit-chat-room")
    fun leaveChatRoom(@Query("chatRoomID") roomId: Int): Call<JsonObject>
}