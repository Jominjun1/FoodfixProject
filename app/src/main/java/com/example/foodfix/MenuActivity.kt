package com.example.foodfix

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_detail)

        supportActionBar?.hide()

        findViewById<Button>(R.id.menu_detailBackButton).setOnClickListener {
            finish()
        }

        val menuPrice = intent.getStringExtra("menu_price")?.toIntOrNull() ?: 1
        val totalPriceTextView = findViewById<TextView>(R.id.menuPrice)

        findViewById<TextView>(R.id.menuName).text = intent.getStringExtra("menu_name")
        totalPriceTextView.text = "$menuPrice"

        val decreaseButton: ImageView = findViewById(R.id.downbutton)
        val increaseButton: ImageView = findViewById(R.id.upbutton)
        val numMenuTextView: TextView = findViewById(R.id.menuNum)

        // 감소 버튼 클릭 리스너
        decreaseButton.setOnClickListener {
            var numMenu = numMenuTextView.text.toString().toInt()
            if (numMenu > 1) { // 0보다 작아지지 않도록 체크
                numMenu--
                numMenuTextView.text = numMenu.toString()
                updateTotalPrice(numMenu, menuPrice, totalPriceTextView)
            }

        }

        // 증가 버튼 클릭 리스너
        increaseButton.setOnClickListener {
            var numMenu = numMenuTextView.text.toString().toInt()
            numMenu++
            numMenuTextView.text = numMenu.toString()
            updateTotalPrice(numMenu, menuPrice, totalPriceTextView)
        }

    }


    // 가격 업데이트를 위한 함수
    private fun updateTotalPrice(numMenu: Int, menuPrice: Int, totalPriceTextView: TextView) {
        val totalPrice = numMenu * menuPrice
        totalPriceTextView.text = "$totalPrice"
    }
}