package com.example.foodfix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.RestaurantDetailBinding


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

        val itemList = mutableListOf<CardModel>()
        itemList.add(CardModel("메뉴1", "10000", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("메뉴2", "20000", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("메뉴3", "30000", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("메뉴4", "40000", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("메뉴5", "50000", R.drawable.ic_launcher_foreground))

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
                val intent = Intent(this@RestaurantActivity, MenuActivity::class.java)
                intent.putExtra("menu_name", clickedItem.title)
                intent.putExtra("menu_price", clickedItem.detail)
                resultLauncher.launch(intent)
            }
        })



        val restaurantName = intent.getStringExtra("title")
        findViewById<TextView>(R.id.restaurant_title).text = restaurantName

        findViewById<Button>(R.id.restaurant_detailBackButton).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.heart).setOnClickListener {

        }

        findViewById<TextView>(R.id.go2review).setOnClickListener {
            openReviewActivity()
        }

        findViewById<Button>(R.id.reservation_button).setOnClickListener {

        }

        findViewById<TextView>(R.id.restautant_inf).setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("식당 정보")
            builder.setMessage("연락처, 운영 시간, 공지 사항")

            // "확인" 버튼
            builder.setPositiveButton("확인") { dialog, which ->
                // "확인" 버튼 클릭 시 실행할 코드
                dialog.dismiss() // 다이얼로그 닫기
            }
            // 다이얼로그를 표시합니다.
            builder.show()
        }

        findViewById<Button>(R.id.includedMenu).setOnClickListener {

        }

    }
    private fun openReviewActivity() {
        val intent = Intent(this, RestaurantReviews::class.java)
        reviewActivityResultLauncher.launch(intent)
    }
}