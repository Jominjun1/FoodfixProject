package com.example.foodfix

import MyWebSocketListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ShowlistBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class ReservationstatusActivity : BaseActivity() {

    lateinit var binding:ShowlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)
        binding = DataBindingUtil.setContentView(this, R.layout.showlist)

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            })
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val userService = retrofit.create(UserService::class.java)

        supportActionBar?.hide()

        val itemList = mutableListOf<ReservationCardModel>()

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("jwt_token", null) ?: ""

        val adapter = ReservationCardAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : ReservationCardAdapter.OnItemClickListener {
            override fun onCancelClick(position: Int) {
                // 여기에서 취소 버튼이 클릭된 아이템의 처리 로직을 구현하세요.
                Toast.makeText(this@ReservationstatusActivity, "Cancel clicked at position $position", Toast.LENGTH_SHORT).show()
            }
        })

        userService.getUserReservations("Bearer $token").enqueue(object : Callback<List<ReservationCardModel>> {
            override fun onResponse(call: Call<List<ReservationCardModel>>, response: Response<List<ReservationCardModel>>) {
                if (response.isSuccessful) {
                    val reservation = response.body() ?: emptyList()
                    Log.d("-------------서버 식당 예약 코드 : ", "$reservation")
                    val cardItems = reservation.map { dto ->
                        ReservationCardModel(
                            reservation_id = dto.reservation_id,
                            store_id = dto.store_id,
                            reservation_date = dto.reservation_date ?: "날짜 없음",
                            reservation_time = dto.reservation_time ?: "시간 없음",
                            num_people = dto.num_people,
                            user_comments = dto.user_comments ?: "요청사항 없음",
                            reservation_status = when (dto.reservation_status ?: "5") {
                                "0" -> "예약 대기"
                                "1" -> "예약 성공"
                                "2" -> "예약 취소"
                                "3" -> "예약 완료"
                                else -> "오류"
                            }
                        )
                    }
                    // RecyclerView 어댑터에 데이터 설정
                    itemList.clear()
                    itemList.addAll(cardItems)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ReservationstatusActivity, "Failed to fetch reservations", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ReservationCardModel>>, t: Throwable) {
                Log.e("ReservationstatusActivity", "Network Error: ${t.localizedMessage}")
                Toast.makeText(this@ReservationstatusActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        findViewById<TextView>(R.id.showlisttext).text = "식당 예약 현황"

        findViewById<ImageView>(R.id.showlistBack).setOnClickListener {
            finish()
        }
    }
}