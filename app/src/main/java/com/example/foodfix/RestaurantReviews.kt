package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RestaurantReviews : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_reviews)

        supportActionBar?.hide()

        findViewById<Button>(R.id.rest_reviewBackButton).setOnClickListener {

            finish()
        }
    }
}