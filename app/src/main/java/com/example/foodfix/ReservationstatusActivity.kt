package com.example.foodfix


import ReservationCardAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ShowlistBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.appcompat.app.AlertDialog

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
            override fun onCancelClick(reservationId: Long) {
                confirmCancellation(reservationId)
            }

            override fun onItemClick(reservationCardModel: ReservationCardModel) {
                // 처리 로직, 예: AlertDialog 띄우기
                showDetailsDialog(reservationCardModel)
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
                            store_name = dto.store_name ?: "오류",
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
    private fun showDetailsDialog(selectedItem: ReservationCardModel) {
        val fullMessage = "식당 ID: ${selectedItem.store_id}\n예약 ID: ${selectedItem.reservation_id}"
        val alertDialog = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        alertDialog.setTitle("메뉴 정보")
            .setMessage(fullMessage)
            .setPositiveButton("닫기") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    private fun confirmCancellation(reservationId: Long) {
        val alertDialog = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        alertDialog.setTitle("예약 취소 확인")
            .setMessage("이 예약을 취소하시겠습니까?")
            .setPositiveButton("예") { dialog, _ ->
                cancelReservation(reservationId)
                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun cancelReservation(reservationId: Long) {
        // 취소 요청 로직 구현, 예: Retrofit을 사용하여 서버에 요청
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080") // 서버 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(UserService::class.java)
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("jwt_token", null) ?: ""

        userService.cancelReservationOrder("Bearer $token", reservationId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "예약 주문 취소 완료", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("------------Reservation", "응답 실패: ${response.code()} ${response.message()}")
                    Toast.makeText(applicationContext, "응답 실패: ${response.code()} ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("------------Reservation", "네트워크 오류 발생: ${t.message}", t)
                Toast.makeText(applicationContext, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}