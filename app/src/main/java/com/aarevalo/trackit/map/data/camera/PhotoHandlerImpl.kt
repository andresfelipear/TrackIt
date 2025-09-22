package com.aarevalo.trackit.map.data.camera

import android.content.Context
import com.aarevalo.trackit.map.domain.camera.PhotoHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PhotoHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PhotoHandler {
    private val photoDirectory: File by lazy {
        File(
            context.filesDir,
            "photos"
        ).apply {
            if(!exists()) {
                mkdirs()
            }
        }
    }

    private val _currentPreviewPhoto = MutableStateFlow<ByteArray?>(null)
    private val _photos = MutableStateFlow<List<File>>(emptyList())

    init {
        loadSavedPhotos()
    }

    private fun loadSavedPhotos() {
        if(photoDirectory.exists()) {
            _photos.value = photoDirectory.listFiles()
                ?.filter {
                    it.isFile && it.extension.lowercase() in listOf(
                        "jpg",
                        "jpeg",
                        "png"
                    )
                } ?.sortedByDescending{
                    it.lastModified()
                } ?: emptyList()
        }
    }

    override suspend fun onPhotoPreview(photoBytes: ByteArray) {
        _currentPreviewPhoto.value = photoBytes
    }

    override fun getCurrentPreviewPhoto(): Flow<ByteArray?> = _currentPreviewPhoto.asStateFlow()

    override suspend fun savePicturePreview(): File? = withContext(Dispatchers.IO) {
        val photoBytes = _currentPreviewPhoto.value ?: return@withContext null

        try {

            val timestamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())

            val photoFile = File(
                photoDirectory,
                "photo_${System.currentTimeMillis()}.jpg"
            )

            FileOutputStream(photoFile).use { outputStream ->
                outputStream.write(photoBytes)
                outputStream.flush()
            }

            loadSavedPhotos()

            _currentPreviewPhoto.value = null

            photoFile
        } catch(e:Exception){
            ensureActive()
            e.printStackTrace()
            null
        }
    }

    override suspend fun onCancelPreview() {
        _currentPreviewPhoto.value = null
    }

    override fun getPhotos(): Flow<List<File>> = _photos.asStateFlow()

    override suspend fun clearPhotos() = withContext(Dispatchers.IO){
        photoDirectory.listFiles()?.forEach {
            it.delete()
        }
        loadSavedPhotos()
    }
}