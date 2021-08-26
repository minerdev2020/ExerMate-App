package com.minerdev.exermate.network

import com.minerdev.exermate.utils.Constants
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("${Constants.API_AUTH}login")
    fun login(@Body user: JsonElement): Call<JsonObject>

    @POST("${Constants.API_AUTH}logout")
    fun logout(): Call<JsonObject>


    @POST("${Constants.API_USER}signup")
    fun signUp(@Body user: JsonElement): Call<JsonObject>

    @GET("${Constants.API_USER}get-data")
    fun read(): Call<JsonObject>

    @Multipart
    @POST("${Constants.API_USER}set-proflie")
    fun updateProfile(@Part imageFile: MultipartBody.Part): Call<JsonObject>

    @POST("${Constants.API_USER}set-status-msg")
    fun updateStateMsg(@Body user: JsonElement): Call<JsonObject>

    @GET("${Constants.API_USER}get-walk-records")
    fun readAllWalkRecords(): Call<JsonObject>

    @POST("${Constants.API_USER}add-walk-record")
    fun addWalkRecord(@Body walkRecord: JsonElement): Call<JsonObject>

    @GET("${Constants.API_USER}get-chat-room")
    fun readAllJoinedChatRooms(): Call<JsonObject>

    @GET("${Constants.API_USER}join-chat-room")
    fun joinChatRoom(@Query("chatRoomID") roomId: Int): Call<JsonObject>

    @GET("${Constants.API_USER}exit-chat-room")
    fun leaveChatRoom(@Query("chatRoomID") roomId: Int): Call<JsonObject>


    @GET("${Constants.API_CHATROOM}get-user-list")
    fun readAllMembers(@Query("chatRoomId") id: Int): Call<JsonObject>


    @GET("${Constants.API_POST}get-exer-post-list")
    fun readAll(): Call<JsonObject>

    @POST("${Constants.API_POST}create")
    fun create(@Body post: JsonElement): Call<JsonObject>

    @GET("${Constants.API_POST}get-exer-post")
    fun read(@Query("exerPostID") id: Int): Call<JsonObject>

    @POST("${Constants.API_POST}modify")
    fun update(@Body post: JsonElement): Call<JsonObject>
}