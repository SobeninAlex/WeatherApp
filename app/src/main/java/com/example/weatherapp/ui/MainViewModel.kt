package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<NetworkResult<WeatherResponse>>()
    val weatherInfo: LiveData<NetworkResult<WeatherResponse>> get() = _weatherInfo

    fun loadInfoWeather(city: String) {
        viewModelScope.launch {
            _weatherInfo.postValue(NetworkResult.Loading())
            val response = weatherRepository.loadInfoWeather(city)
            if (response.isSuccessful) {
                response.body()?.let {
                    _weatherInfo.postValue(NetworkResult.Success(data = it))
                }
            } else {
                _weatherInfo.postValue(NetworkResult.Error(message = response.message()))
            }
        }
    }

}