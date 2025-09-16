package com.aarevalo.trackit.core.domain.location

import kotlin.time.Duration

data class LocationWithTimestamp(
    val location: Location,
    val timestamp: Duration,
    val listPhotos: List<File>
)
