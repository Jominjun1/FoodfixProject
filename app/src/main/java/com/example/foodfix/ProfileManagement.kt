package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


class ProfileManagement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilemanagement)

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
                    findViewById<EditText>(R.id.profilemngNickname).hint = nickname
                    val phone = response.body()?.phone ?: ""
                    findViewById<EditText>(R.id.profilemngphone).hint = phone
                    val address = response.body()?.address ?: ""
                    findViewById<EditText>(R.id.profilemngaddress).hint = address

                    Log.d("ProfileManagement", "phone : $phone")
                } else {
                    // 서버 응답 실패 처리
                    Toast.makeText(this@ProfileManagement, "Failed to fetch user profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                // 네트워크 에러 처리
                Toast.makeText(this@ProfileManagement, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })


        findViewById<Button>(R.id.profilemngbackbutton).setOnClickListener {
            // 다이얼로그를 생성하고 설정합니다.
            val builder = AlertDialog.Builder(this)
            builder.setTitle("변경된 내용이 저장되지 않습니다")
            builder.setMessage("나가시겠습니까?")

            // "확인" 버튼
            builder.setPositiveButton("확인") { dialog, which ->
                // "확인" 버튼 클릭 시 실행할 코드
                val intent = Intent(this, MypageActivity::class.java)
                startActivity(intent)
                finish()
            }

            // "취소" 버튼
            builder.setNegativeButton("취소") { dialog, which ->
                dialog.dismiss() // 다이얼로그 닫기
            }

            // 다이얼로그를 표시합니다.
            builder.show()
        }

        findViewById<Button>(R.id.changePw).setOnClickListener {
            // 다이얼로그를 생성하고 설정합니다.
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog_layout, null)

            // AlertDialog.Builder를 사용하여 다이얼로그를 생성하고 설정합니다.
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            builder.setTitle("비밀번호 변경")

            // "확인" 버튼
            builder.setPositiveButton("확인") { dialog, which ->
                // 에딕텍스트에서 텍스트를 가져옵니다.
                val currentpassword = dialogView.findViewById<EditText>(R.id.currentpassword).text.toString()
                val changepassword = dialogView.findViewById<EditText>(R.id.changepassword).text.toString()
                val checkchangepassword = dialogView.findViewById<EditText>(R.id.checkchangepassword).text.toString()
                // 확인 버튼 클릭 시 할 일을 작성합니다.
                dialog.dismiss() // 다이얼로그 닫기
            }

            // "취소" 버튼
            builder.setNegativeButton("취소") { dialog, which ->
                // 취소 버튼 클릭 시 할 일을 작성합니다.
                dialog.dismiss() // 다이얼로그 닫기
            }

            // 다이얼로그를 표시합니다.
            val dialog = builder.create()
            dialog.show()
        }

        findViewById<Button>(R.id.saveprofileButton).setOnClickListener {
            // 다이얼로그를 생성하고 설정합니다.
            val builder = AlertDialog.Builder(this)
            builder.setTitle("프로필 변경")
            builder.setMessage("저장하시겠습니까?")

            // "확인" 버튼
            builder.setPositiveButton("확인") { dialog, which ->
                // "확인" 버튼 클릭 시 실행할 코드
                val intent = Intent(this, MypageActivity::class.java)
                startActivity(intent)
                finish()
            }

            // "취소" 버튼
            builder.setNegativeButton("취소") { dialog, which ->
                dialog.dismiss() // 다이얼로그 닫기
            }

            // 다이얼로그를 표시합니다.
            builder.show()
        }
    }
}