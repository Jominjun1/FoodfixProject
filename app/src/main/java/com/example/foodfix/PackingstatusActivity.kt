package com.example.foodfix


import MyWebSocketListener
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
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class PackingstatusActivity : BaseActivity() {

    lateinit var binding: ShowlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)
        binding = DataBindingUtil.setContentView(this, R.layout.showlist)

        supportActionBar?.hide()

        findViewById<TextView>(R.id.showlisttext).text = "포장 주문 내역"

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

        val itemList = mutableListOf<PackingCardModel>()

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("jwt_token", null) ?: ""

        val adapter = PackingCardAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : PackingCardAdapter.OnItemClickListener {
            override fun onCancelClick(position: Int) {
                // 여기에서 취소 버튼이 클릭된 아이템의 처리 로직을 구현하세요.
                Toast.makeText(this@PackingstatusActivity, "Cancel clicked at position $position", Toast.LENGTH_SHORT).show()
                Log.d("PackingstatusActivity", "$position")
            }
        })

        userService.getUserPacking("Bearer $token").enqueue(object : Callback<List<PackingCardModel>> {
            override fun onResponse(call: Call<List<PackingCardModel>>, response: Response<List<PackingCardModel>>) {
                if (response.isSuccessful) {
                    val packing = response.body() ?: emptyList()
                    Log.d("---------------PackingState: ", "$packing")
                    val cardItems = packing.map { dto ->

                        var total = 0.0 // 총합을 저장할 변수 초기화

                        dto.menuItemDTOList?.forEach { item ->
                            val price = item.menu_price.toDoubleOrNull() ?: 0.0 // total_price를 Double로 안전하게 변환
                            val quantity = item.quantity.toDoubleOrNull() ?: 0.0 // quantity를 Double로 안전하게 변환
                            total += price * quantity // 각 항목의 가격과 수량을 곱하여 total에 누적
                        }

                        PackingCardModel(
                            packing_id = dto.packing_id,
                            store_id = dto.store_id,
                            store_name = dto.store_name,
                            total_price = total,
                            packing_date = dto.packing_date,
                            packing_time = dto.packing_time,
                            minimumTime = dto.minimumTime,
                            user_comments = dto.user_comments,
                            payment_type = when (dto.payment_type) {
                                "0" -> "앱결제"
                                "1" -> "방문 결제"
                                else -> "오류" },
                            packing_status = when (dto.packing_status) {
                                "0" -> "주문중"
                                "1" -> "주문 접수"
                                "2" -> "주문 완료"
                                "3" -> "주문 취소"
                                else -> "오류"
                            },
                            menuItemDTOList = dto.menuItemDTOList
                        )
                    }
                    // RecyclerView 어댑터에 데이터 설정
                    itemList.clear()
                    itemList.addAll(cardItems)
                    itemList.reverse()
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@PackingstatusActivity, "Failed to fetch packings", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<PackingCardModel>>, t: Throwable) {
                Log.e("PackingstatusActivity", "Network Error: ${t.localizedMessage}")
                Toast.makeText(this@PackingstatusActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        findViewById<Button>(R.id.showlistBackButton).setOnClickListener {
            val intent = Intent(this@PackingstatusActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
