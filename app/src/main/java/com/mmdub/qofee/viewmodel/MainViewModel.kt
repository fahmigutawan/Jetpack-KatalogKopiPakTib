package com.mmdub.qofee.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.util.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository:Repository
):ViewModel() {
    val showNavbar = mutableStateOf(false)
    val currentRoute = mutableStateOf(NavRoutes.SPLASH_SCREEN.name)
    val snackbarMessage = mutableStateOf("")
    val snackbarActive = mutableStateOf(false)
}