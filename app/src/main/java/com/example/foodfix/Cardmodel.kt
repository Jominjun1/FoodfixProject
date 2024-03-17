package com.example.foodfix

data class CardModel(
    val title: String, val detail: String, val image: Int
)
data class ReviewCardModel(
    val name: String, val detail: String, val rating: Float, val date: String, val image: Int
)
data class ReservationCardModel(
    val storename: String, val date: String, val time: String, val people: Int, val detail: String
)
