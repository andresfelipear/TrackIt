package com.aarevalo.trackit.map.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aarevalo.trackit.map.domain.camera.PhotoHandler
import com.aarevalo.trackit.map.domain.location.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel @Inject constructor(
    private val photoHandler: PhotoHandler,
    private val locationTracker: LocationTracker
): ViewModel() {

    private val _uiState = MutableStateFlow(CameraScreenState())
    val uiState = _uiState.asStateFlow()

    val previewPhoto : StateFlow<ByteArray?> = photoHandler.getCurrentPreviewPhoto()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onAction(action: CameraScreenAction){
        viewModelScope.launch {
            when(action){
                CameraScreenAction.CancelPreview -> cancelPreview()
                CameraScreenAction.SavePicture -> savePhoto()
                is CameraScreenAction.SubmitCameraPermissionInfo -> {
                    _uiState.update {
                        it.copy(
                            showCameraRationale = action.shouldShowCameraRationale,
                            permissionGranted = action.acceptedCameraPermission
                        )
                    }
                }
                is CameraScreenAction.TakenPicture -> processPhoto(action.data)
            }
        }
    }

    private suspend fun savePhoto() {
        val savedFile = photoHandler.savePicturePreview()

        if(savedFile != null){
            associatePhotoWithLatestLocation(savedFile)

            _uiState.update {
                it.copy(
                    isInPreviewMode = false,
                    lastSavedPhoto = savedFile
                )
            }
        }
    }

    private suspend fun associatePhotoWithLatestLocation(photoFile: File) {
        val locationData = locationTracker.locationData.value

        val lastNonEmptySegmentIndex = locationData.locations.indexOfLast { it.isNotEmpty() }

        if(lastNonEmptySegmentIndex == -1) return

        val lastNonEmptySegment = locationData.locations[lastNonEmptySegmentIndex]

        val latestLocation = lastNonEmptySegment.last()

        val updatedLocation = latestLocation.copy(
            listPhotos = latestLocation.listPhotos.plus(photoFile)
        )

        val updatedSegment = lastNonEmptySegment.dropLast(1) + updatedLocation

        val updateSegment = locationData.locations.toMutableList().apply {
            set(lastNonEmptySegmentIndex, updatedSegment)
        }

        locationTracker.updateLocationData(
            locationData.copy(
                locations = updateSegment
            )
        )
    }

    private suspend fun processPhoto(data: ByteArray) {
        photoHandler.onPhotoPreview(photoBytes = data)
        _uiState.update {
            it.copy(
                isInPreviewMode = true
            )
        }
    }

    private suspend fun cancelPreview(){
        photoHandler.onCancelPreview()
        _uiState.update {
            it.copy(
                isInPreviewMode = false
            )
        }

    }
}