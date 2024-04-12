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
import com.example.foodfix.databinding.TakeoutMenuBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TakeoutActivity : AppCompatActivity(){

    lateinit var binding: TakeoutMenuBinding
    private val itemList = mutableListOf<MenuItemDTO>() // 클래스 멤버로 변경

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takeout_menu)
        binding = DataBindingUtil.setContentView(this, R.layout.takeout_menu)

        supportActionBar?.hide()

        // 사용자의 JWT 토큰을 가져옴
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = GsonBuilder().create()
        val user_id = sharedPref.getString("user_id", null)

        // 식당 정보
        val store_id = intent.getLongExtra("store_id", 0L)
        //val store_name = intent.getStringExtra("store_name")


        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080") // 서버 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(StoreService::class.java)

        val adapter = TakeoutmenuAdapter(itemList) {
            updateTotalPrice() // 어댑터에서 호출할 콜백
        }
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)


        val jsonList =sharedPref.getString("menuList","none") // json list 가져오기

        // 저장된 데이터가 비어있지 않으면
        if (!jsonList.isNullOrEmpty()) {
            val menuListType: Type = object : TypeToken<ArrayList<MenuDTO>>() {}.type
            val menuList = gson.fromJson<ArrayList<MenuDTO>>(jsonList, menuListType)

            val itemmenuList = mutableListOf<MenuItemDTO>()

            // menuList에 저장된 데이터를 MenuItemDTO로 변환하여 itemList에 추가하기
            for (menu in menuList) {
                if (menu.menu_id != null && menu.menu_id.isNotEmpty()) { // menu_id가 null이 아니고 비어 있지 않은 경우에만 처리
                    val initialPrice = menu.menu_price.toDouble() / menu.quantity.toInt()
                    itemList.add(MenuItemDTO(menu.menu_id.toLong(), menu.menu_price.toDouble(), menu.menu_name, menu.quantity.toInt(), initialPrice))
                } else {
                    Log.e("TakeoutActivity", "Invalid menu ID: ${menu.menu_id}")
                }
            }

            // itemList에 저장된 데이터를 확인하기 위해 로그 출력
            for (item in itemmenuList) {
                Log.d("TakeoutActivity", "Menu ID: ${item.menu_id}, Name: ${item.menu_name}, Price: ${item.menu_price}, Quantity: ${item.quantity}, Initial Price: ${item.initialPrice}")
            }
        } else {
            // 저장된 데이터가 없을 때의 처리
            Log.d("TakeoutActivity", "No data found in SharedPreferences.")
        }

        val total_price = itemList.sumOf { it.menu_price }
        findViewById<TextView>(R.id.totalPrice).text = total_price.toString()

        adapter.setOnItemClickListener(object : TakeoutmenuAdapter.OnItemClickListener {
            override fun onCancelClick(position: Int) {
                // 아이템 삭제
                itemList.removeAt(position)
                adapter.notifyItemRemoved(position)
                updateTotalPrice()
            }

            override fun onDownClick(position: Int) {
                adapter.notifyItemChanged(position)
                updateTotalPrice()
            }

            override fun onUpClick(position: Int) {
                adapter.notifyItemChanged(position)
                updateTotalPrice()
            }
        })

        
        val takemenuList = mutableListOf<MenuDTO>()
        findViewById<Button>(R.id.takeoutButton).setOnClickListener {
            val user_phone = findViewById<TextView>(R.id.user_phone).text
            val user_comments = findViewById<TextView>(R.id.user_commend).text

            for (item in itemList) {
                val newMenu = MenuDTO(
                    menu_id = item.menu_id.toString(),
                    menu_price = item.menu_price.toString(),
                    menu_name = item.menu_name,
                    quantity = item.quantity.toString()
                )
                takemenuList.add(newMenu)
            }

            val currentDate = LocalDate.now()
            val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))

            val currentTime = LocalTime.now()
            val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            val packingOrder = PackingOrder(
                user_id = user_id.toString(),
                user_phone = user_phone.toString(),
                user_comments = user_comments.toString(),
                packing_date = formattedDate,
                packing_time = formattedTime,
                payment_type = "현장 결제",
                store_id = store_id.toString(),
                menuDTOList = takemenuList
            )

            // 사용자 전화번호와 코멘트가 공백인지 검사합니다.
            if (user_phone.isEmpty() || user_comments.isEmpty()) {
                // 사용자에게 필수 정보 입력을 요청하는 Toast 메시지를 표시합니다.
                Toast.makeText(this@TakeoutActivity, "전화번호와 요청사항을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else {
                service.createPackingOrder(packingOrder).enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                        if (response.isSuccessful){
                            // 포장 성공 처리
                            response.body()?.let { responseBody ->
                                val responseString = responseBody.string() // 응답을 문자열로 변환
                                if (responseString.contains("매장 포장 주문 성공")) {
                                    Toast.makeText(this@TakeoutActivity, "성공: $responseString", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@TakeoutActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // 예상치 못한 성공 메시지 처리
                                    Toast.makeText(this@TakeoutActivity, "응답: $responseString", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        else {
                            // 예약 실패 처리
                            val errorMessage = response.errorBody()?.string()
                            Log.e("Packing", "포장 실패: $errorMessage")
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 네트워크 오류 처리
                        Log.e("Packing", "네트워크 오류: ${t.message}")
                    }
                })
            }
        }

        findViewById<Button>(R.id.take_outBackButton).setOnClickListener {
            /*val intent = Intent(this@TakeoutActivity, RestaurantActivity::class.java)
            startActivity(intent)*/
            finish()
        }
    }

    private fun updateTotalPrice() {
        // 총 가격 업데이트
        val total_price = itemList.sumOf { it.menu_price }
        binding.totalPrice.text = String.format("%.2f", total_price)
    }
}

