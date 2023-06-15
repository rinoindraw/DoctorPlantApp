package com.rinoindraw.capstonerino.database.repository

import androidx.paging.ExperimentalPagingApi
import com.rinoindraw.capstonerino.database.history.ApiService
import com.rinoindraw.capstonerino.database.model.HistoryResponse
import com.rinoindraw.capstonerino.database.model.UploadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
class ApiRepository @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun uploadImage(
        username: RequestBody,
        plant: RequestBody,
        image: MultipartBody.Part
    ): Flow<Result<UploadResponse>> = flow {
        try {
            val uploadResponse = apiService.uploadImage(username, plant ,image)
            emit(Result.success(uploadResponse))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }

    fun getHistory(username: String): Flow<Result<HistoryResponse>> = flow {
        try {
            val historyResponse = apiService.getHistory(username)
            emit(Result.success(historyResponse))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }

}