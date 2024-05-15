package com.example.foodfix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init

class TakeoutmenuAdapter( private val items: MutableList<MenuItemDTO>,
                          private val updateTotalPriceCallback: () -> Unit) : RecyclerView.Adapter<TakeoutmenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakeoutmenuAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.menu_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TakeoutmenuAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
        holder.cancelButton.setOnClickListener {
            listener?.onCancelClick(position)
            listener?.onDownClick(position)
            listener?.onUpClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    // 클릭 이벤트를 처리하기 위한 인터페이스 정의
    interface OnItemClickListener {
        fun onCancelClick(position: Int)
        fun onDownClick(position: Int)
        fun onUpClick(position: Int)
    }

    var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size) // 인덱스 갱신을 위해 추가
    }

    // 화면에 표시 될 뷰를 저장하는 역할
    // 뷰를 재활용 하기 위해 각 요소를 저장해두고 사용한다.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downButton: ImageView = itemView.findViewById(R.id.downbutton)
        val upButton: ImageView = itemView.findViewById(R.id.upbutton)
        val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)


        init {
            downButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && items[position].quantity > 1) {
                    items[position].quantity--
                    items[position].menu_price = items[position].initialPrice * items[position].quantity
                    notifyItemChanged(position)
                    updateTotalPriceCallback() // 총 가격 업데이트
                }
            }

            upButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    items[position].quantity++
                    items[position].menu_price = items[position].initialPrice * items[position].quantity
                    notifyItemChanged(position)
                    updateTotalPriceCallback() // 총 가격 업데이트
                }
            }

            cancelButton.setOnClickListener {
                // 위치에 따라 아이템 삭제
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    removeItem(adapterPosition)
                }
            }
        }

        fun bindItems(menuModel: MenuItemDTO) {
            val menu_name = itemView.findViewById<TextView>(R.id.menu_name)
            val menu_price = itemView.findViewById<TextView>(R.id.menu_price)
            val menuNum = itemView.findViewById<TextView>(R.id.menuNum)
            val downButton: ImageView = itemView.findViewById(R.id.downbutton)
            val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)

            val initialPrice = menuModel.initialPrice

            menu_name.text = menuModel.menu_name
            menu_price.text = menuModel.menu_price.toString()
            menuNum.text = menuModel.quantity.toString()

            // menuNum 값이 1일 때 downButton 숨기기, cancelButton 보이기
            if (menuModel.quantity <= 1) {
                downButton.visibility = View.INVISIBLE // 또는 View.GONE
                cancelButton.visibility = View.VISIBLE
            } else {
                downButton.visibility = View.VISIBLE
                cancelButton.visibility = View.INVISIBLE // 또는 View.GONE, 상황에 따라
            }
        }
    }
}