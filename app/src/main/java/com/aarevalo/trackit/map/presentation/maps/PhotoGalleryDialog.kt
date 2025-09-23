package com.aarevalo.trackit.map.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.aarevalo.trackit.map.domain.location.LocationWithTimestamp

@Composable
fun PhotoGalleryDialog(
    locationWithPhotos: LocationWithTimestamp,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        PhotoGallery(
            photos = locationWithPhotos.listPhotos,
            locationTimestamp = locationWithPhotos.timestamp
        )
    }
}