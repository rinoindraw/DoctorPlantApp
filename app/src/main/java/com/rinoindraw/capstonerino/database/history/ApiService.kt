package com.rinoindraw.capstonerino.database.history

import com.rinoindraw.capstonerino.database.model.HistoryResponse
import com.rinoindraw.capstonerino.database.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("history")
    suspend fun getHistory(
        @Query("username") history: String? = null,
    ): HistoryResponse

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part("username") username: RequestBody,
        @Part("plant") plant: RequestBody,
        @Part image: MultipartBody.Part
    ): UploadResponse

}