package com.example.weatherapp.model

data class Weather(
    val city: String,
    val time: String,
    val condition: String,
    val currentTemp: String,
    val minimalTemp: String,
    val maximalTemp: String,
    val iconLink: String,
    val hours: String
)
