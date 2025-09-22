package com.aarevalo.trackit.map.presentation.maps

sealed interface MapScreenAction {

    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ): MapScreenAction

    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationRationale: Boolean
    ): MapScreenAction


    data object StartTracking : MapScreenAction
    data object PauseTracking : MapScreenAction
    data object ResumeTracking : MapScreenAction
}
