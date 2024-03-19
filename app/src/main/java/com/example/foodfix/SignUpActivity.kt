package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignUpActivity : AppCompatActivity() {

    private var selectedValue = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080/") // 실제 서버 주소로 변경하세요.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(UserService::class.java)

        val radioGroup = findViewById<RadioGroup>(R.id.signGender)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // 선택된 라디오 버튼에 따라 selectedValue 값을 변경합니다.
            selectedValue = when (checkedId) {
                R.id.maleButton -> 0
                R.id.femaleButton -> 1
                else -> selectedValue
            }
            // 선택된 값을 확인하기 위한 로그 출력
            Log.d("Selected Value", selectedValue.toString())
        }

        findViewById<Button>(R.id.signupCompletionButtton).setOnClickListener {
            val username = findViewById<EditText>(R.id.signName).text.toString()
            val userId = findViewById<EditText>(R.id.signId).text.toString()
            val userPw = findViewById<EditText>(R.id.signPw).text.toString()
            val userPwCheck = findViewById<EditText>(R.id.signPwCheck).text.toString()
            val userPhone = findViewById<EditText>(R.id.signPhone).text.toString()
            val userAddress = findViewById<EditText>(R.id.signAddress).text.toString()
            val userNickname = findViewById<EditText>(R.id.signNickname).text.toString()
            val userGender = selectedValue.toString()

            /*val username = "androidname"
            val userId = "android"
            val userPw = "android!@"
            val userPwCheck = "android!@"
            val userPhone = "010-7777-8888"
            val userAddress = "ananananana"
            val userNickname = "androidNick"
            val userGender = "0" // 성별 입력 처리 로직 추가 필요 0 = 남자, 1 = 여자*/

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
            userService.registerUser(user).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 성공적인 응답 처리
                        response.body()?.let { responseBody ->
                            val responseString = responseBody.string() // 응답을 문자열로 변환
                            if (responseString.contains("회원가입 성공")) {
                                Toast.makeText(this@SignUpActivity, "성공: $responseString", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // 예상치 못한 성공 메시지 처리
                                Toast.makeText(this@SignUpActivity, "응답: $responseString", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        // 에러 응답 처리
                        val errorResponseString = response.errorBody()?.string() ?: "알 수 없는 오류 발생"
                        Toast.makeText(this@SignUpActivity, "오류: $errorResponseString", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("SignUpActivity", "네트워크 요청 실패: ${t.message}")
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
}
data class User(
    val user_phone: String,
    val user_id: String,
    val user_name: String,
    val user_pw: String,
    val user_address: String,
    val nickname: String,
    val gender: String
)

data class UserResponse(
    val message: String
)
