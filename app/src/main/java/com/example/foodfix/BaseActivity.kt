package com.example.foodfix

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

            // 사용자의 JWT 토큰을 가져옴
            val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val token = sharedPref.getString("jwt_token", null) ?: ""

            // Retrofit 인스턴스 생성 및 UserService 가져오기
            val retrofit = Retrofit.Builder()
                .baseUrl("http://54.180.213.178:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val userService = retrofit.create(UserService::class.java)

            // 로그아웃 요청
            userService.logoutUser("Bearer $token").enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 로컬에서 JWT 토큰 삭제
                        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            remove("jwt_token") // 'jwt_token' 키로 저장된 토큰 삭제
                            apply()
                        }
                    } else {
                        // 서버로부터의 응답 실패 처리
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 네트워크 에러 또는 요청 처리 실패 시
                }
            })

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

