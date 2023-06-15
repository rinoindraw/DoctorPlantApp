package com.rinoindraw.capstonerino.ui.register

import androidx.lifecycle.ViewModel
import com.rinoindraw.capstonerino.database.model.RegisterResponse
import com.rinoindraw.capstonerino.database.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun registerUser(name: String, username: String, password: String): Flow<Result<RegisterResponse>> =
        authRepository.userRegister(name, username, password)

}