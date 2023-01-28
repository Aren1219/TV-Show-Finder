package com.example.tvshow.model


import com.google.gson.annotations.SerializedName

data class SearchResponseItem(
    @SerializedName("score")
    val score: Double,
    @SerializedName("show")
    val show: Show
)