package com.aarevalo.trackit.map.presentation.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aarevalo.trackit.map.domain.location.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingMapViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val hasLocationPermission = MutableStateFlow(true)
    private val shouldTrack = MutableStateFlow(false)

    private val _state = MutableStateFlow(TrackingLocationState())

    private val isAllowedToTrack = combine(
        hasLocationPermission,
        shouldTrack
    ) { hasLocationPermission, shouldTrack ->
        hasLocationPermission && shouldTrack
    }.onEach { isAllowedToTrack ->
        if(isAllowedToTrack) {
            locationTracker.startObservingLocation()
        } else {
            locationTracker.stopObservingLocation()
        }
    }

    init {
        isAllowedToTrack.onEach {
            updateState { state ->
                state.copy(
                    isPaused = !it
                )
            }
        }
            .launchIn(viewModelScope)

        locationTracker.isTracking.onEach {
            updateState { state ->
                state.copy(
                    isTracking = it
                )
            }
        }
            .launchIn(viewModelScope)

        locationTracker.currentLocation.onEach { location ->
            updateState { state ->
                state.copy(
                    location = location
                )
            }
        }
            .launchIn(viewModelScope)

        locationTracker.locationData.onEach {
            updateState { state ->
                state.copy(
                    trackingLocations = it.locations
                )
            }
        }
            .launchIn(viewModelScope)
    }

    val state: StateFlow<TrackingLocationState> = _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TrackingLocationState()
        )

    fun onAction(intent: TrackingIntent) {
        viewModelScope.launch {
            when(intent) {
                TrackingIntent.PauseTracking -> {
                    shouldTrack.value = false
                }

                TrackingIntent.ResumeTracking -> {
                    shouldTrack.value = true
                    locationTracker.setIsTracking(true)
                }

                TrackingIntent.StartTracking -> {
                    shouldTrack.value = true
                    locationTracker.setIsTracking(true)
                }

                is TrackingIntent.SubmitLocationPermissionInfo -> {
                    hasLocationPermission.value = intent.acceptedLocationPermission
                    updateState { state ->
                        state.copy(
                            showLocationRationale = intent.showLocationRationale
                        )
                    }
                }
                is TrackingIntent.SubmitNotificationPermissionInfo -> {
                    updateState { state ->
                        state.copy(
                            showNotificationRationale = intent.showNotificationRationale
                        )
                    }
                }
            }
        }
    }

    private fun updateState(update: (TrackingLocationState) -> TrackingLocationState) {
        _state.update {
            update(it)
        }
    }

}