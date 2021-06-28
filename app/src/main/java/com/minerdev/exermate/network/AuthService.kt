package com.minerdev.exermate.network

import com.minerdev.exermate.utils.Constants.API_AUTH
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthService {
    private val client = RetrofitClient.getClient(API_AUTH)?.create(AuthApi::class.java)

    fun login(
        id: String,
        pw: String,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
        }
        val call = client?.login(user) ?: return
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun logout(
        id: String,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val user = buildJsonObject {
            put("user_id", id)
        }
        val call = client?.logout(user) ?: return
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun register(
        id: String,
        pw: String,
        nickname: String,
        onAcceptance: (code: Int, response: String) -> Unit,
        onRejection: (code: Int, response: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        val user = buildJsonObject {
            put("user_id", id)
            put("user_pw", pw)
            put("nickname", nickname)
        }
        val call = client?.register(user) ?: return
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onAcceptance(response.code(), it.toString())
                    }

                } else {
                    response.errorBody()?.let {
                        onRejection(response.code(), it.string())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}