package edu.davidd.weatherlogger.framework.ui.compose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import edu.davidd.weatherlogger.R
import edu.davidd.weatherlogger.framework.location.LocationHandlerForPermission
import edu.davidd.weatherlogger.framework.location.LocationHandlerForSettings
import edu.davidd.weatherlogger.framework.ui.*
import edu.davidd.weatherlogger.framework.ui.model.WeatherDataListMapper
import edu.davidd.weatherlogger.framework.util.CombinedLiveData
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : FragmentActivity() {

    private val viewModel by viewModel<MainViewModel>()
    private val weatherDataListMapper by inject<WeatherDataListMapper>()

    private val locationHandlerForPermission by inject<LocationHandlerForPermission>()
    private val locationHandlerForSettings by inject<LocationHandlerForSettings>()
    private lateinit var locationPermissionResultLauncher: ActivityResultLauncher<String>
    private lateinit var locationSettingsResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val messageLiveData = MutableLiveData<UiEvent<UiMessage>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        setContent {
            Timber.d("MainActivity - onCreate - setContent")
            val weatherItems = weatherDataListMapper(LocalContext.current, viewModel.data.observeAsState(emptyList()).value)

            Main(
                weatherItems = weatherItems,
                uiMessageEvent = CombinedLiveData(messageLiveData, viewModel.uiMessage).observeAsState().value,
                validateLocationPermission = ::validateLocationPermission
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadDataHistory()
    }

    private fun init() {
        locationPermissionResultLauncher = registerForLocationPermissionResult()
        locationSettingsResultLauncher = registerForLocationSettingsResult()

        viewModel.onLocationUnavailable.observe(this) {
            if (it) validateLocationPermission()
        }
    }

    private fun registerForLocationPermissionResult() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it)
                onLocationPermissionGranted()
            else if (!locationHandlerForPermission.shouldShowRationale(this)) {
                messageLiveData.value =
                    UiEvent(
                        UiMessage(
                            R.string.location_access_dialog_settings,
                            UiMessageAction(
                                R.string.settings
                            ) { context ->
                                context.startActivity(
                                    Intent().apply {
                                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                )
                            })
                    )
            }
        }

    private fun registerForLocationSettingsResult() =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK)
                onLocationAccessEnabled()
            else
                messageLiveData.value = UiEvent(UiMessage(R.string.location_settings_denied_error))
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
                    locationHandlerForSettings.launchRequest(
                        locationSettingsResultLauncher,
                        result.exception.resolution
                    )
                is LocationHandlerForSettings.Result.Error ->
                    messageLiveData.value = UiEvent(UiMessage(result.messageResId))
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