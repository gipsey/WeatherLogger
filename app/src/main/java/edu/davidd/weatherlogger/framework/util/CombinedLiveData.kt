package edu.davidd.weatherlogger.framework.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class CombinedLiveData<T>(source1: LiveData<T>, source2: LiveData<T>) : MediatorLiveData<T>() {

    init {
        super.addSource(source1) { value = it }
        super.addSource(source2) { value = it }
    }

    override fun <T : Any?> addSource(source: LiveData<T>, onChanged: Observer<in T>) = throw UnsupportedOperationException()

    override fun <T : Any?> removeSource(toRemote: LiveData<T>) = throw UnsupportedOperationException()
}