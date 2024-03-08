package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080/") // 실제 서버 주소로 변경하세요.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(UserService::class.java)

        findViewById<Button>(R.id.signupCompletionButtton).setOnClickListener {
            val username = findViewById<EditText>(R.id.signName).text.toString()
            val userId = findViewById<EditText>(R.id.signId).text.toString()
            val userPw = findViewById<EditText>(R.id.signPw).text.toString()
            val userPwCheck = findViewById<EditText>(R.id.signPwCheck).text.toString()
            val userPhone = findViewById<EditText>(R.id.signPhone).text.toString()
            val userAddress = findViewById<EditText>(R.id.signAddress).text.toString()
            val userNickname = findViewById<EditText>(R.id.signNickname).text.toString()
            val userGender = "남성" // 성별 입력 처리 로직 추가 필요

            /*val username = "antest"
            val userId = "antest"
            val userPw = "antest!@"
            val userPwCheck = "antest!@"
            val userPhone = "010-0000-1111"
            val userAddress = "ananananana"
            val userNickname = "android"
            val userGender = "남성" // 성별 입력 처리 로직 추가 필요*/

            val user = User(userPhone, userId, username, userPw, userAddress, userNickname, userGender)

            if (username.isEmpty() || userId.isEmpty() || userPw.isEmpty() ||
                userPhone.isEmpty() || userAddress.isEmpty() || userNickname.isEmpty()) {

                // 사용자에게 모든 입력란을 채워야 한다는 메시지를 보여줍니다.
                Toast.makeText(this@SignUpActivity, "모든 필드를 채워주세요.", Toast.LENGTH_LONG).show()
            }
            else if(userPw != userPwCheck){
                //비밀번호 입력 확인
                Toast.makeText(this@SignUpActivity, "비밀번호가 다릅니다", Toast.LENGTH_LONG).show()
            }
            else {
                userService.registerUser(user).enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.body()?.message == "회원가입 성공") {
                            Toast.makeText(this@SignUpActivity, "성공: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                            // 성공 처리, 예를 들어 로그인 화면으로 이동
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else if(response.body()?.message == "이미 존재하는 ID 입니다."){
                            Toast.makeText(this@SignUpActivity, "이미  존재하는 ID입니다. ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this@SignUpActivity, "오류 ${response.code()}", Toast.LENGTH_LONG).show()

                        }
                    }
                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "실패: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        findViewById<Button>(R.id.signbackButton).setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
    interface UserService {
        @POST("user/signup")
        fun registerUser(@Body user: User): Call<UserResponse>
}
}
data class User(
    val user_phone: String,
    val user_id: String,
    val user_name: String,
    val user_pw: String,
    val user_address: String,
    val nickname: String,
    val male: String
)

data class UserResponse(
    val message: String
)
