package com.mmdub.qofee.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mmdub.qofee.util.NavRoutes

enum class NavbarItem(
    val route: String,
    val word: String,
    val icon: ImageVector
) {
    Home(
        NavRoutes.HOME_SCREEN.name,
        "Beranda",
        Icons.Default.Home
    ),
//    Catalogue(
//        NavRoutes.CATALOGUE_SCREEN.name,
//        "Katalog",
//        Icons.Default.Book
//    ),
    Favorite(
        NavRoutes.FAVORITE_SCREEN.name,
        "Favorit",
        Icons.Default.Favorite
    ),
    Profile(
        NavRoutes.PROFILE_SCREEN.name,
        "Profil",
        Icons.Default.Person
    )
}

@Composable
fun Navbar(
    onItemClicked: (route: String) -> Unit,
    currentRoute: String
) {
    val containerWidth = remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    BottomAppBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    density.run {
                        containerWidth.value = it.width.toDp()
                    }
                }
        ) {
            NavbarItem.values().forEach { item ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(containerWidth.value / NavbarItem.values().size)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = rememberRipple(
                                color = Color.Black
                            ),
                            onClick = {
                                onItemClicked(item.route)
                            }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Int.MAX_VALUE.dp))
                            .background(
                                color = if (currentRoute == item.route) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = item.icon,
                            contentDescription = "",
                            tint = if (currentRoute == item.route) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = item.word,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (currentRoute == item.route) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}