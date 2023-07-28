package com.mmdub.qofee

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mmdub.qofee.component.Navbar
import com.mmdub.qofee.ui.theme.KatalogKopiPakTibTheme
import com.mmdub.qofee.util.NavRoutes
import com.mmdub.qofee.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KatalogKopiPakTibTheme {
                val mainViewModel = viewModel<MainViewModel>()
                val navController = rememberNavController()

                navController.addOnDestinationChangedListener{ _, route, _ ->
                    route.route?.let {
                        mainViewModel.currentRoute.value = it
                        when(it){
                            NavRoutes.HOME_SCREEN.name -> {
                                mainViewModel.showNavbar.value = true
                            }

                            NavRoutes.CATALOGUE_SCREEN.name -> {
                                mainViewModel.showNavbar.value = true
                            }

                            NavRoutes.FAVORITE_SCREEN.name -> {
                                mainViewModel.showNavbar.value = true
                            }

                            NavRoutes.PROFILE_SCREEN.name -> {
                                mainViewModel.showNavbar.value = true
                            }

                            else -> {
                                mainViewModel.showNavbar.value = false
                            }
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if(mainViewModel.showNavbar.value){
                                Navbar(
                                    onItemClicked = { route ->
                                        navController.navigate(route)
                                    },
                                    currentRoute = mainViewModel.currentRoute.value
                                )
                            }
                        }
                    ) {
                        MainNavHost(
                            modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                            navController = navController,
                            mainViewModel = mainViewModel,
                            showSnackbar =
                        )
                    }
                }
            }
        }
    }
}

@HiltAndroidApp
class MainApplication:Application()