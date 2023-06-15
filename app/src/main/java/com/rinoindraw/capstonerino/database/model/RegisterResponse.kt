package com.rinoindraw.capstonerino.database.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("code")
    val code: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: List<RegisterUser>? = null,

)
