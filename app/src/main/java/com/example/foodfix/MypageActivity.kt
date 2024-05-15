package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypageActivity : BaseActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // 여기서는 별도의 결과 처리가 필요 없습니다.
        }

        supportActionBar?.hide()

        // 사용자의 JWT 토큰을 가져옴
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("jwt_token", null) ?: ""

        // Retrofit 인스턴스 생성 및 UserService 가져오기
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)

        // 사용자 프로필 정보 요청
        userService.getUserProfile("Bearer $token").enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    // 서버로부터 응답을 성공적으로 받았을 경우, 닉네임을 화면에 표시
                    val nickname = response.body()?.nickname ?: ""
                    findViewById<TextView>(R.id.mypageNickname).text = nickname
                } else {
                    // 서버 응답 실패 처리
                    Toast.makeText(this@MypageActivity, "Failed to fetch user profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                // 네트워크 에러 처리
                Toast.makeText(this@MypageActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        findViewById<Button>(R.id.mypageBackButton).setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.mypage2orderHistory).setOnClickListener {
            val intent = Intent(this, PackingstatusActivity::class.java)
            resultLauncher.launch(intent)
        }

        findViewById<Button>(R.id.mypage2reservation).setOnClickListener {
            val intent = Intent(this, ReservationstatusActivity::class.java)
            resultLauncher.launch(intent)
        }

        findViewById<Button>(R.id.mypage2myReview).setOnClickListener {
            val intent = Intent(this, MyreviewActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.mypage2changeProfile).setOnClickListener {
            val intent = Intent(this, ProfileManagement::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<TextView>(R.id.logoutText).setOnClickListener{
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

                        // 웹소켓 해제
                        val clearWebSocket = WebSocketManager.getWebSocket()
                        clearWebSocket?.let {
                            WebSocketManager.disconnectWebSocket()
                        }

                        Toast.makeText(this@MypageActivity, "Logout Successful", Toast.LENGTH_SHORT).show()
                        // 로그아웃 성공 후, 로그인 화면으로 이동
                        val intent = Intent(this@MypageActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // 서버로부터의 응답 실패 처리
                        Toast.makeText(this@MypageActivity, "Logout Failed: Server Response Error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 네트워크 에러 또는 요청 처리 실패 시
                    Toast.makeText(this@MypageActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

