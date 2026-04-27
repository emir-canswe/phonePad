package com.phonepad.utils

import org.json.JSONObject
import com.phonepad.network.WebSocketClient

class GestureHandler(private val webSocketClient: WebSocketClient) {
    
    // Hassasiyet çarpanı varsayılan 1.5x
    var sensitivity: Float = 1.5f
    
    fun sendMove(dx: Float, dy: Float) {
        val payload = JSONObject().apply {
            put("type", "move")
            put("dx", dx * sensitivity)
            put("dy", dy * sensitivity)
        }
        webSocketClient.sendAction(payload)
    }

    fun sendClick(button: String = "left") {
        val payload = JSONObject().apply {
            put("type", "click")
            put("button", button)
        }
        webSocketClient.sendAction(payload)
    }

    fun sendDoubleClick() {
        val payload = JSONObject().apply {
            put("type", "double_click")
            put("button", "left")
        }
        webSocketClient.sendAction(payload)
    }

    fun sendScroll(dy: Float) {
        val payload = JSONObject().apply {
            put("type", "scroll")
            put("dy", dy * sensitivity)
        }
        webSocketClient.sendAction(payload)
    }
    
    fun sendDrag(dx: Float, dy: Float) {
        val payload = JSONObject().apply {
            put("type", "drag")
            put("dx", dx * sensitivity)
            put("dy", dy * sensitivity)
        }
        webSocketClient.sendAction(payload)
    }
}
