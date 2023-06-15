package com.rinoindraw.capstonerino.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    @SerializedName("ID")
    val id: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("tanaman")
    val tanaman: String,
    @SerializedName("penyakit")
    val penyakit: String,
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