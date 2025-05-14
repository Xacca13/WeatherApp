package com.example.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.databinding.FragmentHoursBinding
import com.example.weatherapp.model.MainViewModel
import com.example.weatherapp.model.Weather
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycledView()
        model.liveDataCurrent.observe(viewLifecycleOwner){
            adapter.submitList(getHoursList(it))
        }
    }

    private fun initRecycledView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter(null)
        rcView.adapter = adapter
    }

    private fun getHoursList(weather: Weather): List<Weather> {
        val hoursArray = JSONArray(weather.hours)
        val list = ArrayList<Weather>()
        for (i in 0 until hoursArray.length()) {
            list.add(
                Weather(
                weather.city,
                (hoursArray[i] as JSONObject).getString("time").substring(10),
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                (hoursArray[i] as JSONObject).getString("temp_c").toFloat().toInt().toString() + "°",
                "",
                "",
                "https:" + (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                ""
                )
            )
        }
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}