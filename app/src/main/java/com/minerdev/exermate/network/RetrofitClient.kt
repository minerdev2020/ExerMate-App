package com.minerdev.exermate.network

import com.minerdev.exermate.utils.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    fun getClient(apiUrl: String): Retrofit? {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL + apiUrl)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}