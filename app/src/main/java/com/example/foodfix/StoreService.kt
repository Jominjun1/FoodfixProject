package com.example.foodfix

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalTime

interface StoreService {
    @GET("/store/reservable")
    fun searchReservableStores(
        @Query("store_category") storeCategory: String?,
        @Query("store_name") storeName: String?,
        @Query("menu_name") menuName: String?
    ): Call<List<StoreDTO>>

    @GET("/store/packable")
    fun searchPackableStores(
        @Query("store_category") storeCategory: String?,
        @Query("store_name") storeName: String?,
        @Query("menu_name") menuName: String?
    ): Call<List<StoreDTO>>

    @POST("/order/reservation")
    fun createReservation(@Body reservationDTO: ReservationDTO): Call<ResponseBody>

    @POST("/order/packing")
    fun createPackingOrder(@Body packingOrder: PackingOrder): Call<ResponseBody>
}


