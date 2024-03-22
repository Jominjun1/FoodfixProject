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
data class StoreDTO(
    val store_id: Long,
    val store_name: String,
    val storeImage: String?,
    val store_address:String?,
    val storeCategory: String?,
    val store_phone:String?,
    val res_status:String?,
    val store_intro:String?,
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val reservationCancel:LocalTime?
)

data class MenuModel(
    val menu_id: Long,
    val menu_name: String,
    val explanation: String?,
    val menu_image: String?,
    val menu_price: Double
    // 서버에서 받는 메뉴 관련 다른 필드도 추가할 수 있습니다.
)
