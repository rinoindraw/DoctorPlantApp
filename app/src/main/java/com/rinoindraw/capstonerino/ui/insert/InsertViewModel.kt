package com.rinoindraw.capstonerino.ui.insert

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.rinoindraw.capstonerino.database.model.AddStoriesResponse
import com.rinoindraw.capstonerino.database.repository.AuthRepository
import com.rinoindraw.capstonerino.database.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class InsertViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        ): Flow<Result<AddStoriesResponse>> =
        storyRepository.addNewStories(token, file, description)

}