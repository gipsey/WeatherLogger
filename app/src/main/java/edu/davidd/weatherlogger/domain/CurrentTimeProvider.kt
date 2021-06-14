package edu.davidd.weatherlogger.domain

interface CurrentTimeProvider {
    fun get(): Long
}