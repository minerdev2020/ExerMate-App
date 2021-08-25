package com.minerdev.exermate.network.api

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {
    @GET("get-exer-post-list")
    fun readAll(): Call<JsonObject>

    @POST("create")
    fun create(@Body post: JsonElement): Call<JsonObject>

    @GET("get-exer-post")
    fun read(@Query("exerPostID") id: Int): Call<JsonObject>

    @POST("modify")
    fun update(@Body post: JsonElement): Call<JsonObject>
}