package com.example.weatherapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.databinding.FragmentDaysBinding
import com.example.weatherapp.ui.MainViewModel
import com.example.weatherapp.ui.adapters.DaysAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaysFragment : Fragment() {

    private lateinit var binding: FragmentDaysBinding
    private lateinit var daysAdapter: DaysAdapter
    private val viewMode by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        viewMode.weatherInfo.observe(viewLifecycleOwner) { response ->
            response.data?.let {
                it.forecast.forecastday.also { list ->
                    daysAdapter.submitList(list.subList(1, list.size))
                }
            }
        }
    }

    private fun initAdapter() = with(binding) {
        daysAdapter = DaysAdapter(requireContext())
        recyclerViewDays.adapter = daysAdapter
    }

    companion object {
        fun newInstance() = DaysFragment()
    }

}