package com.example.foodfix

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestaurantReservation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_reservation)

        supportActionBar?.hide()

        // 사용자의 JWT 토큰을 가져옴
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        // 식당 정보
        val store_id = intent.getLongExtra("store_id", 0L)
        val store_name = intent.getStringExtra("store_name")

        findViewById<TextView>(R.id.restaurantName).text = store_name

        findViewById<Button>(R.id.restaurnatReservationBackButton).setOnClickListener {
            finish()
        }

        // 뷰 참조 가져오기
        val decreaseButton: Button = findViewById(R.id.decreaseButton)
        val increaseButton: Button = findViewById(R.id.increaseButton)
        val numPeopleTextView: TextView = findViewById(R.id.numpeople)

        // 감소 버튼 클릭 리스너
        decreaseButton.setOnClickListener {
            var numPeople = numPeopleTextView.text.toString().toInt()
            if (numPeople > 1) { // 0보다 작아지지 않도록 체크
                numPeople--
                numPeopleTextView.text = numPeople.toString()
            }
        }

        // 증가 버튼 클릭 리스너
        increaseButton.setOnClickListener {
            var numPeople = numPeopleTextView.text.toString().toInt()
            numPeople++
            numPeopleTextView.text = numPeople.toString()
        }
    }
}