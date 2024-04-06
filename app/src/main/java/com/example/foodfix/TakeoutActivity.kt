package com.example.foodfix

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.TakeoutMenuBinding

class TakeoutActivity : AppCompatActivity(){

    lateinit var binding: TakeoutMenuBinding
    private val itemList = mutableListOf<MenuItemDTO>() // 클래스 멤버로 변경

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takeout_menu)
        binding = DataBindingUtil.setContentView(this, R.layout.takeout_menu)

        supportActionBar?.hide()

        val adapter = TakeoutmenuAdapter(itemList) {
            updateTotalPrice() // 어댑터에서 호출할 콜백
        }
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)


        var menu_id = 1
        var menu_price = 2000.0
        var menu_name = "핫도그"
        var quantity = 4
        var initialPrice = menu_price / quantity
        itemList.add(MenuItemDTO(menu_id.toLong(), menu_price, menu_name, quantity, initialPrice))
        itemList.add(MenuItemDTO(2, 10000.0, "떡볶이", 2, 5000.0))

        val total_price = itemList.sumOf { it.menu_price }
        findViewById<TextView>(R.id.totalPrice).text = total_price.toString()

        adapter.setOnItemClickListener(object : TakeoutmenuAdapter.OnItemClickListener {
            override fun onCancelClick(position: Int) {
                // 아이템 삭제
                itemList.removeAt(position)
                adapter.notifyItemRemoved(position)
                updateTotalPrice()
            }

            override fun onDownClick(position: Int) {
                adapter.notifyItemChanged(position)
                updateTotalPrice()
            }

            override fun onUpClick(position: Int) {
                adapter.notifyItemChanged(position)
                updateTotalPrice()
            }
        })

        findViewById<Button>(R.id.restaurant_detailBackButton).setOnClickListener {
            finish()
        }
    }

    private fun updateTotalPrice() {
        // 총 가격 업데이트
        val total_price = itemList.sumOf { it.menu_price }
        binding.totalPrice.text = String.format("%.2f", total_price)
    }
}