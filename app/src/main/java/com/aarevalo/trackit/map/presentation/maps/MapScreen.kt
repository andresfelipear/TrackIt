package com.aarevalo.trackit.map.presentation.maps

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aarevalo.trackit.TrackitService
import com.aarevalo.trackit.core.presentation.dialogs.PermissionRationaleDialogs
import com.aarevalo.trackit.core.presentation.utils.hasLocationPermission
import com.aarevalo.trackit.core.presentation.utils.hasNotificationPermission
import com.aarevalo.trackit.core.presentation.utils.shouldShowLocationRationalePermission
import com.aarevalo.trackit.core.presentation.utils.shouldShowPostNotificationRationalePermission


@Composable
fun MapScreenRoot(
    viewModel: MapScreenViewModel,
    navigateToCameraScreen: () -> Unit
){
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = true){
        viewModel.event.collect{ event ->
            when(event){
                MapScreenEvents.NavigationCamera -> navigateToCameraScreen()
            }
        }
    }

    MapScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@Composable
fun MapScreen(
    onAction: (MapScreenAction) -> Unit,
    state: MapScreenState,
){
    val context = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity

    val permissionLauncherLocationAndNotifications = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->

        val hasLocationPermission = activity.hasLocationPermission()
        val hasNotificationPermission = activity.hasNotificationPermission()

        val showLocationRationale = activity.shouldShowLocationRationalePermission()
        val showNotificationRationale = activity.shouldShowPostNotificationRationalePermission()

        onAction(
            MapScreenAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            MapScreenAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationRationale = showNotificationRationale
            )
        )
    }

    PermissionRationaleDialogs(
        showLocationRationale = state.showLocationRationale,
        showNotificationRationale = state.showNotificationRationale,
        showCameraRationale = false,
        onAccept = {
            permissionLauncherLocationAndNotifications.requestTrackingScreenPermissions(context)
        },
        onDismiss = {
            onAction(
                MapScreenAction.SubmitLocationPermissionInfo(
                    acceptedLocationPermission = context.hasLocationPermission(),
                    showLocationRationale = false
                )
            )
            onAction(
                MapScreenAction.SubmitNotificationPermissionInfo(
                    acceptedNotificationPermission = context.hasNotificationPermission(),
                    showNotificationRationale = false
                )
            )

            if(context.hasLocationPermission()){
                val intent = Intent(activity, TrackitService::class.java).apply {
                    action = TrackitService.ACTION_START_SERVICE
                }

                if(!TrackitService.isServiceActive.value){
                    activity.startService(intent)
                }
            }
        }
    )

    LaunchedEffect(key1 = true){
        val showLocationRationale = activity.shouldShowLocationRationalePermission()
        val showNotificationRationale = activity.shouldShowPostNotificationRationalePermission()

        onAction(
            MapScreenAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            MapScreenAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationRationale = showNotificationRationale
            )
        )

        if(!showLocationRationale && !showNotificationRationale){
            permissionLauncherLocationAndNotifications.requestTrackingScreenPermissions(context)
        }

        if(context.hasLocationPermission()){

            val intent = Intent(activity, TrackitService::class.java).apply {
                action = TrackitService.ACTION_START_SERVICE
            }

            if(!TrackitService.isServiceActive.value){
                activity.startService(intent)
            } else {
                onAction(MapScreenAction.StartTracking)
                onAction(MapScreenAction.ResumeTracking)
            }
        }
    }


    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when{
                        !state.isTracking -> {
                            if(context.hasLocationPermission()){
                                val intent = Intent(activity, TrackitService::class.java).apply {
                                    action = TrackitService.ACTION_START_SERVICE
                                }
                                if(!TrackitService.isServiceActive.value){
                                    activity.startService(intent)
                                }
                                onAction(MapScreenAction.StartTracking)
                            }
                        }
                        state.isPaused -> onAction(MapScreenAction.ResumeTracking)
                        else -> onAction(MapScreenAction.PauseTracking)
                    }
                }
            ){
                if(state.isPaused){
                    Image(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
                else{
                    Image(
                        imageVector = Icons.Default.Pause,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MapSection(
                currentLocation = state.location,
                isTrackingFinished = false,
                locations = state.trackingLocations,
                modifier = Modifier.fillMaxSize(),
                onAction = onAction,
                selectedLocation = state.selectedLocation
            )
        }
    }
}

private fun ActivityResultLauncher<Array<String>>.requestTrackingScreenPermissions(
    context: Context,
){
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val notificationPermissions = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    when{
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermissions)
        }
        !hasLocationPermission -> {
            launch(locationPermissions)
        }
        !hasNotificationPermission -> {
            launch(notificationPermissions)
        }
    }
}