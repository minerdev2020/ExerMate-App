package com.minerdev.exermate.network

import android.util.Log
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

open class ChatWebSocketListener(private val userEmail: String, private val userPw: String) :
    WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        val msg = buildJsonObject {
            put("bizType", "USER_LOGIN")
            put("useremail", userEmail)
            put("password", userPw)
            put("text", "user login")
        }
        webSocket.send(msg.toString())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("Socket", "Receiving : $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("Socket", "Receiving bytes : $bytes")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Socket", "Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Socket", "Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Socket", "Error : " + t.message)
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}