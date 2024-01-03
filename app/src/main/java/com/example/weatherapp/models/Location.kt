package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("country") val country: String,
    @SerializedName("localtime") val localtime: String,
    @SerializedName("name") val city: String,
    @SerializedName("region") val region: String,
    @SerializedName("tz_id") val timeZoneId: String
)