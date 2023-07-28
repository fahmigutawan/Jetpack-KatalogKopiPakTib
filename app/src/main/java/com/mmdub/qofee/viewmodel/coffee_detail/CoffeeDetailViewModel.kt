package com.mmdub.qofee.viewmodel.coffee_detail

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.model.entity.FavoriteEntity
import com.mmdub.qofee.model.response.coffee.CoffeeItem
import com.mmdub.qofee.model.response.seller.SellerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeeDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val coffeeItem = MutableStateFlow<Resource<CoffeeItem>>(Resource.Loading())
    val pickedVariantIndex = mutableStateOf<Int?>(null)
    val sellerDetail = MutableStateFlow<Resource<SellerResponse?>>(Resource.Loading())
    val favorite = mutableStateListOf<FavoriteEntity>()

    fun getCoffeeData(id: String) = viewModelScope.launch {
        repository.getCoffeeByCoffeeId(id).collect {
            coffeeItem.value = it
        }
    }

    fun getSellerDetail(uid: String) = viewModelScope.launch {
        repository.getSellerDetail(uid).collect {
            sellerDetail.value = it
        }
    }

    fun getFavoriteById(id: String) {
        favorite.clear()
        viewModelScope.launch(Dispatchers.IO) {
            favorite.addAll(repository.getFavoriteById(id))
        }
    }

    fun insertFavorite(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertFavorite(favoriteEntity) }

    fun deleteFavorite(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteFavorite(favoriteEntity) }
}