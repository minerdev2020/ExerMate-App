package com.minerdev.exermate.network

import android.util.Log
import com.minerdev.exermate.utils.Constants
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseCallBack(
    private val onAcceptance: (code: Int, response: String) -> Unit = { _, _ -> },
    private val onRejection: (code: Int, response: String) -> Unit = { _, _ -> },
    private val onFailure: (error: Throwable) -> Unit = { error: Throwable ->
        Log.d(Constants.TAG, "Network Error : " + error.localizedMessage)
    }
) : Callback<JsonObject> {
    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        if (response.isSuccessful) {
            response.body()?.let {
                Log.d(Constants.TAG, "code : ${response.code()}, response : $it")
                onAcceptance(response.code(), it.toString())
            }

        } else {
            response.errorBody()?.let {
                Log.d(Constants.TAG, "code : ${response.code()}, response : $it")
                onRejection(response.code(), it.string())
            }
        }
    }

    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
        onFailure(t)
    }
}