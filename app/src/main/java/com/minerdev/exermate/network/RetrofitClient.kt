package com.minerdev.exermate.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.minerdev.exermate.utils.Constants.BASE_URL
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.CookieManager

object RetrofitClient {
    private var instance: Api? = null

    fun getClient(): Api? {
        if (instance == null) {
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .cookieJar(JavaNetCookieJar(CookieManager()))
                .addInterceptor(logInterceptor)
                .build()

            val contentType = "application/json".toMediaType()

            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory(contentType))
                .client(client)
                .build()
                .create(Api::class.java)
        }

        return instance
    }
}