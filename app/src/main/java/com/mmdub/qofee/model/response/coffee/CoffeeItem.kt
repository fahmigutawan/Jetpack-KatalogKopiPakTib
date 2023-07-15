package com.mmdub.qofee.model.response.coffee

data class CoffeeItem(
    val id:String? = "",
    val name:String? = "",
    val description:String? = "",
    val category_id:String? = "",
    val thumbnail:String? = "",
    val prices:List<Int>? = listOf()
)