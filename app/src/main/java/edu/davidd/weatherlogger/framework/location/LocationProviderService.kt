package edu.davidd.weatherlogger.framework.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class LocationProviderService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}