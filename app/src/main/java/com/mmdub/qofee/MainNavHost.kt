package com.mmdub.qofee

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mmdub.qofee.screen.coffee_detail.CoffeeDetailScreen
import com.mmdub.qofee.screen.home.HomeScreen
import com.mmdub.qofee.screen.profile.ProfileScreen
import com.mmdub.qofee.screen.search.SearchScreen
import com.mmdub.qofee.screen.splash.SplashScreen
import com.mmdub.qofee.util.NavRoutes
import com.mmdub.qofee.viewmodel.MainViewModel

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    showSnackbar:(String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoutes.SPLASH_SCREEN.name
    ){
        composable(NavRoutes.SPLASH_SCREEN.name){
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.HOME_SCREEN.name){
            HomeScreen(navController = navController)
        }

        composable(NavRoutes.CATALOGUE_SCREEN.name){

        }

        composable(NavRoutes.FAVORITE_SCREEN.name){

        }

        composable(NavRoutes.PROFILE_SCREEN.name){
            ProfileScreen(navController = navController)
        }

        composable(
            route = "${NavRoutes.SEARCH_SCREEN.name}/{keyword}",
            arguments = listOf(
                navArgument("keyword"){
                    type = NavType.StringType
                }
            )
        ){
            val keyword = it.arguments?.getString("keyword") ?: ""
            SearchScreen(
                keyword = keyword,
                navController = navController
            )
        }

        composable(
            route = "${NavRoutes.COFFEE_DETAIL.name}/{coffeeId}",
            arguments = listOf(
                navArgument("coffeeId"){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString("coffeeId") ?: ""
            CoffeeDetailScreen(
                navController = navController,
                coffeeId = id,
                showSnackbar = showSnackbar
            )
        }
    }
}