package com.mmdub.katalogkopipaktib

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mmdub.katalogkopipaktib.util.NavRoutes
import com.mmdub.katalogkopipaktib.viewmodel.MainViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH_SCREEN.name
    ){

    }
}