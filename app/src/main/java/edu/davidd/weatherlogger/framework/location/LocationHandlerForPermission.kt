package edu.davidd.weatherlogger.framework.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.davidd.weatherlogger.R
import edu.davidd.weatherlogger.framework.ui.UiMessage
import edu.davidd.weatherlogger.framework.ui.UiMessageAction
import edu.davidd.weatherlogger.framework.ui.showMessage

class LocationHandlerForPermission {

    fun isGranted(context: Context) =
        ContextCompat.checkSelfPermission(context, PERMISSION) == PackageManager.PERMISSION_GRANTED

    fun shouldShowRationale(activity: Activity) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            false
        else
            activity.shouldShowRequestPermissionRationale(PERMISSION)

    fun showRationale(context: Context, agreementCallback: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setTitle(R.string.location_access_dialog_title)
            .setMessage(R.string.location_access_dialog_message)
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.agree) { _, _ -> agreementCallback() }
            .show()
    }

    fun launchRequest(launcher: ActivityResultLauncher<String>) =
        launcher.launch(PERMISSION)

    fun showSettingsMessage(view: View) =
        view.showMessage(
            UiMessage(
                R.string.location_access_dialog_settings,
                UiMessageAction(
                    R.string.settings
                ) {
                    it.startActivity(
                        Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", it.packageName, null)
                        }
                    )
                }
            )
        )

    companion object {
        const val PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}