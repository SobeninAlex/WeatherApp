package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday") val forecastday: List<ForecastDay>
)