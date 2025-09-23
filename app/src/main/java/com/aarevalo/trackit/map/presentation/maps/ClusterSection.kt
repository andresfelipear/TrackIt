package com.aarevalo.trackit.map.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.aarevalo.trackit.map.domain.location.LocationWithTimestamp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState
import kotlin.collections.flatten

@Composable
fun ClusterSection(
    modifier: Modifier = Modifier,
    locations: List<List<LocationWithTimestamp>>,
    onAction:(MapScreenAction) -> Unit
) {
    val listWithPictures = remember (locations){
        locations.flatten().filter { it.listPhotos.isNotEmpty() }
    }

    listWithPictures.forEach { locationWithTimestamp ->
        val latLng = LatLng(
            locationWithTimestamp.location.lat,
            locationWithTimestamp.location.long
        )

        val photoMarkerState = rememberMarkerState(
            position = latLng
        )

        MarkerComposable (
            state = photoMarkerState,
            onClick = {
                onAction(MapScreenAction.PauseTracking)
                onAction(MapScreenAction.SelectLocation(locationWithTimestamp))
                true
            }
        ){

            PhotoCluster(
                locationWithPhotos = locationWithTimestamp,
            )
        }
    }
}