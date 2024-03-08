package com.example.foodfix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReservationstatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)

        supportActionBar?.hide()

        findViewById<TextView>(R.id.showlisttext).text = "예약 현황"

        findViewById<Button>(R.id.showlistBackButton).setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}