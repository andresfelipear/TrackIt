package com.aarevalo.trackit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aarevalo.trackit.map.domain.location.LocationTracker
import com.aarevalo.trackit.map.presentation.camera.CameraScreenRoot
import com.aarevalo.trackit.map.presentation.camera.CameraScreenViewModel
import com.aarevalo.trackit.map.presentation.maps.MapScreenRoot
import com.aarevalo.trackit.map.presentation.maps.MapScreenViewModel
import com.aarevalo.trackit.ui.theme.TrackItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var locationTracker: LocationTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
                            val viewModel = hiltViewModel<MapScreenViewModel>()

                            MapScreenRoot(
                                viewModel = viewModel,
                                navigateToCameraScreen = {
                                    navController.navigate(CameraScreenDes)
                                }
                            )
                        }

                        composable<CameraScreenDes> {
                            val viewModel = hiltViewModel<CameraScreenViewModel>()
                            CameraScreenRoot(
                                viewModel = viewModel
                            )
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