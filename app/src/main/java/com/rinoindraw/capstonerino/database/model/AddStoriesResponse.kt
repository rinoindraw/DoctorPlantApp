package com.rinoindraw.capstonerino.database.model

import com.google.gson.annotations.SerializedName

data class AddStoriesResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null

)
