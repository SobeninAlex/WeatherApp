package com.example.weatherapp.models

data class WeatherResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)