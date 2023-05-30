package com.rinoindraw.capstonerino.database.repository

import androidx.paging.ExperimentalPagingApi
import com.rinoindraw.capstonerino.database.database.StoryAppDatabase
import com.rinoindraw.capstonerino.database.model.AddStoriesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val storyDatabase: StoryAppDatabase,
    private val storyService: StoryService,
) {


    suspend fun addNewStories(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        ): Flow<Result<AddStoriesResponse>> = flow {
        try {
            val bearerToken = initializeBearerToken(token)
            val storyResponse = storyService.addNewStories(bearerToken, file, description)
            emit(Result.success(storyResponse))
        } catch (story: Exception) {
            story.printStackTrace()
            emit(Result.failure(story))
        }
    }


    private fun initializeBearerToken(token: String): String {
        return "Bearer $token"
    }

}