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
                    findViewById<EditText>(R.id.profilemngNickname).setText(nickname)
                    val phone = response.body()?.phone ?: ""
                    findViewById<EditText>(R.id.profilemngphone).setText(phone)
                    val address = response.body()?.address ?: ""
                    findViewById<EditText>(R.id.profilemngaddress).setText(address)

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

        //뒤로가기 버튼
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

        //비밀번호 변경 버튼 이벤트
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

        //저장 버튼 이벤트
        findViewById<Button>(R.id.saveprofileButton).setOnClickListener {
            val nickname = findViewById<EditText>(R.id.profilemngNickname).text.toString()
            val phone = findViewById<EditText>(R.id.profilemngphone).text.toString()
            val address = findViewById<EditText>(R.id.profilemngaddress).text.toString()
            if (nickname.isBlank() || phone.isBlank() || address.isBlank()) {
                // 하나라도 비어 있으면 사용자에게 알립니다.
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                // 다이얼로그를 생성하고 설정합니다.
                val builder = AlertDialog.Builder(this)
                builder.setTitle("프로필 변경")
                builder.setMessage("저장하시겠습니까?")

                // "확인" 버튼
                builder.setPositiveButton("확인") { dialog, which ->
                    // "확인" 버튼 클릭 시 실행할 코드

                    val userInfo = UserProfileResponse(nickname, phone, address) // 사용자가 입력한 정보로 객체 생성

                    userService.updateUser("Bearer $token", userInfo).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@ProfileManagement, "사용자 정보가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                                // 성공적으로 업데이트되면 다른 액티비티로 이동하거나 UI를 업데이트합니다.
                            } else {
                                Toast.makeText(this@ProfileManagement, "정보 업데이트 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@ProfileManagement, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })

                    //화면 전환
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

        findViewById<TextView>(R.id.secede).setOnClickListener {
            // 다이얼로그를 생성하고 설정합니다.
            val builder = AlertDialog.Builder(this)
            builder.setTitle("탈퇴")
            builder.setMessage("정말 탈퇴 하시겠습니까?")

            // "확인" 버튼
            builder.setPositiveButton("확인") { dialog, which ->
                // "확인" 버튼 클릭 시 실행할 코드
                deleteUser()

                val intent = Intent(this, LoginActivity::class.java)
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
    private fun deleteUser() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080") // 실제 서버 URL로 변경하세요
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)

        // SharedPreferences에서 JWT 토큰 가져오기
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("jwt_token", null) ?: ""

        userService.deleteUser("Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 회원 탈퇴 성공 처리
                    Toast.makeText(applicationContext, "회원 탈퇴가 성공적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                    // 탈퇴 후 로그인 화면 등으로 이동 처리
                } else {
                    // 실패 처리
                    Toast.makeText(applicationContext, "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 네트워크 에러 처리
                Toast.makeText(applicationContext, "네트워크 에러: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

}