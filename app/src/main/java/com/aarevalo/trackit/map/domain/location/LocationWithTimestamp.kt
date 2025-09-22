package com.aarevalo.trackit.map.domain.location

import java.io.File
import kotlin.time.Duration

data class LocationWithTimestamp(
    val location: Location,
    val timestamp: Duration,
    val listPhotos: List<File>? = null
)
