package com.example.weatherapp.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val liveDataCurrent = MutableLiveData<Weather>()
    val liveDataList = MutableLiveData<List<Weather>>()
}