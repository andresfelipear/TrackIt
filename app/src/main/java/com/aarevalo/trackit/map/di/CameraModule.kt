package com.aarevalo.trackit.map.di

import android.content.Context
import com.aarevalo.trackit.map.data.camera.PhotoHandlerImpl
import com.aarevalo.trackit.map.domain.camera.PhotoHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CameraModule {
    @Provides
    @Singleton
    fun providePhotoHandler(
        @ApplicationContext context: Context
    ): PhotoHandler {
        return PhotoHandlerImpl(context)
    }

}