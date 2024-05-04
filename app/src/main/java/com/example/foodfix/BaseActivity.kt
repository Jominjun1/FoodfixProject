package com.example.foodfix

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    private var backPressedTwice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        if (backPressedTwice) {
            // 웹소켓 해제
            val clearWebSocket = WebSocketManager.getWebSocket()
            clearWebSocket?.let {
                WebSocketManager.disconnectWebSocket()
            }
            super.onBackPressed() // 앱 종료
            finishAffinity()
            return
        }

        backPressedTwice = true
        Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            backPressedTwice = false
        }, 1000) // 1초 내에 다시 눌러야 함
    }
}

