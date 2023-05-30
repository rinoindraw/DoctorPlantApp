package com.rinoindraw.capstonerino.database.repository

import com.rinoindraw.capstonerino.database.model.AddStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryService {

    @Multipart
    @POST("stories")
    suspend fun addNewStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        ): AddStoriesResponse
}