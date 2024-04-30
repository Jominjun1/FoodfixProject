package com.example.foodfix

import okhttp3.WebSocket

object WebSocketManager {
    private var webSocket: WebSocket? = null

    fun setWebSocket(socket: WebSocket) {
        webSocket = socket
    }

    fun getWebSocket(): WebSocket? {
        return webSocket
    }

    fun disconnectWebSocket() {
        webSocket?.close(1000, "로그아웃으로 인한 웹소켓 해제")
        webSocket = null
    }
}
