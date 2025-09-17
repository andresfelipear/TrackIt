package com.aarevalo.trackit.map.domain.location

data class LocationData(
    val distanceMeters: Int = 0,
    val locations: List<List<LocationWithTimestamp>> = emptyList()
)
