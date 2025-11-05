package com.umc.myapplication.data.mock

import com.umc.myapplication.R
import com.umc.myapplication.data.models.Product

object testProductRepository {
    val products = listOf(
        Product(
            id = "1",
            imageResource = R.drawable.img_search_item1,
            name = "Nike Everyday Plus Cushioned",
            isLiked = true,
            shortDescription = "Training Ankle Socks (6 Pairs)",
            description = "The Nike Everyday Plus Cushioned Socks bring comfort to your workout with extra cushioning under the heel and forefoot and a snug, supportive arch band. Sweat-wicking power and breathability up top help keep your feet dry and cool to help push you through that extra set.",
            price = 10,
            category = "Training Crew Socks",
            colors = 6,
            options = listOf(
                "Shown: Multi-Color",
                "Style: SX6897-965"
            ),
        ),
        Product(
            id = "2",
            imageResource = R.drawable.img_search_item1,
            name = "Nike Everyday Plus Cushioned",
            isLiked = false,
            shortDescription = "Training Ankle Socks (6 Pairs)",
            description = "Designed for high-intensity training, these socks offer cushioning and breathability for all-day comfort. The moisture-wicking fabric keeps your feet dry and prevents odor buildup.",
            colors = 3,
            price = 300,
            category = "Performance Ankle Socks",
            options = listOf(
                "Shown: Black/White",
                "Fabric: Cotton blend",
                "Style: SX6900-010"
            ),
        ),
        Product(
            id = "3",
            imageResource = R.drawable.img_home_new_product1,
            name = "Air Jordan XXXVI",
            isLiked = false,
            shortDescription = "Lightweight Basketball Shoes",
            description = "Inspired by the legacy of flight, Air Jordan XXXVI features ultra-lightweight material and responsive cushioning for explosive plays on the court. The multidirectional traction pattern provides superior grip.",
            colors = 3,
            price = 185,
            category = "Men's Basketball Shoes",
            options = listOf(
                "Shown: Infrared/White",
                "Style: DA9053-100",
                "Material: Textile, synthetic leather"
            ),
        ),
        Product(
            id = "4",
            imageResource = R.drawable.img_home_new_product2,
            name = "Nike Air Force 1 '07",
            isLiked = false,
            shortDescription = "Classic Everyday Sneaker",
            description = "The Nike Air Force 1 '07 updates the iconic shoe with crisp synthetic leather, bold accents, and legendary cushioning for day-long comfort. A timeless look for any style.",
            colors = 3,
            price = 115,
            category = "Lifestyle Shoes",
            options = listOf(
                "Shown: White/Pure Platinum",
                "Style: CW2288-111",
                "Feature: Perforated toe box"
            ),
        ),
        Product(
            id = "5",
            imageResource = R.drawable.img_home_new_product2,
            name = "Nike Air Force 1 '07",
            isLiked = false,
            shortDescription = "Classic Everyday Sneaker",
            description = "The Nike Air Force 1 '07 updates the iconic shoe with crisp synthetic leather, bold accents, and legendary cushioning for day-long comfort. A timeless look for any style.",
            colors = 3,
            price = 115,
            category = "Lifestyle Shoes",
            options = listOf(
                "Shown: White/Pure Platinum",
                "Style: CW2288-111",
                "Feature: Perforated toe box"
            ),
        ),
    )
}