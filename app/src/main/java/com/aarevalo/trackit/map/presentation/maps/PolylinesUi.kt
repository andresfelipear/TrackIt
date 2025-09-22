package com.aarevalo.trackit.map.presentation.maps

import androidx.compose.ui.graphics.Color
import com.aarevalo.trackit.map.domain.location.Location

data class PolylinesUi(
    val location1: Location,
    val location2: Location,
    val color: Color
)