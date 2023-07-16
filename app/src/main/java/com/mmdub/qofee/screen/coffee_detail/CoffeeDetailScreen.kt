package com.mmdub.qofee.screen.coffee_detail

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mmdub.qofee.viewmodel.coffee_detail.CoffeeDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailScreen(
    navController: NavController,
    coffeeId:String
) {
    val viewModel = hiltViewModel<CoffeeDetailViewModel>()
    val coffeeItem = viewModel.coffeeItem.collectAsState()

    LaunchedEffect(key1 = true){
        viewModel.getCoffeeData(coffeeId)
    }

    Scaffold(bottomBar = {}) {

    }
}