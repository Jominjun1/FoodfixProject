package com.example.foodfix

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestuarantReservation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_reservation)

        supportActionBar?.hide()

        // 사용자의 JWT 토큰을 가져옴
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val store_id = sharedPref.getString("store_id", null) ?: ""
        val store_name = sharedPref.getString("title", null) ?: ""

        findViewById<TextView>(R.id.restaurantName).text = store_name
    }
}