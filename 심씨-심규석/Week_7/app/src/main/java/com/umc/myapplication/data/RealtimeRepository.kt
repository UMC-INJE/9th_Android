package com.umc.myapplication.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.umc.myapplication.data.models.Product
import kotlinx.coroutines.tasks.await

class RealtimeRepository (
    private val db: FirebaseDatabase
){
    private val productRef: DatabaseReference
        get() = db.getReference("products")

    suspend fun fetchProductsOnce(): List<Product> {
        return try {
            val snap = productRef.get().await()
            snap.children.mapNotNull { child ->
                child.getValue(Product::class.java)?.apply { id = child.key.orEmpty().toInt() }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // upsert
    suspend fun upsertPostProduct(productId: Int, product: Product) {
        productRef.child(productId.toString()).setValue(product).await()
    }
    suspend fun upsertProducts(products: List<Product>) {
        if (products.isEmpty()) return
        val updates = buildMap<String, Any?> {
            for (product in products) {
                put(product.id.toString(), product)
            }
        }
        if (updates.isNotEmpty()) {
            productRef.updateChildren(updates).await()
        }
    }

    // 삭제
    suspend fun deletePostProduct(productId: String) {
        productRef.child(productId).removeValue().await()
    }
}