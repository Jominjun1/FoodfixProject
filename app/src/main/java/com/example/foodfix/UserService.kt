package com.example.foodfix

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @POST("user/signup")
    fun registerUser(@Body user: User): Call<ResponseBody>
    @POST("/user/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<ResponseBody> // 또는 Call<JsonElement>
    @POST("/user/logout")
    fun logoutUser(@Header("Authorization") token: String): Call<ResponseBody>
    @GET("/user/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<UserProfileResponse>
    @DELETE("/user/delete")
    fun deleteUser(@Header("Authorization") token: String): Call<ResponseBody>
    @PUT("update")
    fun updateUser(@Header("Authorization") token: String, @Body userInfo: UserProfileResponse): Call<ResponseBody>
}
data class UserProfileResponse(
    val nickname: String,
    @SerializedName("user_phone") val phone: String,
    @SerializedName("user_address") val address: String
    // 필요한 경우 다른 사용자 정보도 포함시킬 수 있습니다.
)
