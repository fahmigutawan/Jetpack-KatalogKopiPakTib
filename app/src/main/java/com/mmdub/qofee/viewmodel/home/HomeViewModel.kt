package com.mmdub.qofee.viewmodel.home

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.data.paging_util.PagingState
import com.mmdub.qofee.model.response.category.CategoryItem
import com.mmdub.qofee.model.response.coffee.CoffeeItem
import com.mmdub.qofee.model.response.seller.SellerResponse
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
    val sellerState = MutableStateFlow<Resource<List<SellerResponse?>>>(Resource.Loading())
    val categoryPicked = mutableStateOf<CategoryItem?>(null)
    val showSellerDropdown = mutableStateOf(false)
    val pickedSeller = mutableStateOf<SellerResponse?>(null)
    val shouldLoadFirstItem = mutableStateOf(true)
    val endOfPage = mutableStateOf(false)
    val pagingState = mutableStateOf(PagingState.FirstLoad)

    val coffeeItems = mutableStateListOf<CoffeeItem>()

    val bannerUrls = MutableStateFlow<Resource<List<String>>>(Resource.Loading())

    fun loadFirstCoffee() {
        viewModelScope.launch {
            repository.getFirstCoffee(
                category_id = categoryPicked.value?.id ?: ""
            ).collect {
                when (it) {
                    is Resource.Error -> {
                        pagingState.value = PagingState.FirstLoadError
                    }

                    is Resource.Loading -> {
                        pagingState.value = PagingState.FirstLoad
                    }

                    is Resource.Success -> {
                        it.data?.let { list ->
                            endOfPage.value = list.isEmpty() || list.size < 6
                            coffeeItems.addAll(list)
                        }
                        pagingState.value = PagingState.Success
                        shouldLoadFirstItem.value = false
                    }
                }
            }
        }
    }

    fun loadNextCoffee() {
        viewModelScope.launch {
            repository.getNextCoffee(
                lastId = coffeeItems.last().id ?: "",
                category_id = categoryPicked.value?.id ?: ""
            ).collect {
                when (it) {
                    is Resource.Error -> {
                        pagingState.value = PagingState.NextLoadError
                    }

                    is Resource.Loading -> {
                        pagingState.value = PagingState.NextLoad
                    }

                    is Resource.Success -> {
                        it.data?.let { list ->
                            endOfPage.value = list.isEmpty() || list.size < 6
                            coffeeItems.addAll(list)
                        }
                        pagingState.value = PagingState.Success
                        shouldLoadFirstItem.value = false
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getAllCategory().collect {
                categoryState.value = it
            }
        }

        viewModelScope.launch {
            repository.getAllBannerUrl().collect {
                bannerUrls.value = it
            }
        }

        viewModelScope.launch {
            repository.getAllSeller().collect{
                sellerState.value = it
            }
        }
    }
}