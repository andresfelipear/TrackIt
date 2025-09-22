package com.aarevalo.trackit.map.presentation.maps

sealed interface TrackingIntent {

    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ): TrackingIntent

    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationRationale: Boolean
    ): TrackingIntent


    data object StartTracking : TrackingIntent
    data object PauseTracking : TrackingIntent
    data object ResumeTracking : TrackingIntent
}
