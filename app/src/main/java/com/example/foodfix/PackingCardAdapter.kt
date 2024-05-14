package com.example.foodfix

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PackingCardAdapter(val revervationitems: MutableList<PackingCardModel>) : RecyclerView.Adapter<PackingCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackingCardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.packing_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PackingCardAdapter.ViewHolder, position: Int) {
        holder.bindItems(revervationitems[position])
        holder.cancelButton.setOnClickListener {
            listener?.onCancelClick(position)
        }
    }

    override fun getItemCount(): Int {
        return revervationitems.count()
    }

    // 클릭 이벤트를 처리하기 위한 인터페이스 정의
    interface OnItemClickListener {
        fun onCancelClick(position: Int)
    }

    var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // 화면에 표시 될 뷰를 저장하는 역할
    // 뷰를 재활용 하기 위해 각 요소를 저장해두고 사용한다.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)
        /*init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }*/
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedItem = revervationitems[position]
                    val menuItemDetails = selectedItem.menuItemDTOList?.joinToString(separator = "\n") {
                        "${it.menu_name}: ${it.quantity}개 - ${it.menu_price}원"
                    }

                    // 메뉴 정보를 Toast 메시지로 표시
                    Toast.makeText(itemView.context, "메뉴 정보:\n$menuItemDetails", Toast.LENGTH_LONG).show()

                    Log.d("PackingCardAdapter", "Selected Item's menuItemDTOList: ${selectedItem.menuItemDTOList}")
                }
            }
        }

        fun bindItems(packingCardModel: PackingCardModel) {
            val packing_id = itemView.findViewById<TextView>(R.id.packingId)
            val store_id = itemView.findViewById<TextView>(R.id.storeId)
            val date = itemView.findViewById<TextView>(R.id.packingDate)
            val time = itemView.findViewById<TextView>(R.id.packingTime)
            val price = itemView.findViewById<TextView>(R.id.menuTotalPrice)
            val payment_type = itemView.findViewById<TextView>(R.id.payment_type)
            val packingcomment = itemView.findViewById<TextView>(R.id.packingcomment)
            val packingstatus = itemView.findViewById<TextView>(R.id.packingstatus)

            val menuItemDTOList = packingCardModel.menuItemDTOList
            val store_name = packingCardModel.store_name

            packing_id.text = packingCardModel.packing_id.toString()
            store_id.text = packingCardModel.store_id.toString()
            date.text = packingCardModel.packing_date
            time.text = packingCardModel.packing_time
            price.text = packingCardModel.total_price.toString()
            payment_type.text = packingCardModel.payment_type
            packingcomment.text = packingCardModel.user_comments
            packingstatus.text = packingCardModel.packing_status
        }
    }
}