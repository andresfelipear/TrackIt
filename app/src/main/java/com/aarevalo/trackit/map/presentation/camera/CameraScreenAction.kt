package com.aarevalo.trackit.map.presentation.camera

sealed interface CameraScreenAction {

    data class TakenPicture(val data:ByteArray) : CameraScreenAction {
        override fun equals(other: Any?): Boolean {
            if(this === other) return true
            if(javaClass != other?.javaClass) return false

            other as TakenPicture

            if(!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val shouldShowCameraRationale: Boolean
    ): CameraScreenAction

    data object SavePicture : CameraScreenAction
    data object CancelPreview : CameraScreenAction
}