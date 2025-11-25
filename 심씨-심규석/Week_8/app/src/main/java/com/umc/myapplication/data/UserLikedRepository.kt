package com.umc.myapplication.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserLikedRepository(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {
    private fun getCurrentUid(): String? = auth.currentUser?.uid

    private fun userLikesRef(): DatabaseReference {
        val uid = getCurrentUid() ?: throw IllegalStateException("User not signed in")
        return database.getReference("user_likes").child(uid)
    }

    suspend fun addLike(productId: Int) = suspendCancellableCoroutine<Unit> { cont ->
        userLikesRef().child(productId.toString()).setValue(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) cont.resume(Unit)
                else cont.resumeWithException(task.exception ?: Exception("Unknown error"))
            }
    }

    suspend fun removeLike(productId: Int) = suspendCancellableCoroutine<Unit> { cont ->
        userLikesRef().child(productId.toString()).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) cont.resume(Unit)
                else cont.resumeWithException(task.exception ?: Exception("Unknown error"))
            }
    }

    suspend fun fetchLikedProductIds(): Set<Int> = suspendCancellableCoroutine { cont ->
        userLikesRef().get()
            .addOnSuccessListener { snapshot ->
                val ids = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.toSet()
                cont.resume(ids)
            }
            .addOnFailureListener { e ->
                cont.resumeWithException(e)
            }
    }
}
