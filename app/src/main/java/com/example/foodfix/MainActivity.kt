package com.example.foodfix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)

        supportActionBar?.hide()

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
                intent.putExtra("restaurant_name", clickedItem.title)
                resultLauncher.launch(intent)
            }
        })


        findViewById<Button>(R.id.reservationButton).setOnClickListener {

        }

        findViewById<Button>(R.id.orderButton).setOnClickListener {

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