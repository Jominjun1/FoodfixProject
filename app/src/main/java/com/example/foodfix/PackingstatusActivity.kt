package com.example.foodfix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodfix.databinding.ShowlistBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PackingstatusActivity : BaseActivity() {

    private lateinit var binding: ShowlistBinding
    private lateinit var adapter: PackingCardAdapter
    private val itemList = mutableListOf<PackingCardModel>()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showlist)
        binding = DataBindingUtil.setContentView(this, R.layout.showlist)

        supportActionBar?.hide()

        findViewById<TextView>(R.id.showlisttext).text = "포장 주문 내역"

        /*val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            })
            .create()*/

        /*val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val userService = retrofit.create(UserService::class.java)*/

        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        token = sharedPref.getString("jwt_token", null) ?: ""

        adapter = PackingCardAdapter(itemList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : PackingCardAdapter.OnItemClickListener {
            override fun onCancelClick(packingId: Long) {
                confirmCancellation(packingId)
            }

            override fun onItemClick(packingCardModel: PackingCardModel) {
                showDetailsDialog(packingCardModel)
            }
        })

        loadPackings()

        findViewById<ImageView>(R.id.showlistBack).setOnClickListener {
            val intent = Intent(this@PackingstatusActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadPackings() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(UserService::class.java)

        userService.getUserPacking("Bearer $token").enqueue(object : Callback<List<PackingCardModel>> {
            override fun onResponse(call: Call<List<PackingCardModel>>, response: Response<List<PackingCardModel>>) {
                if (response.isSuccessful) {
                    itemList.clear()
                    itemList.addAll(response.body() ?: emptyList())
                    itemList.reverse()
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@PackingstatusActivity, "Failed to fetch packings", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PackingCardModel>>, t: Throwable) {
                Log.e("PackingstatusActivity", "Network Error: ${t.localizedMessage}")
                Toast.makeText(this@PackingstatusActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun confirmCancellation(packingId: Long) {
        val alertDialog = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        alertDialog.setTitle("포장 취소 확인")
            .setMessage("이 포장을 취소하시겠습니까?")
            .setPositiveButton("예") { dialog, _ ->
                cancelPacking(packingId)
                dialog.dismiss()
            }
            .setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun cancelPacking(packingId: Long) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.213.178:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(UserService::class.java)

        userService.cancelPackingOrder("Bearer $token", packingId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "포장 주문 취소 완료", Toast.LENGTH_SHORT).show()
                    loadPackings()  // Reload the packings list to reflect the changes
                } else {
                    Log.d("------------Packing", "응답 실패: ${response.code()} ${response.message()}")
                    Toast.makeText(applicationContext, "응답 실패: ${response.code()} ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("------------Packing", "네트워크 오류 발생: ${t.message}", t)
                Toast.makeText(applicationContext, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showDetailsDialog(packingCardModel: PackingCardModel) {
        var price = 0.0
        val menuItemDetails = packingCardModel.menuItemDTOList?.joinToString(separator = "\n") {
            val total = it.quantity.toInt() * it.menu_price.toDouble()
            price += total
            "${it.menu_name}: ${it.menu_price}원 - ${it.quantity}개 = ${total}원"
        } ?: "메뉴 정보가 없습니다."

        val fullMessage = "식당 ID: ${packingCardModel.store_id}\n포장 ID: ${packingCardModel.packing_id}\n\n*메뉴 정보*\n$menuItemDetails\n\n총가격: ${price}"
        val alertDialog = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        alertDialog.setTitle("포장 정보")
            .setMessage(fullMessage)
            .setPositiveButton("닫기") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
