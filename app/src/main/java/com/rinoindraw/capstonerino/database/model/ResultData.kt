package com.rinoindraw.capstonerino.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultData(
    @SerializedName("id_result")
    val idResult: String,
    @SerializedName("file")
    val file: String,
    @SerializedName("plant")
    val plant: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("penyebab")
    val penyebab: String,
    @SerializedName("solusi")
    val solusi: List<String>,
    @SerializedName("source")
    val source: String,
    @SerializedName("penulis")
    val penulis: String
) : Parcelable