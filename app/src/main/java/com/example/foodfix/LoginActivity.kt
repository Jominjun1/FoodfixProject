package com.example.foodfix

import MyWebSocketListener
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        // 앱 알림 권한 설정
        checkNotificationPermission()

        findViewById<Button>(R.id.signupButton).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.kakaoButton).setOnClickListener {

        }

        //로그인 기능
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val user_id = findViewById<EditText>(R.id.login_id).text.toString()
            val user_pw = findViewById<EditText>(R.id.login_pw).text.toString()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://54.180.213.178:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val userService = retrofit.create(UserService::class.java)

            val loginRequest = LoginRequest(user_id, user_pw)
            userService.loginUser(loginRequest).enqueue(object : Callback<ResponseBody> {
                // 로그인 기능 내 onResponse 수정 부분
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        response.body()?.string()?.let { responseBodyString ->
                            Log.d("LoginActivity", "Response: $responseBodyString")

                            // Gson을 사용하여 응답 본문 파싱
                            val gson = Gson()
                            val loginResponse = gson.fromJson(responseBodyString, LoginResponse::class.java)
                            val token = loginResponse.token

                            if (token != null && token.isNotEmpty()) {

                                Log.d("LoginActivity","--------------------------------------------------------------")
                                Log.d("LoginActivity", "Received JWT Token: $token")
                                // JWT 토큰 저장 처리
                                val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                                with (sharedPref.edit()) {
                                    putString("jwt_token", token) // 'jwt_token' 키에 토큰 저장
                                    putString("user_id", user_id)
                                    putString("user_pw", user_pw)
                                    apply()
                                }

                                Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                                // 웹소켓 연결
                                connectWebSocket()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // 토큰이 없거나 비어있는 경우 로그인 실패 처리
                                Toast.makeText(this@LoginActivity, "Login Failed: No Token Received", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // 서버로부터의 응답이 실패했을 때의 처리
                        Toast.makeText(this@LoginActivity, "Login Failed: Server Response Error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 네트워크 에러 또는 요청 처리 실패 시
                    Toast.makeText(this@LoginActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", "Network Error: ", t)
                    t.printStackTrace() }
                }
            )
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Notification Settings page not found.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun connectWebSocket() {

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("user_id", "").toString() // 사용자 아이디 가져오기
        // OkHttpClient 생성
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()

        // 웹소켓 요청 생성
        val request = Request.Builder()
            .url("ws://54.180.213.178:8080/wsk?user_id=$userId")
            .build()

        // Context를 안전하게 전달
        val listener = MyWebSocketListener(getApplicationContext())

        // 웹소켓 생성 및 연결
        val webSocket = client.newWebSocket(request, listener)

        // WebSocketManager에 웹소켓 설정
        WebSocketManager.setWebSocket(webSocket)
    }
}
data class LoginRequest(val user_id: String, val user_pw: String)

data class LoginResponse(val token: String?)


