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
data class PackingCardModel(
    val packing_id: Long,
    val store_id: Long,
    val totalPrice: Double,
    val packing_date: String,
    val packing_time: String,
    val user_comments: String,
    val packing_status: String,
    val payment_type: String,
    val menuItemDTOList: List<MenuDTO>? = emptyList()
)
data class StoreDTO(
    val store_id: Long,
    val store_name: String,
    val store_address:String?,
    val storeCategory: String?,
    val store_phone:String?,
    val res_status:String?,
    val store_intro:String?,
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val reservationCancel:LocalTime?,
    val imagePath: String?
)

data class ReservationDTO(
    val user_id: String?,
    val user_phone: String?,
    val user_comments: String?,
    val reservation_date: String?,
    val reservation_time: String?,
    val people_cnt: Int?,
    val store_id: Long?
)

data class PackingOrder(
    val user_id: String,
    val user_phone: String,
    val user_comments: String,
    val packing_date: String,
    val packing_time: String,
    val payment_type: String,
    val store_id: String,
    val menuItemDTOList: List<MenuDTO>
)

data class MenuDTO(
    val menu_id: String,
    var menu_price: String,
    val menu_name: String,
    var quantity: String,
)

data class MenuItemDTO(
    val menu_id: Long?,
    var menu_price: Double,
    val menu_name: String,
    var quantity: Int,
    val initialPrice: Double
)

data class MenuModel(
    val menu_id: Long,
    val menu_name: String,
    val explanation: String?,
    val menu_image: String?,
    val menu_price: Double
    // 서버에서 받는 메뉴 관련 다른 필드도 추가할 수 있습니다.
)

