package com.umc.myapplication.data.models

import com.umc.myapplication.domain.model.UiProduct

data class Product(
    var id: Int = -1,
    var name: String = "",
    var imageResource: Int = 0,
    var price: Int = 0,
    var categoryId: Int = -1,
    var shortDescription: String = "",
    var description: String = "",
    var colors: Int = 0,
    var options: List<String> = emptyList()
)


fun List<Product>.toUiProducts(
    categoryMap: Map<Int, Category>,
    userLikedProductIds: Set<Int>
): List<UiProduct> = map { p ->
    val cat = categoryMap[p.categoryId]
    UiProduct(
        id = p.id,
        name = p.name,
        imageResource = p.imageResource,
        liked = userLikedProductIds.contains(p.id), // liked 판단 추가
        shortDescription = p.shortDescription,
        description = p.description,
        price = p.price,
        category = cat?.name ?: "no Category",
        colors = p.colors
    )
}