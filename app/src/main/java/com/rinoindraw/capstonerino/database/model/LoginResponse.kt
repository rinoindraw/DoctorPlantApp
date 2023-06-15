package com.rinoindraw.capstonerino.database.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("code")
    val code: Int? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("loginResult")
    val loginResult: User? = null,

)