package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RestaurantReservation : AppCompatActivity() {

    // 선택된 날짜를 저장할 변수 선언. 초기값은 오늘 날짜로 설정합니다.
    private var selectedDate: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_reservation)

        supportActionBar?.hide()

        // 사용자의 JWT 토큰을 가져옴
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        //val token = sharedPref.getString("jwt_token", null) ?: ""
        val user_id = sharedPref.getString("user_id", null)

        // 식당 정보
        val store_id = intent.getLongExtra("store_id", 0L)
        val store_name = intent.getStringExtra("store_name")

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080") // 서버 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(StoreService::class.java)

        findViewById<TextView>(R.id.restaurantName).text = store_name

        findViewById<Button>(R.id.restaurnatReservationBackButton).setOnClickListener {
            finish()
        }

        // 뷰 참조 가져오기
        val decreaseButton: Button = findViewById(R.id.decreaseButton)
        val increaseButton: Button = findViewById(R.id.increaseButton)
        val numPeopleTextView: TextView = findViewById(R.id.numpeople)

        // 감소 버튼 클릭 리스너
        decreaseButton.setOnClickListener {
            var numPeople = numPeopleTextView.text.toString().toInt()
            if (numPeople > 1) { // 0보다 작아지지 않도록 체크
                numPeople--
                numPeopleTextView.text = numPeople.toString()
            }
        }

        // 증가 버튼 클릭 리스너
        increaseButton.setOnClickListener {
            var numPeople = numPeopleTextView.text.toString().toInt()
            numPeople++
            numPeopleTextView.text = numPeople.toString()
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val monthFormatted = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            val dayFormatted = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            selectedDate = "$year-$monthFormatted-$dayFormatted"
            Log.d("SelectedDate", selectedDate)
        }

        //예약 완료 버튼
        findViewById<Button>(R.id.DoneButton).setOnClickListener {
            val numpeople = findViewById<TextView>(R.id.numpeople).text.toString()
            //val user_phone = findViewById<EditText>(R.id.user_phone).text.toString()

            // selectedDate 변수를 사용하여 LocalDate.parse() 부분을 수정합니다.
            val reservationDate = LocalDate.parse(selectedDate, DateTimeFormatter.ISO_DATE)
            val reservationTime = LocalTime.parse("18:00") // 시간은 예시로 고정값을 사용하였습니다.

            val reservationDTO = ReservationDTO(
                user_id = user_id,
                user_phone = "010-7777-5555",
                user_comments = "아이가 있어요",
                reservation_date = selectedDate,
                reservation_time = "18:00",
                people_cnt = numpeople.toInt(),
                store_id = store_id
            )
            service.createReservation(reservationDTO).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 예약 성공 처리
                        response.body()?.let { responseBody ->
                            val responseString = responseBody.string() // 응답을 문자열로 변환
                            if (responseString.contains("매장 예약 주문 성공")) {
                                Toast.makeText(this@RestaurantReservation, "성공: $responseString", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@RestaurantReservation, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // 예상치 못한 성공 메시지 처리
                                Toast.makeText(this@RestaurantReservation, "응답: $responseString", Toast.LENGTH_LONG).show()
                            }
                        }
                        /*val successMessage = ResponseBody.string() // "예약 성공" 메시지
                        //Log.d("Reservation", "예약 성공: ${successMessage}")
                        if (successMessage == "매장 예약 주문 성공"){
                            val intent = Intent(this@RestaurantReservation, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }*/
                    } else {
                        // 예약 실패 처리
                        val errorMessage = response.errorBody()?.string()
                        Log.e("Reservation", "예약 실패: $errorMessage")
                        Log.e("-------------reservationDate", "${reservationDate}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("Reservation", "네트워크 오류: ${t.message}")
                }
            })
        }
    }
}