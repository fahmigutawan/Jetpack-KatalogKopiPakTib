package com.mmdub.qofee.screen.coffee_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.model.entity.FavoriteEntity
import com.mmdub.qofee.viewmodel.coffee_detail.CoffeeDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailScreen(
    navController: NavController,
    coffeeId: String,
    showSnackbar: (String) -> Unit
) {
    val viewModel = hiltViewModel<CoffeeDetailViewModel>()
    val coffeeItem = viewModel.coffeeItem.collectAsState()
    val sellerDetail = viewModel.sellerDetail.collectAsState()
    val imgWidth = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val context = LocalContext.current
    val isFavorite = remember {
        derivedStateOf {
            viewModel.favorite.isNotEmpty()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.getCoffeeData(coffeeId)
    }

    LaunchedEffect(key1 = coffeeItem.value) {
        if (coffeeItem.value is Resource.Success) {
            coffeeItem.value.data?.let {
                viewModel.getSellerDetail(it.admin_uid ?: "")
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.getFavoriteById(coffeeId)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Harga", style = MaterialTheme.typography.titleMedium)

                        if (viewModel.pickedVariantIndex.value != null) {
                            when (coffeeItem.value) {
                                is Resource.Error -> {}
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    coffeeItem.value.data?.let {
                                        Text(
                                            text = "Rp ${
                                                (it.prices?.get(viewModel.pickedVariantIndex.value ?: 0)
                                                    ?.get("price")
                                                    ?: 0L)
                                            }", style = MaterialTheme.typography.headlineSmall
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(text = "Rp -", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                    Button(
                        modifier = Modifier.fillMaxHeight(),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            if (sellerDetail.value is Resource.Success) {
                                sellerDetail.value.data?.let {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(
                                                "https://api.whatsapp.com/send?phone=${it.whatsapp}&text=Halo Pak/Bu, kopi dengan nama ${coffeeItem.value.data?.name} pada varian ${
                                                    coffeeItem.value.data?.prices?.get(
                                                        viewModel.pickedVariantIndex.value ?: 0
                                                    )?.get("weight")
                                                } gram, Apakah ready dan bisa dipesan ya?"
                                            )
                                        )
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        enabled = viewModel.pickedVariantIndex.value != null
                    ) {
                        Text(text = "Beli Sekarang", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Detail Kopi")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding() + 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (coffeeItem.value) {
                is Resource.Error -> {/*TODO*/
                }

                is Resource.Loading -> {/*TODO*/
                }

                is Resource.Success -> {
                    coffeeItem.value.data?.let { item ->
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .onSizeChanged {
                                        density.run {
                                            imgWidth.value = it.width.toDp()
                                        }
                                    }
                                    .height(imgWidth.value * 9 / 16)
                            ) {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = item.thumbnail ?: "",
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        item {
                            Column {
                                Text(
                                    text = item.name ?: "",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = item.seller ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = item.category ?: "",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        item.prices?.let { prices ->
                                            if ((prices.size) <= 1) {
                                                Text(
                                                    text = "Rp ${prices[0]["price"] ?: 0L}",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            } else {
                                                val tmp = prices.map {
                                                    it["price"] ?: 0L
                                                }.sorted()
                                                Text(
                                                    text = "Rp ${tmp.first()} - Rp ${tmp.last()}",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            }
                                        }
                                    }

                                    FilledIconButton(
                                        onClick = {
                                            if (isFavorite.value) {
                                                viewModel.deleteFavorite(viewModel.favorite.first())
                                                viewModel.favorite.clear()
                                                showSnackbar("Berhasil dihapus dari favorit")
                                            } else {
                                                val ent = FavoriteEntity(
                                                    id = item.id ?: "",
                                                    name = item.name ?: "",
                                                    category = item.category ?: "",
                                                    thumbnail = item.thumbnail ?: "",
                                                    price = item.prices?.first()?.get("price")
                                                        ?: 0L,
                                                    seller = item.seller ?: ""
                                                )
                                                viewModel.insertFavorite(ent)
                                                viewModel.favorite.add(ent)
                                                showSnackbar("Berhasil ditambahkan ke favorit")
                                            }
                                        },
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = if (isFavorite.value) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = if (isFavorite.value) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Column {
                                Text(
                                    text = "Deskripsi",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = item.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        item {
                            Column {
                                Text(text = "Ukuran", style = MaterialTheme.typography.titleLarge)
                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    item.prices?.let {
                                        it.forEachIndexed { index, map ->
                                            OutlinedCard(
                                                shape = RoundedCornerShape(8.dp),
                                                colors = CardDefaults.outlinedCardColors(
                                                    containerColor = when (index) {
                                                        (viewModel.pickedVariantIndex.value
                                                            ?: Int.MAX_VALUE) -> MaterialTheme.colorScheme.secondaryContainer

                                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                                    },
                                                    contentColor = when (index) {
                                                        (viewModel.pickedVariantIndex.value
                                                            ?: Int.MAX_VALUE) -> MaterialTheme.colorScheme.onSecondaryContainer

                                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                    }
                                                ),
                                                border = BorderStroke(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.primary
                                                ),
                                                onClick = {
                                                    viewModel.pickedVariantIndex.value = index
                                                }
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(8.dp),
                                                    text = "${map.get("weight")} gr"
                                                )
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
}