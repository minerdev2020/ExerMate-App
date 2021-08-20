package com.minerdev.exermate.network

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatRoomApi {
    @POST("make-chat-room")
    fun create(@Body chatRoom: JsonElement): Call<JsonObject>

    @GET("get-chat-room")
    fun read(@Query("id") id: Int): Call<JsonObject>

    @GET("get-user")
    fun readAllMembers(@Query("id") id: Int): Call<JsonObject>

    @GET("add-user")
    fun addMember(@Query("email") email: String): Call<JsonObject>

    @GET("delete-user")
    fun deleteMember(@Query("email") email: String): Call<JsonObject>
}