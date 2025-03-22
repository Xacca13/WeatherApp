package com.example.weatherapp.model

data class Day(
    val city: String,
    val time: String,
    val condition: String,
    val iconLink: String,
    val currentTime: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: String
)
