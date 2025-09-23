package com.aarevalo.trackit.map.presentation.maps

import com.aarevalo.trackit.map.domain.location.LocationWithTimestamp

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
    data object GoToCamera: MapScreenAction

    data class SelectLocation(val location: LocationWithTimestamp): MapScreenAction
    data object DismissDialogLocation: MapScreenAction
}
