package com.example.weatherapp.fragment

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.example.weatherapp.R
import com.example.weatherapp.adapter.VpAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.fragment.extension.isPermissionGranted
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var API_KEY: String

    private val fragmentList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private lateinit var tabList: List<String>
    private lateinit var binding: FragmentMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<String>

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
    }

    private fun init() = with(binding) {
        val adapter = VpAdapter(activity as FragmentActivity, fragmentList)
        viewPage.adapter = adapter
        TabLayoutMediator(tabLayout, viewPage){
            tab, pos ->
            tab.text = tabList[pos]
        }.attach()
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

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private fun getResource() {
        API_KEY = getString(R.string.api_key)
        tabList = listOf(
            getString(R.string.tab_hours),
            getString(R.string.tab_days)
        )
    }
}