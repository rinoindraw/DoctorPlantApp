package com.rinoindraw.capstonerino.database.model

import com.google.gson.annotations.SerializedName

data class RegisterUser(

    @field:SerializedName("username")
    val username: String? = null,
    @field:SerializedName("name")
    val name: String? = null,

)