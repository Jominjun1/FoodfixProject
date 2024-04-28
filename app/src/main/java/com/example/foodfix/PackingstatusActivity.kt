package com.example.foodfix


import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PackingstatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)

        supportActionBar?.hide()

        findViewById<TextView>(R.id.showlisttext).text = "포장 주문 내역"

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            })
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val userService = retrofit.create(UserService::class.java)

        val itemList = mutableListOf<ReservationCardModel>()

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("jwt_token", null) ?: ""

        findViewById<Button>(R.id.showlistBackButton).setOnClickListener {
            finish()
        }
    }
}