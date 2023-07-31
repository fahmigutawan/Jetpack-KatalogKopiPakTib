package com.mmdub.qofee.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Tentang Aplikasi")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                    }
                }
            )
        }
    ) {
        Card(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Qofee (for Admin) v1.0.0",
                    style = MaterialTheme.typography.titleMedium
                )

                Column {
                    Text(
                        text = "Developed by",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Fahmi Noordin Rumagutawan",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "081553993193",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column {
                    Text(
                        text = "Designed by",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Ferdiansah Dwika Permana",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "085768388130",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}