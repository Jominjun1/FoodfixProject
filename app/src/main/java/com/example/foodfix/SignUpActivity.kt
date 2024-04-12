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

            // 사용자 ID 검증을 위한 정규 표현식
            val userIdPattern = "^[a-zA-Z0-9]{4,10}$".toRegex()
            // 비밀번호 검증을 위한 정규 표현식
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}$".toRegex()
            // 닉네임 검증을 위한 정규 표현식
            val nicknamePattern = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$".toRegex()

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
            else if(!userId.matches(userIdPattern)){
                    Toast.makeText(this, "사용자 ID는 4자리에서 10자리 사이의 대소문자 영어 또는 숫자여야 합니다.", Toast.LENGTH_LONG).show()
            }
            else  if (!userPw.matches(passwordPattern)) {
                // 비밀번호가 조건에 맞지 않을 경우
                Toast.makeText(this, "비밀번호는 대소문자 영문자, 숫자, 특수문자를 포함하는 8~16자리여야 합니다.", Toast.LENGTH_LONG).show()
            }
            else if (!userNickname.matches(nicknamePattern)) {
                // 닉네임이 조건에 맞지 않을 경우
                Toast.makeText(this, "닉네임은 한글, 영문 소문자, 숫자를 포함하는 2~10자리여야 합니다.", Toast.LENGTH_LONG).show()
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
