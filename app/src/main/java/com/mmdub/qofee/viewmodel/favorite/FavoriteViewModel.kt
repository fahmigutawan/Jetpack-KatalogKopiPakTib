package com.mmdub.qofee.viewmodel.favorite

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.model.entity.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val favorites = mutableStateListOf<FavoriteEntity>()

    fun getAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            favorites.clear()
            favorites.addAll(repository.getAllFavorite())
        }
    }
}