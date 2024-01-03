package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("condition") val condition: Condition,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val temp: Double,
)