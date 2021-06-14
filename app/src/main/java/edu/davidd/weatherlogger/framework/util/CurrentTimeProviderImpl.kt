package edu.davidd.weatherlogger.framework.util

import edu.davidd.weatherlogger.domain.CurrentTimeProvider

class CurrentTimeProviderImpl : CurrentTimeProvider {
    override fun get() = System.currentTimeMillis()
}
