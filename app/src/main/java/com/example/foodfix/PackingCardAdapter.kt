package com.example.foodfix

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class PackingCardAdapter(val packingItems: MutableList<PackingCardModel>) : RecyclerView.Adapter<PackingCardAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onCancelClick(packing_id: Long)

        fun onItemClick(packingCardModel: PackingCardModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackingCardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.packing_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PackingCardAdapter.ViewHolder, position: Int) {
        holder.bindItems(packingItems[position])
    }

    override fun getItemCount(): Int {
        return packingItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(packingItems[position])
                }
            }

            cancelButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onCancelClick(packingItems[position].packing_id)
                }
            }
        }

        fun bindItems(packingCardModel: PackingCardModel) {

            val store_name = itemView.findViewById<TextView>(R.id.storename)
            val date = itemView.findViewById<TextView>(R.id.packingDate)
            val time = itemView.findViewById<TextView>(R.id.packingTime)
            val price = itemView.findViewById<TextView>(R.id.menuTotalPrice)
            val payment_type = itemView.findViewById<TextView>(R.id.payment_type)
            val packingcomment = itemView.findViewById<TextView>(R.id.packingcomment)
            val packingstatus = itemView.findViewById<TextView>(R.id.packingstatus)

            store_name.text = packingCardModel.store_name
            date.text = packingCardModel.packing_date
            time.text = packingCardModel.packing_time
            payment_type.text = packingCardModel.payment_type
            packingcomment.text = packingCardModel.user_comments
            packingstatus.text = packingCardModel.packing_status

            var totalPrice = 0.0
            packingCardModel.menuItemDTOList?.forEach { item ->
                totalPrice += item.menu_price.toDouble() * item.quantity.toInt()
            }

            price.text = totalPrice.toString()

            when (packingCardModel.payment_type) {
                "0" -> payment_type.text = "앱결제"
                "1" -> payment_type.text = "방문 결제"
                else -> payment_type.text = "오류"
            }

            when (packingCardModel.packing_status) {
                "0" -> packingstatus.text = "주문중"
                "1" -> packingstatus.text = "주문 접수"
                "2" -> packingstatus.text = "주문 취소"
                "3" -> packingstatus.text = "주문 완료"
                else -> packingstatus.text = "오류"
            }

            // Set visibility based on reservation status
            when (packingCardModel.packing_status) {
                "0" -> cancelButton.visibility = View.VISIBLE
                else -> cancelButton.visibility = View.INVISIBLE
            }
        }
    }
}