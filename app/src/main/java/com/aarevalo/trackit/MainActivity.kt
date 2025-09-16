package com.aarevalo.trackit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aarevalo.trackit.core.domain.timer.Timer
import com.aarevalo.trackit.map.presentation.mapSection.MapSection
import com.aarevalo.trackit.ui.theme.TrackItTheme
import com.google.maps.android.compose.GoogleMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            Timer.timeAndEmits().collect {
                Log.d("FlowTIMER", "Timer interval: $it")
            }
        }

        Timer.timeAndEmits().onEach {
            Log.d("FlowTimerLaunchIn", "Timer interval: $it")
        }.launchIn(lifecycleScope)

        Timer.timeAndEmits()
            .scan(Duration.ZERO){
                duration, value -> duration + value
            }.zip(
                Timer.randomFlow()
            ){ time, random ->
                time to random
            }.onEach {
                Log.d("ZipFlow", "value: ${it.first}, random: ${it.second}")
            }.launchIn(lifecycleScope)

        setContent {
            val navController = rememberNavController()
            TrackItTheme {
                Box (
                    modifier = Modifier.fillMaxSize()
                ){
                    NavHost(
                        navController = navController,
                        startDestination = MapScreenDes
                    ){
                        composable<MapScreenDes> (){
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