package com.minerdev.exermate.network

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {
    @GET("get-exer-posts")
    fun readAll(): Call<JsonObject>

    @POST("make-exer-post")
    fun create(@Body post: JsonElement): Call<JsonObject>

    @GET("get-exer-post")
    fun read(@Query("id") id: Int): Call<JsonObject>

    @POST("modify-exer-post")
    fun update(@Body post: JsonElement): Call<JsonObject>

    @GET("delete-exer-post")
    fun delete(@Query("postId") postId: Int): Call<JsonObject>
}