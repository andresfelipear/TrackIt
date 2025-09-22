package com.aarevalo.trackit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aarevalo.trackit.map.domain.location.LocationTracker
import com.aarevalo.trackit.map.presentation.maps.MapSection
import com.aarevalo.trackit.ui.theme.TrackItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var locationTracker: LocationTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationTracker.locationData.onEach {
                Log.d(
                    "LocationData",
                    it.toString()
                )
            }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            kotlinx.coroutines.delay(2000)
            locationTracker.startObservingLocation()
            locationTracker.setIsTracking(true)
            kotlinx.coroutines.delay(5000)
            locationTracker.finishTracking()
        }

        setContent {
            val navController = rememberNavController()
            TrackItTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MapScreenDes
                    ) {
                        composable<MapScreenDes>() {
                            MapSection(
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable<CameraScreenDes> {

                        }
                    }
                }

            }
        }
    }
}

@Serializable
data object MapScreenDes

@Serializable
data object CameraScreenDes