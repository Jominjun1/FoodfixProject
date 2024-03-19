package com.example.foodfix

import java.time.LocalDate
import java.time.LocalTime

data class CardModel(
    val title: String, val detail: String, val image: Int
)
data class ReviewCardModel(
    val name: String, val detail: String, val rating: Float, val date: String, val image: Int
)
data class ReservationCardModel(
    val reservation_id: Long,
    val store_id: Long,
    val reservation_date: String,
    val reservation_time: String,
    val num_people: Int,
    val user_comments: String,
    val reservation_status: String
)
