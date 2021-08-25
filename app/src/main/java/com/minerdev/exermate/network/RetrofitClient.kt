package com.minerdev.exermate.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.minerdev.exermate.utils.Constants.BASE_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {
    fun getClient(apiUrl: String): Retrofit? {
        val contentType = "application/json".toMediaType()

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder()
        builder.interceptors().add(logInterceptor)
        val client: OkHttpClient = builder.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL + apiUrl)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(client)
            .build()
    }
}