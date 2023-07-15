package com.mmdub.qofee.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mmdub.qofee.R
import com.mmdub.qofee.component.CategoryItem
import com.mmdub.qofee.component.CoffeeItem
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.model.response.category.CategoryItem
import com.mmdub.qofee.ui.theme.AppColor
import com.mmdub.qofee.util.NavRoutes
import com.mmdub.qofee.viewmodel.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val scrWidth = LocalConfiguration.current.screenWidthDp
    val categoryState = viewModel.categoryState.collectAsState()
    val defaultCategory = CategoryItem(
        id = "Semua",
        word = "Semua"
    )
    val gridState = rememberLazyGridState()
    val shouldLoadNextItem = remember {
        derivedStateOf {
            viewModel.coffeeItems.isNotEmpty()
                    && viewModel.coffeeItems.size % 6 == 0
                    && gridState
                .layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index
                ?.minus(2) == viewModel.coffeeItems.size - 1
        }
    }

    LaunchedEffect(key1 = categoryState.value) {
        if (categoryState.value is Resource.Success) {
            categoryState.value.data?.let {
                if (it.size > 0) {
                    viewModel.categoryPicked.value = defaultCategory
                }
            }
        }
    }

    if (viewModel.shouldLoadFirstItem.value) {
        when (viewModel.categoryPicked.value?.word) {
            "Semua" -> {
                LaunchedEffect(key1 = true) {
                    viewModel.loadAllFirstCoffee()
                }
            }

            else -> {
                LaunchedEffect(key1 = true) {
                    //Load by Category
                }
            }
        }
    }

    if(shouldLoadNextItem.value && !viewModel.endOfPage.value){
        LaunchedEffect(key1 = true){
            viewModel.loadAllNextCoffee()
        }
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        columns = GridCells.Fixed(2),
        state = gridState
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Selamat Datang!",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )

                    AsyncImage(
                        modifier = Modifier.size(42.dp),
                        model = R.drawable.ic_profile,
                        contentDescription = ""
                    )
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.searchState.value,
                    onValueChange = {
                        viewModel.searchState.value = it
                    },
                    placeholder = {
                        Text(text = "Cari produk kopi...")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    },
                    trailingIcon = {
                        FilledIconButton(
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            onClick = {
                                if (viewModel.searchState.value.isNotEmpty()) {
                                    navController.navigate("${NavRoutes.SEARCH_SCREEN.name}/${viewModel.searchState.value}")
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.NavigateNext, contentDescription = "")
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.background
                    )
                )

                //Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((scrWidth * 7 / 16).dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColor.Primary95)
                )
            }
        }

        item(
            span = { GridItemSpan(2) }
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (categoryState.value) {
                    is Resource.Error -> {/*TODO*/
                    }

                    is Resource.Loading -> {/*TODO*/
                    }

                    is Resource.Success -> {
                        categoryState.value.data?.let {
                            CategoryItem(
                                word = "Semua",
                                picked = viewModel.categoryPicked.value == defaultCategory,
                                onClick = {
                                    if (viewModel.categoryPicked.value != defaultCategory) {
                                        viewModel.coffeeItems.clear()
                                    }
                                    viewModel.categoryPicked.value = defaultCategory
                                }
                            )
                            it.forEach { item ->
                                item?.let { itemNotNull ->
                                    CategoryItem(
                                        word = itemNotNull.word ?: "",
                                        picked = viewModel.categoryPicked.value == itemNotNull,
                                        onClick = {
                                            if (viewModel.categoryPicked.value != item) {
                                                viewModel.coffeeItems.clear()
                                            }
                                            viewModel.categoryPicked.value = itemNotNull
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        items(viewModel.coffeeItems){
            CoffeeItem(
                thumbnailUrl = "",
                name = it.name ?: "",
                category = it.category_id ?: "",
                price = it.prices?.getOrNull(0) ?: 0
            )
        }
    }
}