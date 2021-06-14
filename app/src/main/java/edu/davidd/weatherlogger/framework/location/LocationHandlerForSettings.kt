package edu.davidd.weatherlogger.framework.location

import android.app.Activity
import android.app.PendingIntent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.StringRes
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import edu.davidd.weatherlogger.R
import timber.log.Timber

class LocationHandlerForSettings {

    fun validateLocationSettingsAvailability(activity: Activity, onResult: (Result) -> Unit) {
        createValidationTask(activity)
            .addOnFailureListener { exception ->
                Timber.w(exception, "checkLocationSettings failure")

                if (exception is ResolvableApiException)
                    if (exception.status.statusCode == CommonStatusCodes.RESOLUTION_REQUIRED)
                        onResult(Result.ResolutionRequired(exception))
                    else
                        onResult(Result.Error(R.string.location_settings_error))
                else
                    onResult(Result.Error(R.string.error_general))
            }
            .addOnSuccessListener {
                val states = it.locationSettingsStates
                Timber.d(
                    "checkLocationSettings success\n" +
                            "isGpsPresent=${states?.isGpsPresent}\n" +
                            "isGpsUsable=${states?.isGpsUsable}\n" +
                            "isLocationPresent=${states?.isLocationPresent}\n" +
                            "isLocationUsable=${states?.isLocationUsable}"
                )

                if (states == null || !states.isLocationPresent || !states.isLocationUsable)
                    onResult(Result.Error(R.string.location_settings_error))
                else
                    onResult(Result.Available)
            }
            .addOnCanceledListener {
                Timber.d("checkLocationSettings canceled")
                onResult(Result.Error(R.string.location_settings_error))
            }
    }

    fun launchRequest(launcher: ActivityResultLauncher<IntentSenderRequest>, resolution: PendingIntent) {
        launcher.launch(IntentSenderRequest.Builder(resolution).build())
    }

    private fun createValidationTask(activity: Activity) =
        LocationServices
            .getSettingsClient(activity)
            .checkLocationSettings(createLocationSettingsRequest())

    companion object {
        fun createLocationRequest(): LocationRequest =
            LocationRequest.create()
                .apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    smallestDisplacement = 1_000f
                    interval = 8_000
                    fastestInterval = 8_000
                }

        private fun createLocationSettingsRequest() =
            LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest()).build()
    }

    sealed class Result {
        data class Error(@StringRes val messageResId: Int) : Result()
        data class ResolutionRequired(val exception: ResolvableApiException) : Result()
        object Available : Result()
    }
}