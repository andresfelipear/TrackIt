package com.aarevalo.trackit.map.domain.camera

import kotlinx.coroutines.flow.Flow
import java.io.File

interface PhotoHandler {
    suspend fun onPhotoPreview(photoBytes:ByteArray)
    fun getCurrentPreviewPhoto():Flow<ByteArray?>

    suspend fun savePicturePreview(): File?
    suspend fun onCancelPreview()

    fun getPhotos(): Flow<List<File>>
    suspend fun clearPhotos()
}