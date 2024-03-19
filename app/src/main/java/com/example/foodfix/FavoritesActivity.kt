package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ShowlistBinding

class FavoritesActivity : AppCompatActivity() {

    lateinit var binding:ShowlistBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)
        binding = DataBindingUtil.setContentView(this, R.layout.showlist)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // 여기서는 별도의 결과 처리가 필요 없습니다.
        }

        supportActionBar?.hide()

        val itemList = mutableListOf<CardModel>()
        itemList.add(CardModel("솔솥밥", "4.2", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("사생활", "4.2", R.drawable.ic_launcher_foreground))
        itemList.add(CardModel("행운동 조개", "4.2", R.drawable.ic_launcher_foreground))

        val adapter = CardviewAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // 각 카드 뷰의 클릭 이벤트 처리
        adapter.setOnItemClickListener(object : CardviewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 클릭한 아이템의 정보를 로그로 출력하여 확인
                val clickedItem = itemList[position]
                Log.d("FavoritesActivity", "Clicked item: ${clickedItem.title}")

                // 클릭한 아이템에 대한 처리 작업을 여기에 추가
                // 예를 들어, 다른 화면으로 이동하거나 데이터를 전달할 수 있습니다.
                val intent = Intent(this@FavoritesActivity, RestaurantActivity::class.java)
                intent.putExtra("restaurant_name", clickedItem.title)
                resultLauncher.launch(intent)
            }
        })

        findViewById<TextView>(R.id.showlisttext).text = "즐겨 찾기"

        findViewById<Button>(R.id.showlistBackButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}