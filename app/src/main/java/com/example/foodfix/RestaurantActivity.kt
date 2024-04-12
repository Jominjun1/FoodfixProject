package com.example.foodfix

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.RestaurantDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantActivity : AppCompatActivity() {

    // ActivityResultLauncher 초기화
    private lateinit var reviewActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    lateinit var binding:RestaurantDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.restaurant_detail)

        // ActivityResultLauncher 설정
        reviewActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 필요한 경우 결과 처리, 여기서는 별도의 작업을 수행하지 않음
            }
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // 여기서는 별도의 결과 처리가 필요 없습니다.
        }

        supportActionBar?.hide()

        // Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080") // 서버의 기본 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(UserService::class.java)

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        // 식당 정보
        val store_id = intent.getLongExtra("store_id", 1L)
        Log.d("RestaurantActivity", "store_id $store_id")
        val store_name = intent.getStringExtra("store_name")
        val store_intro = intent.getStringExtra("store_intro")

       findViewById<TextView>(R.id.restaurant_name).text = store_name

        val itemList = mutableListOf<MenuModel>()

        val adapter = MenuAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        userService.getMenusByStoreId(store_id).enqueue(object : Callback<List<MenuModel>> {
            override fun onResponse(call: Call<List<MenuModel>>, response: Response<List<MenuModel>>) {
                if (response.isSuccessful) {
                    val menuList = response.body() ?: emptyList()
                    val cardItem = menuList.map { dto ->
                        MenuModel(
                            menu_id = dto.menu_id,
                            menu_name = dto.menu_name,
                            explanation = dto.explanation,
                            menu_image = R.drawable.ic_launcher_foreground.toString(),
                            menu_price = dto.menu_price
                        )
                    }
                    itemList.clear()
                    itemList.addAll(cardItem)
                    adapter.notifyDataSetChanged()
                } else {
                    // 실패 처리
                    Log.d("RestaurantActivity", "Response: ${response.body()}")
                    Toast.makeText(this@RestaurantActivity, "메뉴 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuModel>>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("RestaurantActivity", "Network Error: ${t.localizedMessage}")
                Toast.makeText(this@RestaurantActivity, "네트워크 오류가 발생했습니다: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })

        // 각 카드 뷰의 클릭 이벤트 처리
        adapter.setOnItemClickListener(object : MenuAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 클릭한 아이템의 정보를 로그로 출력하여 확인
                val clickedItem = itemList[position]
                Log.d("MainActivity", "Clicked item: ${clickedItem.menu_name}")

                // 클릭한 아이템에 대한 처리 작업을 여기에 추가
                // 예를 들어, 다른 화면으로 이동하거나 데이터를 전달할 수 있습니다.
                val intent = Intent(this@RestaurantActivity, MenuActivity::class.java)
                intent.putExtra("store_id", store_id)
                intent.putExtra("menu_id", clickedItem.menu_id)
                intent.putExtra("menu_name", clickedItem.menu_name)
                intent.putExtra("menu_price", clickedItem.menu_price)
                intent.putExtra("explanation", clickedItem.explanation)
                resultLauncher.launch(intent)
            }
        })

        findViewById<Button>(R.id.restaurant_detailBackButton).setOnClickListener {
            val intent = Intent(this@RestaurantActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.heart).setOnClickListener {

        }

        findViewById<TextView>(R.id.go2review).setOnClickListener {
            val intent = Intent(this, RestaurantReviews::class.java)
            reviewActivityResultLauncher.launch(intent)
        }

        findViewById<TextView>(R.id.restautant_inf).setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("식당 정보")
            builder.setMessage(store_intro)

            // "확인" 버튼
            builder.setPositiveButton("확인") { dialog, which ->
                // "확인" 버튼 클릭 시 실행할 코드
                dialog.dismiss() // 다이얼로그 닫기
            }
            // 다이얼로그를 표시합니다.
            builder.show()
        }

        findViewById<Button>(R.id.includedMenu).setOnClickListener {
            val prev = sharedPref.getString("menuList", "none") // 이전에 저장된 데이터 가져오기

            if (prev == "none") {
                // 이전 데이터가 없을 때의 처리
                Toast.makeText(this, "이전 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, TakeoutActivity::class.java)
                intent.putExtra("store_id", store_id)
                reviewActivityResultLauncher.launch(intent)
            }
        }
    }
}