package com.aarevalo.trackit.map.di

import android.content.Context
import com.aarevalo.trackit.map.data.AndroidLocationObserver
import com.aarevalo.trackit.map.domain.location.LocationObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideLocationObserver(
        @ApplicationContext
        context: Context
    ): LocationObserver {
        return AndroidLocationObserver(context)
    }
}