package com.phonepad.network

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WebSocketClient {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    private val _connectionStatus = MutableStateFlow(false)
    val connectionStatus: StateFlow<Boolean> = _connectionStatus

    fun connect(ip: String, port: String) {
        val url = "ws://$ip:$port"
        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionStatus.value = true
                Log.d("WebSocket", "Connected to $url")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionStatus.value = false
                Log.d("WebSocket", "Closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionStatus.value = false
                Log.e("WebSocket", "Error", t)
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        _connectionStatus.value = false
    }

    fun sendAction(action: JSONObject) {
        if (_connectionStatus.value) {
            webSocket?.send(action.toString())
        }
    }
}
