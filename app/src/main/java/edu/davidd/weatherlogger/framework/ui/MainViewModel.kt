package edu.davidd.weatherlogger.framework.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.davidd.weatherlogger.R
import edu.davidd.weatherlogger.domain.model.WeatherData
import edu.davidd.weatherlogger.domain.usecase.DownloadDataForLocation
import edu.davidd.weatherlogger.domain.usecase.LoadDataHistory
import edu.davidd.weatherlogger.framework.location.LocationProvider

class MainViewModel(
    private val locationProvider: LocationProvider,
    private val loadDataHistory: LoadDataHistory,
    private val downloadDataForLocation: DownloadDataForLocation,
) : ViewModel() {

    private val _data = MutableLiveData<List<WeatherData>>()
    val data: LiveData<List<WeatherData>> = _data

    private val _onLocationUnavailable = MutableLiveData<Boolean>()
    val onLocationUnavailable: LiveData<Boolean> = _onLocationUnavailable

    private val _messageResId = MutableLiveData<Int>()
    val messageResId: LiveData<Int> = _messageResId

    fun requestLocationUpdates() {
        locationProvider.request(
            { data ->
                loadData(data.latitude, data.longitude)
            }, {
                locationProvider.reset()
                if (_onLocationUnavailable.value != true) _onLocationUnavailable.value = true
            })
    }

    fun loadDataHistory() {
        loadDataHistory(Unit) { result ->
            result.either(
                {
                    _messageResId.value = R.string.error_loading
                },
                {
                    _data.value = it
                }
            )
        }
    }

    private fun loadData(latitude: Double, longitude: Double) {
        downloadDataForLocation(DownloadDataForLocation.Params(latitude, longitude)) { result ->
            result.either(
                {
                    _messageResId.value = R.string.error_loading
                },
                {
                    loadDataHistory()
                }
            )
        }
    }
}