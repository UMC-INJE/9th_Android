package com.umc.myapplication.model

data class SearchItem (
    val productId : Int,
    val resId : Int,
    val name : String,
    var isWishList : Boolean,
    val description : String,
    val colors : Int,
    val price : Int,
)