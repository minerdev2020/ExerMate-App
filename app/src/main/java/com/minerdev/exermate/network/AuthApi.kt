package com.minerdev.exermate.network

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    fun login(@Body user: JsonElement): Call<JsonObject>

    @POST("logout")
    fun logout(@Body user: JsonElement): Call<JsonObject>

    @POST("signup")
    fun signUp(@Body user: JsonElement): Call<JsonObject>
}