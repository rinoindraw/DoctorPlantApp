package com.rinoindraw.capstonerino.ui.insert

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.rinoindraw.capstonerino.database.model.UploadResponse
import com.rinoindraw.capstonerino.database.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class InsertViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    suspend fun uploadImage(
        username: RequestBody,
        plant: RequestBody,
        image: MultipartBody.Part
        ): Flow<Result<UploadResponse>> =
        apiRepository.uploadImage(username, plant, image)

}