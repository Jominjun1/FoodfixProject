package com.example.foodfix

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("/user/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<ResponseBody> // 또는 Call<JsonElement>
    @POST("/user/logout")
    fun logoutUser(@Header("Authorization") token: String): Call<ResponseBody>
    @GET("/user/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<UserProfileResponse>
}
data class UserProfileResponse(
    val nickname: String,
    val phone: String,
    val address: String
    // 필요한 경우 다른 사용자 정보도 포함시킬 수 있습니다.
)
