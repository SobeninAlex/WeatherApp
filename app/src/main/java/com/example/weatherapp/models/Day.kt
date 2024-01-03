package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("condition") val condition: Condition,
    @SerializedName("maxtemp_c") val maxTemp: Double,
    @SerializedName("mintemp_c") val minTemp: Double,
)