package com.aarevalo.trackit.map.presentation.maps

import com.aarevalo.trackit.map.domain.location.Location
import com.aarevalo.trackit.map.domain.location.LocationWithTimestamp

data class MapScreenState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val location: Location? = null,
    val selectedLocation: LocationWithTimestamp? = null,
    val trackingLocations: List<List<LocationWithTimestamp>> = emptyList(),
    val showLocationRationale: Boolean = false,
    val showNotificationRationale: Boolean = false
)
