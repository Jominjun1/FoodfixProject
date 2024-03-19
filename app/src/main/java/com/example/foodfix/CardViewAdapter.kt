package com.example.foodfix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardviewAdapter(val items: MutableList<CardModel>) :
    RecyclerView.Adapter<CardviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardviewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CardviewAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    //카드 클릭 이벤트
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateItems(newItems: List<CardModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    // 화면에 표시 될 뷰를 저장하는 역할
    // 뷰를 재활용 하기 위해 각 요소를 저장해두고 사용한다.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }

        fun bindItems(cardModel: CardModel) {
            val imageArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val detailArea = itemView.findViewById<TextView>(R.id.detailArea)
            val titleArea = itemView.findViewById<TextView>(R.id.titleArea)

            imageArea.setImageResource(cardModel.image)
            detailArea.text = cardModel.detail // "별점 ${cardModel.detail}" 혹은 다른 문자열 포맷을 직접 사용
            titleArea.text = cardModel.title
        }
    }
}