package com.example.foodfix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReservationCardAdapter(val revervationitems: MutableList<ReservationCardModel>) : RecyclerView.Adapter<ReservationCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationCardAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reservation_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReservationCardAdapter.ViewHolder, position: Int) {
        holder.bindItems(revervationitems[position])
    }

    override fun getItemCount(): Int {
        return revervationitems.count()
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

        fun bindItems(reservationCardModel: ReservationCardModel) {
            val storename = itemView.findViewById<TextView>(R.id.storename)
            val date = itemView.findViewById<TextView>(R.id.reservationDate)
            val time = itemView.findViewById<TextView>(R.id.reservationTime)
            val peopleNum = itemView.findViewById<TextView>(R.id.peopleNum)
            val detail = itemView.findViewById<TextView>(R.id.reservationdetail)

            storename.text = reservationCardModel.storename
            date.text = reservationCardModel.date
            time.text = reservationCardModel.time
            peopleNum.text = reservationCardModel.people.toString()
            detail.text = reservationCardModel.detail

        }
    }
}