package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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

class MainActivity : BaseActivity() {

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
                override fun deserialize(
                    json: JsonElement,
                    typeOfT: Type,
                    context: JsonDeserializationContext
                ): LocalTime {
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

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 특정 키의 값을 null로 설정하여 SharedPreferences에서 해당 데이터 비우기
        editor.putString("menuList", null)
        editor.apply()

        val itemList = mutableListOf<StoreDTO>()
        val adapter = StoreAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val koreanLayout = findViewById<LinearLayout>(R.id.korean)
        val chickenLayout = findViewById<LinearLayout>(R.id.chicken)
        val westernLayout = findViewById<LinearLayout>(R.id.western)
        val chinaLayout = findViewById<LinearLayout>(R.id.china)
        val japanLayout = findViewById<LinearLayout>(R.id.japan)
        val snackLayout = findViewById<LinearLayout>(R.id.snack)

        val koreanText = findViewById<TextView>(R.id.text_korean)
        val chickenText = findViewById<TextView>(R.id.text_chicken)
        val westernText = findViewById<TextView>(R.id.text_western)
        val chinaText = findViewById<TextView>(R.id.text_china)
        val japanText = findViewById<TextView>(R.id.text_japan)
        val snackText = findViewById<TextView>(R.id.text_snack)

        val layouts = listOf(koreanLayout, chickenLayout, westernLayout, chinaLayout, japanLayout, snackLayout)
        val texts = listOf(koreanText, chickenText, westernText, chinaText, japanText, snackText)

        var Storecategory = ""

        layouts.zip(texts).forEach { (layout, text) ->
            layout.setOnClickListener {
                selectLayout(layout, layouts)
                Storecategory = text.text.toString()
            }
        }

        findViewById<LinearLayout>(R.id.orderButton).setOnClickListener {

            val category = Storecategory
            val storeName: String? = null // 사용자가 입력한 매장 이름
            val menuName: String? = null // 사용자가 입력한 메뉴 이름

            storeService.searchPackableStores(category, storeName, menuName).enqueue(object :
                Callback<List<StoreDTO>> {
                override fun onResponse(
                    call: Call<List<StoreDTO>>,
                    response: Response<List<StoreDTO>>
                ) {
                    if (response.isSuccessful) {
                        val packableStores = response.body() ?: emptyList()
                        Log.d("MainActivity", "Packable Stores: $packableStores")
                        val cardItems = packableStores.map { dto ->

                            //Log.d("------------StoreInf", "${dto}")
                            // 서버로부터 받은 정보를 StoreDTO로 변환합니다.
                            StoreDTO(
                                store_id = dto.store_id,
                                imagePath = dto.imagePath,
                                store_name = dto.store_name,
                                store_intro = dto.store_intro,
                                store_phone = dto.store_phone,
                                store_address = dto.store_address,
                                store_category = dto.store_category,
                                minimumTime = dto.minimumTime,
                                openTime = dto.openTime,
                                closeTime = dto.closeTime,
                                res_max = ""
                            )
                        }
                        // RecyclerView 어댑터에 데이터 설정
                        itemList.clear()
                        itemList.addAll(cardItems)
                        adapter.notifyDataSetChanged()

                        // 각 카드뷰 항목에 대한 클릭 이벤트를 처리하는 로직을 재설정
                        adapter.setOnItemClickListener(object :
                            StoreAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                // 클릭한 아이템의 정보를 로그로 출력하고, 필요한 액션을 수행합니다.
                                val clickedItem = itemList[position]
                                Log.d(
                                    "MainActivity",
                                    "Clicked address: ${clickedItem.store_address}"
                                )
                                // 예를 들어, 상세 정보 화면으로 이동하는 인텐트를 발생시킵니다.
                                val intent = Intent(
                                    this@MainActivity,
                                    RestaurantActivity::class.java
                                ).apply {
                                    putExtra("store_id", clickedItem.store_id)
                                    putExtra("store_name", clickedItem.store_name)
                                    putExtra("store_address", clickedItem.store_address)
                                    putExtra("store_intro", clickedItem.store_intro)
                                    putExtra("store_phone", clickedItem.store_phone)
                                    putExtra("openTime", clickedItem.openTime)
                                    putExtra("closeTime", clickedItem.closeTime)
                                    putExtra("imagePath", clickedItem.imagePath)
                                    putExtra("minimumTime", clickedItem.minimumTime.toString())
                                }
                                editor.putString("store_image", clickedItem.imagePath).apply()
                                resultLauncher.launch(intent)
                            }
                        })
                    } else {
                        Log.d("MainActivity", "Failed to fetch data: ${response.code()} ${response.message()}")
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to fetch data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<StoreDTO>>, t: Throwable) {
                    Log.e("MainActivity", "네트워크 요청 실패: ${t.message}")
                    Toast.makeText(
                        this@MainActivity,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

        findViewById<LinearLayout>(R.id.reservationButton).setOnClickListener {

            val category = Storecategory
            val storeName: String? = null // 사용자가 입력한 매장 이름
            val menuName: String? = null // 사용자가 입력한 메뉴 이름

            storeService.searchReservableStores(category, storeName, menuName)
                .enqueue(object : Callback<List<StoreDTO>> {
                    override fun onResponse(
                        call: Call<List<StoreDTO>>,
                        response: Response<List<StoreDTO>>
                    ) {
                        if (response.isSuccessful) {
                            val reservableStores = response.body() ?: emptyList()
                            val cardItems = reservableStores.map { dto ->

                                Log.d("StoreImagePath", "${dto.imagePath}")
                                // 서버로부터 받은 정보를 StoreDTO 변환합니다.
                                StoreDTO(
                                    store_id = dto.store_id,
                                    imagePath = dto.imagePath,
                                    store_name = dto.store_name,
                                    store_phone = dto.store_phone,
                                    store_address = dto.store_address,
                                    store_intro = dto.store_intro,
                                    res_max = dto.res_max,
                                    store_category = dto.store_category,
                                    minimumTime = dto.minimumTime,
                                    openTime = dto.openTime,
                                    closeTime = dto.closeTime,
                                )
                            }
                            // RecyclerView 어댑터에 데이터 설정
                            itemList.clear()
                            itemList.addAll(cardItems)
                            adapter.notifyDataSetChanged()

                            // 각 카드뷰 항목에 대한 클릭 이벤트를 처리하는 로직을 재설정
                            adapter.setOnItemClickListener(object :
                                StoreAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    // 클릭한 아이템의 정보를 로그로 출력하고, 필요한 액션을 수행합니다.
                                    val clickedItem = itemList[position]
                                    Log.d(
                                        "MainActivity",
                                        "Clicked store_id: ${clickedItem.store_id}"
                                    )
                                    // 예를 들어, 상세 정보 화면으로 이동하는 인텐트를 발생시킵니다.
                                    val intent = Intent(
                                        this@MainActivity,
                                        RestaurantReservation::class.java
                                    ).apply {
                                        putExtra("store_id", clickedItem.store_id)
                                        putExtra("store_name", clickedItem.store_name)
                                        putExtra("store_address", clickedItem.store_address)
                                        putExtra("store_intro", clickedItem.store_intro)
                                        putExtra("store_phone", clickedItem.store_phone)
                                        putExtra("openTime", clickedItem.openTime)
                                        putExtra("closeTime", clickedItem.closeTime)
                                        putExtra("res_max", clickedItem.res_max.toString())
                                    }

                                    resultLauncher.launch(intent)
                                }
                            })
                        } else {
                            Log.d("MainActivity", "Failed to fetch data: ${response.code()} ${response.message()}")
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to fetch data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<StoreDTO>>, t: Throwable) {
                        Log.e("MainActivity", "네트워크 요청 실패: ${t.message}")
                        Toast.makeText(
                            this@MainActivity,
                            "Network Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

        // 주문 현황 이동
        findViewById<LinearLayout>(R.id.statusbutton).setOnClickListener {

            val builder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.status_dialog_layout, null)

            val packingstsusButton = dialogLayout.findViewById<Button>(R.id.packingstsusButton)
            val reservationstatusButton = dialogLayout.findViewById<Button>(R.id.reservationstatusButton)
            val dialogButton = dialogLayout.findViewById<Button>(R.id.dialogButton)

            builder.setView(dialogLayout)
            val dialog = builder.create()

            packingstsusButton.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, PackingstatusActivity::class.java)
                resultLauncher.launch(intent)
            }

            reservationstatusButton.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, ReservationstatusActivity::class.java)
                resultLauncher.launch(intent)
            }

            dialogButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        findViewById<LinearLayout>(R.id.Favoritesbutton).setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            finish()
        }

        //마이페이지로 이동
        findViewById<LinearLayout>(R.id.mypageButton).setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun selectLayout(selectedLayout: LinearLayout, layouts: List<LinearLayout>) {
        layouts.forEach {
            it.background = if (it == selectedLayout) {
                ContextCompat.getDrawable(this, R.drawable.select_button)
            } else {
                ContextCompat.getDrawable(this, R.drawable.cardview)
            }
        }
    }
}