package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MenuActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_detail)

        supportActionBar?.hide()

        val imageArea = findViewById<ImageView>(R.id.menuimage)
        val menu_image = intent.getStringExtra("memu_imagePath")
        val parts = menu_image?.split("/")
        val fileName = parts?.last()

        // 이미지 로딩
        Glide.with(imageArea)
            .load("http://54.180.213.178:8080/images/${fileName}") // 서버에서 받은 이미지 URL
            .placeholder(R.drawable.ic_launcher_foreground) // 로딩 중에 표시될 이미지
            .error(R.drawable.ic_launcher_background) // 로딩 에러 발생 시 표시될 이미지
            .into(imageArea)

        findViewById<ImageView>(R.id.menu_detailBack).setOnClickListener {
            finish()
        }

        val menuPrice = intent.getDoubleExtra("menu_price", 1.0)
        val totalPriceTextView = findViewById<TextView>(R.id.menuPrice)

        findViewById<TextView>(R.id.menuName).text = intent.getStringExtra("menu_name")
        totalPriceTextView.text = "$menuPrice"

        findViewById<TextView>(R.id.menu_inf).text = intent.getStringExtra("explanation")

        val menu_id = intent.getLongExtra("menu_id", 0L)
        val store_id = intent.getLongExtra("store_id", 0L)

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

        findViewById<Button>(R.id.addmenuButton).setOnClickListener {
            var menu_num = findViewById<TextView>(R.id.menuNum).text.toString()
            var menu_name = findViewById<TextView>(R.id.menuName).text.toString()
            var menu_price = findViewById<TextView>(R.id.menuPrice).text.toString()

            val spf = getSharedPreferences("MyAppPreferences", MODE_PRIVATE) // 기존에 있던 데이터
            val editor =spf.edit()
            val gson = GsonBuilder().create()
            val menudata = MenuDTO(menu_id.toString(), menu_price, menu_name, menu_num)
            val menuArray = ArrayList<MenuDTO>()

            val groupListType: Type = object : TypeToken<ArrayList<MenuDTO?>?>() {}.type

            val prev =spf.getString("menuList","none") // json list 가져오기
            //val convertedData = gson.toJson(prev)

            if(prev!="none"){ //데이터가 비어있지 않다면?
                if(prev!="[]" || prev!="")menuArray.addAll(gson.fromJson(prev,groupListType))
                menuArray.add(menudata)
                val strList = gson.toJson(menuArray,groupListType)
                editor.putString("menuList",strList)
            }else{
                menuArray.add(menudata)
                val strList = gson.toJson(menuArray,groupListType)
                editor.putString("menuList",strList)
            }
            editor.apply()

            val intent = Intent(this@MenuActivity, TakeoutActivity::class.java)
            intent.putExtra("store_id", store_id)
            startActivity(intent)
            finish()
        }
    }

    // 가격 업데이트를 위한 함수
    private fun updateTotalPrice(numMenu: Int, menuPrice: Double, totalPriceTextView: TextView) {
        val totalPrice = numMenu * menuPrice
        totalPriceTextView.text = "$totalPrice"
    }
}

