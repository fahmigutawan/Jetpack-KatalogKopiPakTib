package com.mmdub.qofee.screen.coffee_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.viewmodel.coffee_detail.CoffeeDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailScreen(
    navController: NavController,
    coffeeId: String
) {
    val viewModel = hiltViewModel<CoffeeDetailViewModel>()
    val coffeeItem = viewModel.coffeeItem.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getCoffeeData(coffeeId)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {

                    }
                }
            }
        }
    ) {
        LazyColumn {
            when (coffeeItem.value) {
                is Resource.Error -> {/*TODO*/
                }

                is Resource.Loading -> {/*TODO*/
                }

                is Resource.Success -> {
                    coffeeItem.value.data?.let {item -> 
                        item {
                            Column {
                                Text(text = "Ukuran")
                                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                    item.prices?.let { 
                                        it.forEach {  
                                            Text(text = "${it.get("weight")} gr")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}