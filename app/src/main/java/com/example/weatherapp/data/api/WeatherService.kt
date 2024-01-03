package com.example.weatherapp.data.api

import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.utils.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("v1/forecast.json")
    suspend fun loadInfoWeather(
        @Query("key") key: String = API_KEY,
        @Query("q") city: String,
        @Query("days") days: Int = 10
    ): Response<WeatherResponse>

}