package com.aarevalo.trackit.map.domain.location

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.time.Duration

class LocationTracker @Inject constructor(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {

    private val _locationData = MutableStateFlow(LocationData())
    val locationData = _locationData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    fun setIsTracking(isTracking: Boolean) {
        this._isTracking.value = isTracking
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    fun finishTracking() {
        stopObservingLocation()
        setIsTracking(false)
        _elapsedTime.value = Duration.ZERO
        _locationData.value = LocationData()
    }

    fun updateLocationData(locationData: LocationData) {
        _locationData.value = locationData
    }

    fun updateElapsedTime(elapsedTime: Duration) {
        _elapsedTime.value = elapsedTime

    }
}