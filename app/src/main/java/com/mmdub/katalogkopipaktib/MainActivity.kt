package com.mmdub.katalogkopipaktib

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mmdub.katalogkopipaktib.ui.theme.KatalogKopiPakTibTheme
import com.mmdub.katalogkopipaktib.viewmodel.MainViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KatalogKopiPakTibTheme {
                val mainViewModel = viewModel<MainViewModel>()
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost(
                        navController = navController,
                        mainViewModel = mainViewModel
                    )
                }
            }
        }
    }
}

class MainApplication:Application()