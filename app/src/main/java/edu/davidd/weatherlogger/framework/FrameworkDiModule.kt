package edu.davidd.weatherlogger.framework

import com.google.android.gms.location.LocationServices
import edu.davidd.weatherlogger.domain.CurrentTimeProvider
import edu.davidd.weatherlogger.framework.location.LocationHandlerForPermission
import edu.davidd.weatherlogger.framework.location.LocationHandlerForSettings
import edu.davidd.weatherlogger.framework.location.LocationProvider
import edu.davidd.weatherlogger.framework.location.LocationProviderImpl
import edu.davidd.weatherlogger.framework.ui.MainViewModel
import edu.davidd.weatherlogger.framework.ui.model.WeatherDataListMapper
import edu.davidd.weatherlogger.framework.util.CurrentTimeProviderImpl
import edu.davidd.weatherlogger.framework.util.WeatherDateFormatter
import edu.davidd.weatherlogger.framework.util.WeatherTemperatureFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val frameworkDiModule = module {

    viewModel { MainViewModel(get(), get(), get()) }

    factory { LocationHandlerForPermission() }
    factory { LocationHandlerForSettings() }

    factory { LocationServices.getFusedLocationProviderClient(androidContext()) }
    factory<LocationProvider> { LocationProviderImpl(get()) }

    factory { WeatherDataListMapper(get(), get()) }

    factory { WeatherDateFormatter() }
    factory { WeatherTemperatureFormatter() }
    factory<CurrentTimeProvider> { CurrentTimeProviderImpl() }
}