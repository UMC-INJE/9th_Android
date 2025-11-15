package com.umc.myapplication.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.umc.myapplication.data.models.Category
import kotlinx.coroutines.tasks.await

class CategoryRepository(
    private val db: FirebaseDatabase
){
    private val categoryRef : DatabaseReference
        get() = db.getReference("category")

    suspend fun fetchCategorysOnce(): List<Category> {
        return try {
            val snap = categoryRef.get().await()
            snap.children.mapNotNull { child ->
                child.getValue(Category::class.java)?.apply { id = child.key.orEmpty().toInt() }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // upsert
    suspend fun upsertCategory(productId: Int, product: Category) {
        categoryRef.child(productId.toString()).setValue(product).await()
    }
    suspend fun upsertCategoryList(products: List<Category>) {
        if (products.isEmpty()) return
        val updates = buildMap<String, Any?> {
            for (product in products) {
                put(product.id.toString(), product)
            }
        }
        if (updates.isNotEmpty()) {
            categoryRef.updateChildren(updates).await()
        }
    }

    // 삭제
    suspend fun deletePostCategory(productId: Int) {
        categoryRef.child(productId.toString()).removeValue().await()
    }
}