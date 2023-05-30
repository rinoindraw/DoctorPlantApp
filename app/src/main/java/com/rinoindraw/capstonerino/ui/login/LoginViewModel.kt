package com.rinoindraw.capstonerino.ui.login

import androidx.lifecycle.ViewModel
import com.rinoindraw.capstonerino.database.repository.AuthRepository
import com.rinoindraw.capstonerino.database.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun loginUser(email: String, password: String): Flow<Result<LoginResponse>> = authRepository.loginUser(email, password)

}