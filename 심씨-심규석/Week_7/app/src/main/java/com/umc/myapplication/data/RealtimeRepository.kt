package com.umc.myapplication.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.umc.myapplication.data.models.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
                child.getValue(Product::class.java)?.apply { id = child.key.orEmpty() }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 단건 upsert
    suspend fun upsertPostProduct(productId: String, dto: Product) {
        productRef.child(productId).setValue(dto).await()
    }

    // 삭제
    suspend fun deletePostProduct(productId: String) {
        productRef.child(productId).removeValue().await()
    }
}