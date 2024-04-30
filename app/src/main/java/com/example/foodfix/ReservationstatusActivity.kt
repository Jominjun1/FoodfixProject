package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class ReservationstatusActivity : AppCompatActivity() {

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

        // 웹소켓 연결
        // OkHttpClient 생성
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()

        // 웹소켓 요청 생성
        val request = Request.Builder()
            .url("ws://54.180.213.178:8080/wsk")
            .build()

        val listener = MyWebSocketListener()
        val webSocket = client.newWebSocket(request, listener)
        // WebSocketManager에 웹소켓 설정
        WebSocketManager.setWebSocket(webSocket)

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
                    val cardItems = reservation.map { dto ->
                        ReservationCardModel(
                            reservation_id = dto.reservation_id,
                            store_id = dto.store_id,
                            reservation_date = dto.reservation_date,
                            reservation_time = dto.reservation_time,
                            num_people = dto.num_people,
                            user_comments = dto.user_comments,
                            reservation_status = when (dto.reservation_status) {
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

        findViewById<Button>(R.id.showlistBackButton).setOnClickListener {
            //웹소켓 해제
            val clearWebSocket = WebSocketManager.getWebSocket()
            clearWebSocket?.let {
                WebSocketManager.disconnectWebSocket()
            }
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}