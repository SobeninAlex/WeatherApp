package com.example.weatherapp.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.databinding.SearchItemBinding
import com.example.weatherapp.ui.MainViewModel
import com.example.weatherapp.ui.adapters.ViewPagerAdapter
import com.example.weatherapp.utils.NetworkResult
import com.example.weatherapp.utils.isPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "check_load"

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var locationClient: FusedLocationProviderClient
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
        initLocationClient()
        viewModelObserver()

        binding.sync.setOnClickListener {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            checkLocation()
        }

        binding.search.setOnClickListener {
            searchCityByName()
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            locationSettingsDialog()
        }
    }

    private fun initLocationClient() {
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {
        val cancellationToken = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
            .addOnCompleteListener {
                viewModel.loadInfoWeather("${it.result.latitude},${it.result.longitude}")
            }
    }

    private fun searchCityByName() {
        val dialogBinding = SearchItemBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.city_name))
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.search), null)
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val text = dialogBinding.textInputEditText.text.toString()
                if (text.isBlank()) {
                    dialogBinding.textField.isErrorEnabled = true
                    dialogBinding.textField.error = getString(R.string.field_cannot_be_empty)
                    return@setOnClickListener
                } else {
                    dialogBinding.textField.isErrorEnabled = false
                    dialogBinding.textInputEditText.text = null
                    viewModel.loadInfoWeather(city = text)
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun locationSettingsDialog() {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.cancel()
            } else dialog.dismiss()
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.enabled_location))
            .setMessage(getString(R.string.location_disabled))
            .setPositiveButton(getString(R.string.ok), listener)
            .setNegativeButton(getString(R.string.cancel), listener)
            .create()
        dialog.show()
    }

    private fun viewModelObserver() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    with(binding) {
                        loader.visibility = View.GONE
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
                is NetworkResult.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, it.message.toString())
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