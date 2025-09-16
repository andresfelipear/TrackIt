package com.aarevalo.trackit.core.data

import android.location.Location

fun Location.toLocation() = com.aarevalo.trackit.core.domain.location.Location(
    lat = latitude,
    lon = longitude
)

