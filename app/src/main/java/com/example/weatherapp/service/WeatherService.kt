package com.example.weatherapp.service

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.model.MainViewModel
import com.example.weatherapp.model.Weather
import org.json.JSONObject

private const val API_KEY: String = "ba2fa8ea575a4294b24144301252203"

class WeatherService {

    fun requestWeatherData(city: String, context: Context?, model: MainViewModel) {
        val link = "https://api.weatherapi.com/v1/forecast.json" +
                "?key=$API_KEY" +
                "&q=$city" +
                "&days=3" +
                "&aqi=no" +
                "&alerts=no"
        val req = StringRequest(
            Request.Method.GET,
            link,
            { result -> parseRequestWeather(result, model)},
            { err -> Log.e("MyLog", "Error: $err")}
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(req)
    }


    private fun parseRequestWeather(result: String, model: MainViewModel) {
        val body =  JSONObject(result)
        val list = parseDaysWeather(body)
        val weather = parseBodyToWeather(body, list[0])
        model.liveDataCurrent.value = weather
        model.liveDataList.value = list
    }


    private fun parseBodyToWeather(body: JSONObject, weatherItem: Weather): Weather {
        val item = Weather(
            body.getJSONObject("location").getString("region"),
            body.getJSONObject("current").getString("last_updated"),
            body.getJSONObject("current").getJSONObject("condition").getString("text"),
            body.getJSONObject("current").getString("temp_c").toFloat().toInt().toString() + "°",
            weatherItem.minimalTemp,
            weatherItem.maximalTemp,
            "https:" + body.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        return item
    }

    private fun parseDaysWeather(body: JSONObject): List<Weather> {
        val list = ArrayList<Weather>()
        val daysArray = body.getJSONObject("forecast").getJSONArray("forecastday")
        val name = body.getJSONObject("location").getString("region")
        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = Weather(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                "",
                day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString() + "°",
                day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString() + "°",
                "https:" + day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        return list
    }
}