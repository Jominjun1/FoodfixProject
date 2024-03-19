package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ShowlistBinding

class ReservationstatusActivity : AppCompatActivity() {

    lateinit var binding:ShowlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)
        binding = DataBindingUtil.setContentView(this, R.layout.showlist)

        supportActionBar?.hide()

        val itemList = mutableListOf<ReservationCardModel>()
        itemList.add(ReservationCardModel("맛있는 식당", "2024-10-10", "20:30",4,"아이가 한명 있어요"))

        val adapter = ReservationCardAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        findViewById<TextView>(R.id.showlisttext).text = "식당 예약 현황"

        findViewById<Button>(R.id.showlistBackButton).setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}