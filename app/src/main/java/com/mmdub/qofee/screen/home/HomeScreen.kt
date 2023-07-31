package com.mmdub.qofee.screen.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.mmdub.qofee.R
import com.mmdub.qofee.component.CategoryItem
import com.mmdub.qofee.component.CoffeeItem
import com.mmdub.qofee.component.CoffeeItemLoading
import com.mmdub.qofee.component.rectLoadingModifier
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.data.paging_util.PagingState
import com.mmdub.qofee.model.response.category.CategoryItem
import com.mmdub.qofee.ui.theme.AppColor
import com.mmdub.qofee.util.NavRoutes
import com.mmdub.qofee.viewmodel.home.HomeViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val scrWidth = LocalConfiguration.current.screenWidthDp
    val categoryState = viewModel.categoryState.collectAsState()
    val sellerState = viewModel.sellerState.collectAsState()
    val bannerUrls = viewModel.bannerUrls.collectAsState()
    val gridState = rememberLazyGridState()
    val shouldLoadNextItem = remember {
        derivedStateOf {
            viewModel.coffeeItems.isNotEmpty()
                    && viewModel.coffeeItems.size % 6 == 0
                    && !viewModel.endOfPage.value
                    && gridState
                .layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index
                ?.minus(2) == viewModel.coffeeItems.size - 1
        }
    }

    if (viewModel.shouldLoadFirstItem.value) {
        LaunchedEffect(key1 = true) {
            viewModel.loadFirstCoffee()
        }
    }

    if (shouldLoadNextItem.value && !viewModel.endOfPage.value) {
        LaunchedEffect(key1 = true) {
            viewModel.loadNextCoffee()
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

                Column {
                    Box {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = viewModel.pickedSellerString.value,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(text = "Pilih Penjual")
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                focusedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            trailingIcon = {
                                FilledIconButton(
                                    onClick = {
                                        viewModel.showSellerDropdown.value =
                                            !viewModel.showSellerDropdown.value
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = viewModel.showSellerDropdown.value,
                        onDismissRequest = {
                            viewModel.showSellerDropdown.value = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua") },
                            onClick = {
                                viewModel.pickedSellerString.value = "Semua"
                                viewModel.coffeeItems.clear()
                                viewModel.pickedSeller.value = null
                                viewModel.showSellerDropdown.value = false
                                viewModel.shouldLoadFirstItem.value = true
                                viewModel.endOfPage.value = false
                            },
                        )
                        if (sellerState.value is Resource.Success) {
                            sellerState.value.data?.let {
                                it.filterNotNull().forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item.name ?: "...") },
                                        onClick = {
                                            viewModel.coffeeItems.clear()
                                            viewModel.pickedSellerString.value = item.name ?: "..."
                                            viewModel.pickedSeller.value = item
                                            viewModel.showSellerDropdown.value = false
                                            viewModel.shouldLoadFirstItem.value = true
                                            viewModel.endOfPage.value = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                when (bannerUrls.value) {
                    is Resource.Error -> {/*TODO*/
                    }

                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((scrWidth * 7 / 16).dp)
                                .rectLoadingModifier(RoundedCornerShape(12.dp))
                        )
                    }

                    is Resource.Success -> {
                        val state = rememberPagerState {
                            bannerUrls.value.data?.size ?: 0
                        }
                        HorizontalPager(state = state) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((scrWidth * 7 / 16).dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AppColor.Neutral60)
                            ) {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = bannerUrls.value.data?.get(index) ?: "",
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            HorizontalPagerIndicator(
                                pagerState = state,
                                pageCount = bannerUrls.value.data?.size ?: 0,
                                activeColor = MaterialTheme.colorScheme.inverseOnSurface,
                                inactiveColor = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
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
                                picked = viewModel.categoryPicked.value == null,
                                onClick = {
                                    if (viewModel.categoryPicked.value != null) {
                                        viewModel.coffeeItems.clear()
                                        viewModel.categoryPicked.value = null
                                        viewModel.shouldLoadFirstItem.value = true
                                        viewModel.endOfPage.value = false
                                    }
                                }
                            )
                            it.forEach { item ->
                                item?.let { itemNotNull ->
                                    CategoryItem(
                                        word = itemNotNull.word ?: "",
                                        picked = viewModel.categoryPicked.value == itemNotNull,
                                        onClick = {
                                            viewModel.coffeeItems.clear()
                                            viewModel.categoryPicked.value = itemNotNull
                                            viewModel.shouldLoadFirstItem.value = true
                                            viewModel.endOfPage.value = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        items(viewModel.coffeeItems) {
            CoffeeItem(
                thumbnailUrl = it.thumbnail ?: "",
                name = it.name ?: "",
                category = it.category ?: "",
                seller = it.seller ?: "",
                price = it.prices?.getOrNull(0)?.get("price")?.toInt() ?: 0,
                onClick = {
                    navController.navigate("${NavRoutes.COFFEE_DETAIL}/${it.id}")
                }
            )
        }

        if (viewModel.pagingState.value == PagingState.FirstLoad) {
            items(4) {
                CoffeeItemLoading()
            }
        }
    }
}