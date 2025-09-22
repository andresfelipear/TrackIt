package com.aarevalo.trackit.map.presentation.maps

sealed interface TrackingIntent {
    data object StartTracking : TrackingIntent
    data object PauseTracking : TrackingIntent
    data object ResumeTracking : TrackingIntent
}
