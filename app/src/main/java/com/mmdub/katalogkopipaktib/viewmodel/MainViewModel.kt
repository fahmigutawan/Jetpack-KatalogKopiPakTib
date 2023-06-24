package com.mmdub.katalogkopipaktib.viewmodel

import androidx.lifecycle.ViewModel
import com.mmdub.katalogkopipaktib.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository:Repository
):ViewModel() {
}