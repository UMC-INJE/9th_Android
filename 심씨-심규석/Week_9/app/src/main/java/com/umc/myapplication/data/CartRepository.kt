package com.umc.myapplication.data

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class CartRepository @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun saveProductId(productId: Int) {
        prefs.edit { putInt(KEY_PRODUCT_ID, productId) }
    }

    fun getProductId(): Int {
        val v = prefs.getInt(KEY_PRODUCT_ID, NO_VALUE)
        return v
    }

    fun clearProductId() {
        prefs.edit { remove(KEY_PRODUCT_ID) }
    }

    private companion object {
        const val KEY_PRODUCT_ID = "cart_product_id"
        const val NO_VALUE = -1
    }
}