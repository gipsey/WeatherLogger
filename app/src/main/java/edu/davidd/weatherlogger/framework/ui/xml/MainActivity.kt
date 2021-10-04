package edu.davidd.weatherlogger.framework.ui.xml

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.davidd.weatherlogger.R
import edu.davidd.weatherlogger.databinding.ActivityMainBinding
import edu.davidd.weatherlogger.framework.location.LocationHandlerForPermission
import edu.davidd.weatherlogger.framework.location.LocationHandlerForSettings
import edu.davidd.weatherlogger.framework.ui.MainViewModel
import edu.davidd.weatherlogger.framework.ui.UiMessage
import edu.davidd.weatherlogger.framework.ui.model.WeatherDataListMapper
import edu.davidd.weatherlogger.framework.ui.showMessage
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val itemsAdapter by lazy { WeatherItemsAdapter() }
    private val viewModel by viewModel<MainViewModel>()

    private val weatherDataListMapper by inject<WeatherDataListMapper>()

    private val locationHandlerForPermission by inject<LocationHandlerForPermission>()
    private val locationHandlerForSettings by inject<LocationHandlerForSettings>()
    private lateinit var locationPermissionResultLauncher: ActivityResultLauncher<String>
    private lateinit var locationSettingsResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        locationPermissionResultLauncher = registerForLocationPermissionResult()
        locationSettingsResultLauncher = registerForLocationSettingsResult()

        binding.recyclerView.adapter = itemsAdapter
        binding.loadButton.setOnClickListener { validateLocationPermission() }

        viewModel.data.observe(this) {
            val items = weatherDataListMapper(this, it)
            itemsAdapter.setData(items) { binding.recyclerView.scrollToPosition(0) }
        }
        viewModel.onLocationUnavailable.observe(this) {
            if (it) validateLocationPermission()
        }
        viewModel.uiMessage.observe(this) {
            it.getContentIfNotHandled()?.let { binding.root.showMessage(it) }
        }


    }

    override fun onStart() {
        super.onStart()
        viewModel.loadDataHistory()
    }

    private fun registerForLocationPermissionResult() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it)
                onLocationPermissionGranted()
            else if (!locationHandlerForPermission.shouldShowRationale(this))
                locationHandlerForPermission.showSettingsMessage(binding.root)
        }

    private fun registerForLocationSettingsResult() =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK)
                onLocationAccessEnabled()
            else
                binding.root.showMessage(UiMessage(R.string.location_settings_denied_error))
        }

    private fun validateLocationPermission() =
        if (locationHandlerForPermission.isGranted(this)) {
            onLocationPermissionGranted()
        } else {
            if (locationHandlerForPermission.shouldShowRationale(this))
                locationHandlerForPermission.showRationale(this) {
                    locationHandlerForPermission.launchRequest(locationPermissionResultLauncher)
                }
            else
                locationHandlerForPermission.launchRequest(locationPermissionResultLauncher)
        }

    private fun onLocationPermissionGranted() {
        locationHandlerForSettings.validateLocationSettingsAvailability(this) { result ->
            when (result) {
                is LocationHandlerForSettings.Result.ResolutionRequired ->
                    locationHandlerForSettings.launchRequest(locationSettingsResultLauncher, result.exception.resolution)
                is LocationHandlerForSettings.Result.Error ->
                    binding.root.showMessage(UiMessage(result.messageResId))
                LocationHandlerForSettings.Result.Available ->
                    onLocationAccessEnabled()
            }
        }
    }

    private fun onLocationAccessEnabled() {
        Timber.d("onLocationAccessEnabled")
        viewModel.requestLocationUpdates()
    }
}