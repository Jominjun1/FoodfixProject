package com.example.foodfix

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.TakeoutMenuBinding

class TakeoutActivity : AppCompatActivity(){

    lateinit var binding: TakeoutMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takeout_menu)
        binding = DataBindingUtil.setContentView(this, R.layout.takeout_menu)

        supportActionBar?.hide()

        val itemList = mutableListOf<MenuItemDTO>()
        val adapter = TakeoutmenuAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : TakeoutmenuAdapter.OnItemClickListener {
            override fun onCancelClick(position: Int) {
                // 여기에서 취소 버튼이 클릭된 아이템의 처리 로직을 구현하세요.
                Toast.makeText(this@TakeoutActivity, "Cancel clicked at position $position", Toast.LENGTH_SHORT).show()
            }
            override fun onDownClick(position: Int) {
                // 여기에서 취소 버튼이 클릭된 아이템의 처리 로직을 구현하세요.
                Toast.makeText(this@TakeoutActivity, "Down clicked at position $position", Toast.LENGTH_SHORT).show()
            }
            override fun onUpClick(position: Int) {
                // 여기에서 취소 버튼이 클릭된 아이템의 처리 로직을 구현하세요.
                Toast.makeText(this@TakeoutActivity, "Up clicked at position $position", Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<Button>(R.id.restaurant_detailBackButton).setOnClickListener {
            finish()
        }
    }
}