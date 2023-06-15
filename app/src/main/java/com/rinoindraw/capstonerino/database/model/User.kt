package com.rinoindraw.capstonerino.database.model

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("username")
    val username: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("token")
    val token: String? = null

)