package com.mmdub.qofee.viewmodel.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.model.response.category.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val searchState = mutableStateOf("")
    val categoryState = MutableStateFlow<Resource<List<CategoryItem?>>>(Resource.Loading())
    val categoryPicked = mutableStateOf<CategoryItem?>(null)

    init {
        viewModelScope.launch {
            repository.getAllCategory().collect{
                categoryState.value = it
            }
        }
    }
}