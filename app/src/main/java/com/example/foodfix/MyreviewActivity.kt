package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ShowlistBinding

class MyreviewActivity : BaseActivity() {

    lateinit var binding:ShowlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)
        binding = DataBindingUtil.setContentView(this, R.layout.showlist)

        supportActionBar?.hide()

        val itemList = mutableListOf<ReviewCardModel>()
        itemList.add(ReviewCardModel("testname", "testDetail", 3.0f, "testdate", R.drawable.ic_launcher_foreground))
        itemList.add(ReviewCardModel("testname2","testDetail2", 4.0f, "test2date", R.drawable.ic_launcher_foreground))


        val adapter = ReviwCardAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        findViewById<TextView>(R.id.showlisttext).text = "내가 작성한 리뷰"

        findViewById<ImageView>(R.id.showlistBack).setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}