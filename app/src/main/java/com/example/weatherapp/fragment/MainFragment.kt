package com.example.weatherapp.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ViewPageAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.extension.isPermissionGranted
import com.example.weatherapp.model.MainViewModel
import com.example.weatherapp.model.Weather
import com.example.weatherapp.service.WeatherService
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainFragment : Fragment() {
    private val weatherService: WeatherService = WeatherService()
    private val fragmentList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private lateinit var tabList: List<String>
    private lateinit var binding: FragmentMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private val model: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getResource()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        updateCurrentCard()
        weatherService.requestWeatherData("Омск", context, model)
//        requestWeatherData("Москва")
    }

    private fun init() = with(binding) {
        val adapter = ViewPageAdapter(activity as FragmentActivity, fragmentList)
        viewPage.adapter = adapter
        TabLayoutMediator(tabLayout, viewPage){
            tab, pos ->
            tab.text = tabList[pos]
        }.attach()
    }

    private fun updateCurrentCard() = with(binding) {
        model.liveDataCurrent.observe(viewLifecycleOwner){
            tvDate.text = it.time
            tvCurrentTemp.text = it.currentTemp
            tvCity.text = it.city
            tvCondition.text = it.condition
            val minMaxTemp = "${it.minimalTemp}/${it.maximalTemp}"
            tvTempMinMax.text = minMaxTemp
            Picasso.get().load(it.iconLink).into(iconWeather)
        }
    }

    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /*private fun requestWeatherData(city: String) {
        val link = "https://api.weatherapi.com/v1/forecast.json" +
                "?key=$API_KEY" +
                "&q=$city" +
                "&days=3" +
                "&aqi=no" +
                "&alerts=no"
        val req = StringRequest(
            Request.Method.GET,
            link,
            { result -> parseRequestWeather(result)},
            { err -> Log.e("MyLog", "Error: $err")}
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(req)
    }

    private fun parseRequestWeather(result: String) {
        val body =  JSONObject(result)
        val list = parseDaysWeather(body)
        parseBodyToWeather(body, list[0])
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
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getString("maxtemp_c"),
                "https:" + day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        return list
    }

    private fun parseBodyToWeather(body: JSONObject, weatherItem: Weather) {
        val item = Weather(
            body.getJSONObject("location").getString("region"),
            body.getJSONObject("current").getString("last_updated"),
            body.getJSONObject("current").getJSONObject("condition").getString("text"),
            body.getJSONObject("current").getString("temp_c"),
            weatherItem.minimalTemp,
            weatherItem.maximalTemp,
            "https:" + body.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        model.liveDataCurrent.value = item
    }*/

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private fun getResource() {
        tabList = listOf(
            getString(R.string.tab_hours),
            getString(R.string.tab_days)
        )
    }
}