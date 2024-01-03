package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Hour(
    @SerializedName("condition") val condition: Condition,
    @SerializedName("temp_c") val temp: Double,
    @SerializedName("time") val time: String,
)