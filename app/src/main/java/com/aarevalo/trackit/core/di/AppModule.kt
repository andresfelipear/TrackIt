package com.aarevalo.trackit.core.di

import android.content.Context
import androidx.compose.ui.tooling.preview.Preview
import com.aarevalo.trackit.TrackitApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppScope(
        @ApplicationContext context: Context
    ): CoroutineScope {
        return (context as TrackitApplication).applicationScope
    }
}