package com.umc.myapplication.model

data class ProductDetail (
    val productId : Int,
    val name : String,
    val resId : Int,
    var isWishList : Boolean,
    val price : Int,

    val category : String,
    val description : String,
    val options : List<String>,
)