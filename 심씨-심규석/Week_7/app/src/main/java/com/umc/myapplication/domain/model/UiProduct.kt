package com.umc.myapplication.domain.model

data class UiProduct(
    var id : Int = -1,
    val name : String,
    val imageResource : Int,
    val imageUrl : String? = null,
    //추후 추가
    var isLiked : Boolean = false,
    val price : Int,
    val category : String = "",
    val shortDescription : String = "",
    val description : String = "",
    val colors : Int = 0,
    val options : List<String> = emptyList(),
)
