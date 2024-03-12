package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.RestaurantReviewsBinding

class RestaurantReviews : AppCompatActivity() {

    lateinit var binding:RestaurantReviewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_reviews)
        binding = DataBindingUtil.setContentView(this, R.layout.restaurant_reviews)

        supportActionBar?.hide()

        val itemList = mutableListOf<ReviewCardModel>()
        itemList.add(ReviewCardModel("testname", "testDetail", 3.0f, "testdate", R.drawable.ic_launcher_foreground))

        val adapter = ReviwCardAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.rest_reviewBackButton).setOnClickListener {
            finish()
        }
    }
}