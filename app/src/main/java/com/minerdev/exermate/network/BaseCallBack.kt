package com.minerdev.exermate.network

import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseCallBack(
    private val onAcceptance: (code: Int, response: String) -> Unit = { _, _ -> },
    private val onRejection: (code: Int, response: String) -> Unit = { _, _ -> },
    private val onFailure: (error: Throwable) -> Unit = { _ -> }
) : Callback<JsonObject> {
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
}