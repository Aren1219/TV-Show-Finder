package com.example.tvshow.model


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("average")
    val average: Double
)