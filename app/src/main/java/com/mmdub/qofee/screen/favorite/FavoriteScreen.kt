package com.mmdub.qofee.screen.favorite

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mmdub.qofee.component.CoffeeItem
import com.mmdub.qofee.util.NavRoutes
import com.mmdub.qofee.viewmodel.favorite.FavoriteViewModel

@Composable
fun FavoriteScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<FavoriteViewModel>()

    LaunchedEffect(key1 = true) {
        viewModel.getAllFavorites()
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(viewModel.favorites) { item ->
            CoffeeItem(
                thumbnailUrl = item.thumbnail ?: "",
                name = item.name ?: "",
                category = item.category ?: "",
                price = (item.price ?: 0L).toInt(),
                onClick = {
                    navController.navigate("${NavRoutes.COFFEE_DETAIL}/${item.id}")
                }
            )
        }
    }
}