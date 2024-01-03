package com.example.weatherapp.models

import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("icon") val icon: String,
    @SerializedName("text") val text: String
)