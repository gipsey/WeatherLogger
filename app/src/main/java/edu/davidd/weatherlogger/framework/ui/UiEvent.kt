package edu.davidd.weatherlogger.framework.ui

class UiEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled() =
        if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }

    fun peekContent(): T = content
}