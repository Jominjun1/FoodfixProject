package com.example.foodfix

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalTime


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // 여기서는 별도의 결과 처리가 필요 없습니다.
        }

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalTime::class.java, object : JsonDeserializer<LocalTime> {
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime {
                    return LocalTime.parse(json.asString)
                }
            })
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val storeService = retrofit.create(StoreService::class.java)

        supportActionBar?.hide()

        // 사용자의 JWT 토큰을 가져옴
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        val itemList = mutableListOf<CardModel>()
        itemList.add(CardModel("솔솥밥", "4.2", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("사생활", "4.2", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("행운동 조개", "4.2", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("부촌 육회", "4.2", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("시금치 통닭", "4.2", R.drawable.ic_launcher_foreground))


        val adapter = CardviewAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // 각 카드 뷰의 클릭 이벤트 처리
        adapter.setOnItemClickListener(object : CardviewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 클릭한 아이템의 정보를 로그로 출력하여 확인
                val clickedItem = itemList[position]
                Log.d("MainActivity", "Clicked item: ${clickedItem.title}")

                // 클릭한 아이템에 대한 처리 작업을 여기에 추가
                // 예를 들어, 다른 화면으로 이동하거나 데이터를 전달할 수 있습니다.
                val intent = Intent(this@MainActivity, RestaurantActivity::class.java)
                intent.putExtra("title", clickedItem.title)
                resultLauncher.launch(intent)
            }
        })

        findViewById<Button>(R.id.korean).setOnClickListener {
            findViewById<TextView>(R.id.storeCate).text = "한식"
        }

        findViewById<Button>(R.id.chicken).setOnClickListener {
            findViewById<TextView>(R.id.storeCate).text = "치킨"
        }

        findViewById<Button>(R.id.reservationButton).setOnClickListener {
            findViewById<TextView>(R.id.Filter).text = "예약"
        }

        findViewById<Button>(R.id.orderButton).setOnClickListener {
            findViewById<TextView>(R.id.Filter).text = "포장"
        }

        findViewById<Button>(R.id.searchButton).setOnClickListener {
            val category = findViewById<TextView>(R.id.storeCate).text.toString()
            val filter = findViewById<TextView>(R.id.Filter).text.toString()
            val storeName: String? = null // 사용자가 입력한 매장 이름
            val menuName: String? = null // 사용자가 입력한 메뉴 이름


            /*val category = "한식"
            val storeName: String? = null // 사용자가 입력한 매장 이름
            val menuName: String? = null // 사용자가 입력한 메뉴 이름*/

            if(filter == "예약"){
                storeService.searchReservableStores(category, storeName, menuName).enqueue(object : Callback<List<StoreDTO>> {
                    override fun onResponse(call: Call<List<StoreDTO>>, response: Response<List<StoreDTO>>) {
                        if (response.isSuccessful) {
                            val reservableStores = response.body() ?: emptyList()
                            val cardItems = reservableStores.map { dto ->
                                //식당 아이디 저장
                                val store_id = dto.store_id
                                with (sharedPref.edit()) {
                                    putLong("store_id", store_id)
                                    apply()
                                }
                                // 서버로부터 받은 정보를 CardModel로 변환합니다.
                                CardModel(
                                    title = dto.store_name ?: "기본 매장 이름",
                                    detail = "Open: ${dto.openTime?.toString()} - Close: ${dto.closeTime?.toString()}",
                                    image = R.drawable.ic_launcher_foreground // 이미지 URL 또는 리소스 ID
                                )
                            }
                            // RecyclerView 어댑터에 데이터 설정
                            itemList.clear()
                            itemList.addAll(cardItems)
                            adapter.notifyDataSetChanged()

                            // 각 카드뷰 항목에 대한 클릭 이벤트를 처리하는 로직을 재설정
                            adapter.setOnItemClickListener(object : CardviewAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    // 클릭한 아이템의 정보를 로그로 출력하고, 필요한 액션을 수행합니다.
                                    val clickedItem = itemList[position]
                                    Log.d("MainActivity", "Clicked item: ${clickedItem.title}")
                                    // 예를 들어, 상세 정보 화면으로 이동하는 인텐트를 발생시킵니다.
                                    val intent = Intent(this@MainActivity, RestuarantReservation::class.java).apply {
                                        putExtra("title", clickedItem.title)
                                        val storeId = sharedPref.getLong("store_id", -1L) // 기본값으로 -1을 사용
                                        intent.putExtra("store_id", storeId.toString()) // Long을 String으로 변환하여 putExtra 사용
                                    }
                                    startActivity(intent)
                                }
                            })
                        } else {
                            Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<StoreDTO>>, t: Throwable) {
                        Log.e("MainActivity", "네트워크 요청 실패: ${t.message}")
                        Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
            else if(filter == "포장"){
                storeService.searchPackableStores(category, storeName, menuName).enqueue(object :
                    Callback<List<StoreDTO>> {
                    override fun onResponse(call: Call<List<StoreDTO>>, response: Response<List<StoreDTO>>) {
                        if (response.isSuccessful) {
                            val packableStores = response.body() ?: emptyList()
                            val cardItems = packableStores.map { dto ->
                                //식당 아이디 저장
                                val store_id = dto.store_id
                                with (sharedPref.edit()) {
                                    putLong("store_id", store_id)
                                    apply()
                                }
                                // 서버로부터 받은 정보를 CardModel로 변환합니다.
                                CardModel(
                                    title = dto.store_name ?: "기본 매장 이름",
                                    detail = "Open: ${dto.openTime?.toString()} - Close: ${dto.closeTime?.toString()}",
                                    image = R.drawable.ic_launcher_foreground // 이미지 URL 또는 리소스 ID
                                )
                            }
                            // RecyclerView 어댑터에 데이터 설정
                            itemList.clear()
                            itemList.addAll(cardItems)
                            adapter.notifyDataSetChanged()

                            // 각 카드뷰 항목에 대한 클릭 이벤트를 처리하는 로직을 재설정
                            adapter.setOnItemClickListener(object : CardviewAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    // 클릭한 아이템의 정보를 로그로 출력하고, 필요한 액션을 수행합니다.
                                    val clickedItem = itemList[position]
                                    Log.d("MainActivity", "Clicked item: ${clickedItem.title}")
                                    // 예를 들어, 상세 정보 화면으로 이동하는 인텐트를 발생시킵니다.
                                    val intent = Intent(this@MainActivity, RestaurantActivity::class.java).apply {
                                        putExtra("title", clickedItem.title)
                                        val storeId = sharedPref.getLong("store_id", -1L) // 기본값으로 -1을 사용
                                        intent.putExtra("store_id", storeId.toString()) // Long을 String으로 변환하여 putExtra 사용
                                    }
                                    startActivity(intent)
                                }
                            })
                        } else {
                            Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<StoreDTO>>, t: Throwable) {
                        Log.e("MainActivity", "네트워크 요청 실패: ${t.message}")
                        Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        findViewById<Button>(R.id.mapbutton).setOnClickListener {

        }

        findViewById<Button>(R.id.Orderdetailsbutton).setOnClickListener {
            val intent = Intent(this, OrderDetailsActivity::class.java)
            resultLauncher.launch(intent)
        }

        findViewById<Button>(R.id.Favoritesbutton).setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            finish()
        }

        //마이페이지로 이동
        findViewById<TextView>(R.id.MyInf).setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}