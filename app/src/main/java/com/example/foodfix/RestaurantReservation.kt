package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RestaurantReservation : BaseActivity() {

    // 선택된 날짜를 저장할 변수 선언. 초기값은 오늘 날짜로 설정합니다.
    private var selectedDate: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    // 선택된 시간을 저장할 변수 선언. 초기값은 빈 문자열로 설정합니다.
    private var selectedTime: String = ""

    private lateinit var button1100: Button
    private lateinit var button1200: Button
    private lateinit var button1300: Button
    private lateinit var button1700: Button
    private lateinit var button1800: Button
    private lateinit var button1900: Button

    private var currentlySelectedButton: Button? = null

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

        findViewById<ImageView>(R.id.restaurnatReservationBack).setOnClickListener {
            // 웹소켓 해제
            val clearWebSocket = WebSocketManager.getWebSocket()
            clearWebSocket?.let {
                WebSocketManager.disconnectWebSocket()
            }
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

        val today = Calendar.getInstance()

        // 오늘 날짜를 최소 날짜로 설정하여 과거 날짜를 선택할 수 없게 함
        calendarView.minDate = today.timeInMillis

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val monthFormatted = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            val dayFormatted = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            selectedDate = "$year-$monthFormatted-$dayFormatted"
            Log.d("SelectedDate", selectedDate)
        }

        // 버튼 참조 초기화
        button1100 = findViewById(R.id.button1100)
        button1200 = findViewById(R.id.button1200)
        button1300 = findViewById(R.id.button1300)
        button1700 = findViewById(R.id.button1700)
        button1800 = findViewById(R.id.button1800)
        button1900 = findViewById(R.id.button1900)

        // 모든 시간 버튼에 대한 클릭 리스너 설정
        val timeButtons = listOf(button1100, button1200, button1300, button1700, button1800, button1900)
        timeButtons.forEach { button ->
            button.setOnClickListener {
                currentlySelectedButton?.let {
                    // 이전에 선택된 버튼의 색상을 초기화
                    it.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray)
                    it.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
                // 현재 선택된 버튼을 강조 표시하고 저장
                highlightSelectedButton(it as Button)
                currentlySelectedButton = it // 현재 선택된 버튼을 저장

                // 클릭된 버튼의 텍스트를 selectedTime 변수에 저장
                selectedTime = it.text.toString()
            }
        }

        //예약 완료 버튼
        findViewById<Button>(R.id.DoneButton).setOnClickListener {
            val numpeople = findViewById<TextView>(R.id.numpeople).text.toString()
            val user_phone = findViewById<EditText>(R.id.user_phone).text.toString()
            val user_comments = findViewById<EditText>(R.id.Guest_Request).text.toString()

            val reservationDTO = ReservationDTO(
                user_id = user_id,
                user_phone = user_phone,
                user_comments = user_comments,
                reservation_date = selectedDate,
                reservation_time = selectedTime,
                num_people = numpeople.toInt(),
                store_id = store_id
            )

            // 사용자 전화번호와 코멘트가 공백인지 검사합니다.
            if (user_phone.isEmpty() || user_comments.isEmpty()) {
                // 사용자에게 필수 정보 입력을 요청하는 Toast 메시지를 표시합니다.
                Toast.makeText(this@RestaurantReservation, "전화번호와 요청사항을 입력해주세요.", Toast.LENGTH_LONG).show()
            } else if (selectedTime.isEmpty()) {
                // 시간 선택 여부를 검사하고, 선택하지 않았다면 안내 메시지를 표시합니다.
                Toast.makeText(this@RestaurantReservation, "시간을 선택해주세요.", Toast.LENGTH_LONG).show()
            }
            else {
                service.createReservation(reservationDTO).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            // 예약 성공 처리
                            response.body()?.let { responseBody ->
                                val responseString = responseBody.string() // 응답을 문자열로 변환
                                if (responseString.contains("예약 주문 성공")) {
                                    // 웹소켓 해제
                                    val clearWebSocket = WebSocketManager.getWebSocket()
                                    clearWebSocket?.let {
                                        WebSocketManager.disconnectWebSocket()
                                    }
                                    Toast.makeText(this@RestaurantReservation, "성공: $responseString", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@RestaurantReservation, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // 예상치 못한 성공 메시지 처리
                                    Toast.makeText(this@RestaurantReservation, "응답: $responseString", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            // 예약 실패 처리
                            val errorMessage = response.errorBody()?.string()
                            Log.e("Reservation", "예약 실패: $errorMessage")
                            Log.e("-------------reservationDate", "${selectedDate}")
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
    private fun highlightSelectedButton(selectedButton: Button) {
        // 선택된 버튼의 배경색을 변경
        selectedButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.foodfix_dark_green)
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
}