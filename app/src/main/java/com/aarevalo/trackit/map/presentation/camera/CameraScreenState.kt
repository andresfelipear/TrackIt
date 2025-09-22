package com.aarevalo.trackit.map.presentation.camera

import java.io.File

data class CameraScreenState(
    val isInPreviewMode: Boolean = false,
    val lastSavedPhoto: File? = null,
    val showCameraRationale: Boolean = false,
    val permissionGranted: Boolean = false,
)
