package com.umc.myapplication.model

data class Product(
    val productId : Int,
    val name : String,
    val resId : Int,
    var isWishList : Boolean = false,
    val price : Int,
    val category : String = "",
    val shortDescription : String = "",
    val description : String = "",
    val colors : Int = 0,
    val options : List<String> = emptyList(),
)

