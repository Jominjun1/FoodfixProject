package com.example.foodfix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviwCardAdapter(val items: MutableList<ReviewCardModel>) :
    RecyclerView.Adapter<ReviwCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviwCardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.review_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReviwCardAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    //카드 클릭 이벤트
    /*private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }*/

    // 화면에 표시 될 뷰를 저장하는 역할
    // 뷰를 재활용 하기 위해 각 요소를 저장해두고 사용한다.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }*/

        fun bindItems(reviewCardModel: ReviewCardModel) {
            val username = itemView.findViewById<TextView>(R.id.username)
            val reviewDate = itemView.findViewById<TextView>(R.id.reviewDate)
            val reviewImage = itemView.findViewById<ImageView>(R.id.reviewimage)
            val reviewDtail = itemView.findViewById<TextView>(R.id.reviewdetail)
            val rating = itemView.findViewById<RatingBar>(R.id.reviewRating)


            username.text = reviewCardModel.name
            reviewDate.text = reviewCardModel.date
            reviewImage.setImageResource(reviewCardModel.image)
            reviewDtail.text = reviewCardModel.detail
            rating.rating = reviewCardModel.rating
        }
    }
}