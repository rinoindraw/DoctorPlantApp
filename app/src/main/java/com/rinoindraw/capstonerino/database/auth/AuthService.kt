package com.rinoindraw.capstonerino.database.auth

import com.rinoindraw.capstonerino.database.model.LoginRequest
import com.rinoindraw.capstonerino.database.model.LoginResponse
import com.rinoindraw.capstonerino.database.model.RegisterRequest
import com.rinoindraw.capstonerino.database.model.RegisterResponse
import retrofit2.http.*

interface AuthService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): RegisterResponse

}