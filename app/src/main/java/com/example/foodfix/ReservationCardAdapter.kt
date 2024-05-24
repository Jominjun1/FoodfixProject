import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.example.foodfix.R
import com.example.foodfix.ReservationCardModel

class ReservationCardAdapter(private val reservationItems: MutableList<ReservationCardModel>) : RecyclerView.Adapter<ReservationCardAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onCancelClick(reservationId: Long)
        fun onItemClick(reservationCardModel: ReservationCardModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reservation_cardview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(reservationItems[position])
    }

    override fun getItemCount(): Int {
        return reservationItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cancelButton: ImageButton = itemView.findViewById(R.id.cancelButton)
        private val storeName: TextView = itemView.findViewById(R.id.storename)
        private val reservationDate: TextView = itemView.findViewById(R.id.reservationDate)
        private val reservationTime: TextView = itemView.findViewById(R.id.reservationTime)
        private val peopleNum: TextView = itemView.findViewById(R.id.peopleNum)
        private val details: TextView = itemView.findViewById(R.id.reservationcomment)
        private val status: TextView = itemView.findViewById(R.id.reservationtatus)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(reservationItems[position])
                }
            }

            cancelButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onCancelClick(reservationItems[position].reservation_id)
                }
            }
        }

        fun bindItems(reservationCardModel: ReservationCardModel) {
            storeName.text = reservationCardModel.store_name
            reservationDate.text = reservationCardModel.reservation_date
            reservationTime.text = reservationCardModel.reservation_time
            peopleNum.text = reservationCardModel.num_people.toString()
            details.text = reservationCardModel.user_comments
            status.text = reservationCardModel.reservation_status

            // Set visibility based on reservation status
            when (reservationCardModel.reservation_status) {
                "예약 대기", "예약 성공", "예약 완료" -> cancelButton.visibility = View.VISIBLE
                else -> cancelButton.visibility = View.INVISIBLE
            }
        }
    }
}
