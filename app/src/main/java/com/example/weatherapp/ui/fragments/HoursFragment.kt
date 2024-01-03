package com.example.weatherapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.databinding.FragmentHoursBinding
import com.example.weatherapp.ui.MainViewModel
import com.example.weatherapp.ui.adapters.HoursAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var hoursAdapter: HoursAdapter
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) { response ->
            response.data?.let {
                it.forecast.forecastday.first().hour.also { list ->
                    hoursAdapter.submitList(list)
                }
            }
        }
    }

    private fun initAdapter() = with(binding) {
        hoursAdapter = HoursAdapter(requireContext())
        recyclerViewHours.adapter = hoursAdapter
    }

    companion object {
        fun newInstance() = HoursFragment()
    }

}