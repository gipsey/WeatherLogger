package edu.davidd.weatherlogger.framework

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import edu.davidd.weatherlogger.BuildConfig
import edu.davidd.weatherlogger.data.dataDiModule
import edu.davidd.weatherlogger.domain.domainDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class WeatherLoggerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else TimberReleaseTree())

        startKoin {
            androidContext(this@WeatherLoggerApp)
            androidFileProperties()
            modules(listOf(frameworkDiModule, domainDiModule, dataDiModule))
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR)
        }
    }
}

private class TimberReleaseTree : Timber.Tree() {

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR) Log.e(tag, message, t)
    }
}