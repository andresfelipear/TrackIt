package com.aarevalo.trackit.map.data

import android.location.Location

fun Location.toLocation() = com.aarevalo.trackit.map.domain.location.Location(
    lat = latitude,
    long = longitude
)

