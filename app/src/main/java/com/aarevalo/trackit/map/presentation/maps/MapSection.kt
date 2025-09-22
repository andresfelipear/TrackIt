package com.aarevalo.trackit.map.presentation.maps

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aarevalo.trackit.map.domain.location.Location
import com.aarevalo.trackit.map.domain.location.LocationWithTimestamp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapSection(
    modifier: Modifier = Modifier,
    currentLocation: Location?,
    isTrackingFinished: Boolean,
    locations: List<List<LocationWithTimestamp>>,
    onAction: (MapScreenAction) -> Unit
) {

    val activity = LocalActivity.current as ComponentActivity

    val cameraPositionState = rememberCameraPositionState()

    val markerPosition = remember(currentLocation){
        LatLng(
            (currentLocation?.lat?.toFloat()?:49.283679).toDouble(),
            (currentLocation?.long?.toFloat()?:123.060599).toDouble(),
        )
    }

    val marker = rememberUpdatedMarkerState(markerPosition)


    LaunchedEffect(currentLocation, isTrackingFinished) {
        if(currentLocation != null && !isTrackingFinished) {
            val latLng = LatLng(
                currentLocation.lat,
                currentLocation.long
            )

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    17f
                )
            )

        }
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        onMapLoaded = {
            Toast.makeText(
                activity,
                "Map loaded",
                Toast.LENGTH_SHORT
            )
                .show()
        },
        properties = MapProperties(
            isTrafficEnabled = false,
            isIndoorEnabled = true,
        )
    ){
        PolylinesSection(
            locations = locations
        )

        MapEffect(){}

        if(!isTrackingFinished && currentLocation != null){
            MarkerComposable(
                state = marker,
                onClick = {
                    onAction(MapScreenAction.GoToCamera)
                    true
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}