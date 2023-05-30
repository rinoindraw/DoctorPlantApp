package com.rinoindraw.capstonerino.database.repository

import com.rinoindraw.capstonerino.database.model.LoginResponse
import com.rinoindraw.capstonerino.database.model.RegisterResponse
import com.rinoindraw.capstonerino.database.auth.AuthService
import com.rinoindraw.capstonerino.database.source.AuthPreferenceDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val preferencesDataSource: AuthPreferenceDataSource
) {

    suspend fun loginUser(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = authService.loginUser(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = authService.registerUser(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        preferencesDataSource.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?> = preferencesDataSource.getAuthToken()

}