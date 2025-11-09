package com.umc.myapplication.data

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.umc.myapplication.data.models.Product
import kotlinx.coroutines.tasks.await

class ProductRepository (
    private val db: FirebaseDatabase
){
    private val productRef: DatabaseReference
        get() = db.getReference("products")

    suspend fun fetchProductsOnce(): List<Product> {
        return try {
            val snap = productRef.get().await()
            val data = snap.children.mapNotNull { child ->
                child.getValue(Product::class.java)?.apply { id = child.key.orEmpty().toInt() }
            }
            Log.d("firebase", "fetchProductsOnce: product " + data)
            data
        } catch (e: Exception) {
            emptyList()
        }
    }

    // upsert
    suspend fun upsertProduct(productId: Int, product: Product) {
        productRef.child(productId.toString()).setValue(product).await()
    }
    suspend fun upsertIsLiked(productId: Int, isLiked: Boolean) {
        productRef.child(productId.toString()).child("isLiked").setValue(isLiked).await()
    }
    suspend fun upsertProductList(products: List<Product>) {
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
    suspend fun deleteProduct(productId: Int) {
        productRef.child(productId.toString()).removeValue().await()
    }
}