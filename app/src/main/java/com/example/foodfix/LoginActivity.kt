package com.example.foodfix

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        findViewById<Button>(R.id.signupButton).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        //로그인 후 기능 테스트 위한 설정
        findViewById<Button>(R.id.kakaoButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
                                    apply()
                                }

                                Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
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
}
data class LoginRequest(val user_id: String, val user_pw: String)

data class LoginResponse(val token: String?)


