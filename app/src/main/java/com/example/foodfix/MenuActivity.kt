package com.example.foodfix

import android.os.Bundle
import android.widget.Button
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

        findViewById<TextView>(R.id.menuName).text = intent.getStringExtra("menu_name")
        findViewById<TextView>(R.id.menuPrice).text = intent.getStringExtra("menu_price")
    }
}