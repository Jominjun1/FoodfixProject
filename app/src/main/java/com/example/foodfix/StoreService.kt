package com.example.foodfix

import retrofit2.Call
import retrofit2.http.GET
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
}


