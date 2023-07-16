package com.mmdub.qofee.viewmodel.coffee_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmdub.qofee.data.Repository
import com.mmdub.qofee.data.firebase.Resource
import com.mmdub.qofee.model.response.coffee.CoffeeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoffeeDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val coffeeItem = MutableStateFlow<Resource<CoffeeItem>>(Resource.Loading())

    fun getCoffeeData(id:String) = viewModelScope.launch {
        repository.getCoffeeByCoffeeId(id).collect{
            coffeeItem.value = it
        }
    }
}