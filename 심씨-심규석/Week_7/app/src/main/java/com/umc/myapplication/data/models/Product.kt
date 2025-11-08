package com.umc.myapplication.data.models

import com.umc.myapplication.domain.model.UiProduct

data class Product(
    var id : Int = -1,
    val name : String,
    val imageResource : Int,
    var isLiked : Boolean = false,
    val price : Int,
    //추후 int로 바꿀것
    val categoryId : Int = -1,
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
    "categoryId" to categoryId,
    "shortDescription" to shortDescription,
    "description" to description,
    "colors" to colors,
    "options" to options,
    "imageUrl" to imageUrl,
    "imageResourceId" to imageResource
)

fun List<Product>.toUiProducts(categoryMap: Map<Int, Category>): List<UiProduct> =
    map { p ->
        val cat = p.categoryId.let { categoryMap[it] }
        UiProduct(
            id = p.id,
            name = p.name,
            imageResource = p.imageResource,
            isLiked = p.isLiked,
            shortDescription = p.shortDescription,
            description = p.description,
            price = p.price,
            category = cat?.name ?: "no Category",
            colors = p.colors
        )
    }