package com.example.foodfix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TakeoutmenuAdapter(val items: MutableList<MenuItemDTO>) : RecyclerView.Adapter<TakeoutmenuAdapter.ViewHolder>() {

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

    // 화면에 표시 될 뷰를 저장하는 역할
    // 뷰를 재활용 하기 위해 각 요소를 저장해두고 사용한다.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val downButton: ImageView = itemView.findViewById(R.id.downbutton)
        val upButton: ImageView = itemView.findViewById(R.id.upbutton)
        val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)
        /*init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }*/

        fun bindItems(menuModel: MenuItemDTO) {
            val menu_name = itemView.findViewById<TextView>(R.id.menu_name)
            val menu_price = itemView.findViewById<TextView>(R.id.menu_price)
            val menuNum = itemView.findViewById<TextView>(R.id.menuNum)

            menu_name.text = menuModel.menu_name
            menu_price.text = menuModel.menu_price.toString()
            menuNum.text = menuModel.quantity.toString()
        }
    }
}