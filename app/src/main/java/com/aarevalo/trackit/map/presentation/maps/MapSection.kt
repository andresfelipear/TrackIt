package com.aarevalo.trackit.map.presentation.maps

import android.widget.Toast
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
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapSection(
    modifier: Modifier = Modifier,
    currentLocation: Location?,
    isTrackingFinished: Boolean,
    locations: List<List<LocationWithTimestamp>>
) {

    val activity = LocalActivity.current

    val cameraPositionState = rememberCameraPositionState()

    val markerPosition = remember(currentLocation){
        LatLng(
            (currentLocation?.lat?.toFloat()?:0f).toDouble(),
            (currentLocation?.lon?.toFloat()?:0f).toDouble(),
        )
    }

    val marker = rememberUpdatedMarkerState(markerPosition)


    LaunchedEffect(true) {
        if(currentLocation != null && !isTrackingFinished) {
            val latLng = LatLng(
                currentLocation.lat,
                currentLocation.lon
            )

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
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
        ),
        onMapLoaded = {
            Toast.makeText(
                activity,
                "Map loaded",
                Toast.LENGTH_SHORT
            )
                .show()
        }){

        MapEffect(){}

        PolylinesSection(
            location = locations
        )

        MarkerComposable(
            state = marker
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