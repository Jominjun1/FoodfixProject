package com.example.foodfix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StoreAdapter(val items: MutableList<StoreDTO>) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {
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

    /*fun updateItems(newItems: List<CardModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }*/

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

        fun bindItems(storeDTO: StoreDTO) {
            val imageArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val detailArea = itemView.findViewById<TextView>(R.id.detailArea)
            val titleArea = itemView.findViewById<TextView>(R.id.titleArea)

            // 이미지 로딩
            Glide.with(imageArea)
                .load("http://54.180.213.178:8080/images/${storeDTO.photo?.imagePath.toString()}") // 서버에서 받은 이미지 URL
                .placeholder(R.drawable.ic_launcher_foreground) // 로딩 중에 표시될 이미지
                .error(R.drawable.ic_launcher_background) // 로딩 에러 발생 시 표시될 이미지
                .into(imageArea)

            titleArea.text = storeDTO.store_name // "별점 ${cardModel.detail}" 혹은 다른 문자열 포맷을 직접 사용
            detailArea.text = "Open: ${storeDTO.openTime?.toString()} - Close: ${storeDTO.closeTime?.toString()}"
        }
    }
}