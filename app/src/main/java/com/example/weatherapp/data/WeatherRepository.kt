package com.example.weatherapp.data

import com.example.weatherapp.data.api.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService
) {

    suspend fun loadInfoWeather(city: String) =
        weatherService.loadInfoWeather(city = city)

}