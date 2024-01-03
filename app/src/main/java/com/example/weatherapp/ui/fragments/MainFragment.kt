package com.example.weatherapp.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.ui.MainViewModel
import com.example.weatherapp.ui.adapters.ViewPagerAdapter
import com.example.weatherapp.utils.isPermissionGranted
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()
        initViewPagerAdapter()
        viewModel.loadInfoWeather("Yekaterinburg")
        viewModelObserver()

        binding.sync.setOnClickListener {
            val city = binding.city.text.toString()
            viewModel.loadInfoWeather(city)
        }
    }

    private fun viewModelObserver() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) {
            with(binding) {
                it.data?.let { response ->
                    lastUpdate.text = response.current.lastUpdated
                    city.text = response.location.city
                    currentTemp.text = getString(R.string.current_temp, response.current.temp.toString())
                    condition.text = response.current.condition.text
                    val maxTemp = response.forecast.forecastday.first().day.maxTemp.toString()
                    val minTemp = response.forecast.forecastday.first().day.minTemp.toString()
                    tempMaxMin.text = getString(R.string.temp_min_max, minTemp, maxTemp)
                    Picasso.get().load("https:" + response.current.condition.icon).into(icon)
                }
            }
        }
    }

    private fun initViewPagerAdapter() {
        viewPagerAdapter = ViewPagerAdapter(activity as FragmentActivity)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val array = resources.getStringArray(R.array.fragments_name)
            tab.text = array[position]
        }.attach()
    }

    private fun permissionListener() {
        launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) {
            Toast.makeText(requireContext(), "Permission is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}