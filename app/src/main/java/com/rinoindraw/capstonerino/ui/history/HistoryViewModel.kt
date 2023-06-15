package com.rinoindraw.capstonerino.ui.history

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.rinoindraw.capstonerino.database.model.HistoryResponse
import com.rinoindraw.capstonerino.database.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    fun getHistory(username: String): Flow<Result<HistoryResponse>> =
        apiRepository.getHistory(username)

}