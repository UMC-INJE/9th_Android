package com.umc.myapplication.data.models

data class Product(
    var id : Int = -1,
    val name : String,
    val imageResource : Int,
    var isLiked : Boolean = false,
    val price : Int,
    val category : String = "",
    val shortDescription : String = "",
    val description : String = "",
    val colors : Int = 0,
    val options : List<String> = emptyList(),
)

fun Product.toMap(imageUrl: String? = null): Map<String, Any?> = mapOf(
    "id" to id,
    "name" to name,
    "isLiked" to isLiked,
    "price" to price,
    "category" to category,
    "shortDescription" to shortDescription,
    "description" to description,
    "colors" to colors,
    "options" to options,
    "imageUrl" to imageUrl,
    "imageResourceId" to imageResource
)