package com.umc.myapplication.presentation.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.myapplication.data.CategoryRepository
import com.umc.myapplication.data.ProductRepository
import com.umc.myapplication.data.UserLikedRepository
import com.umc.myapplication.data.models.Category
import com.umc.myapplication.data.models.Product
import com.umc.myapplication.data.models.toUiProducts
import com.umc.myapplication.domain.model.UiProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UiProductState {
    data object Idle : UiProductState
    data object Loading : UiProductState
    data object Empty : UiProductState
    data class Data(val products: List<UiProduct>) : UiProductState
    data class Error(val message: String) : UiProductState
}

@HiltViewModel
class UiProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val userLikedRepository: UserLikedRepository
) : ViewModel() {
    private val TAG = "UiProductViewModel"
    private val _state = MutableStateFlow<UiProductState>(UiProductState.Idle)
    val state: StateFlow<UiProductState> = _state.asStateFlow()

    fun loadOnce() = runWithState(
        
        block = {
            Log.d(TAG, "loadOnce: loadOnce")
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertProduct(productId: Int, product: Product) = runWithState(
        block = {
            productRepository.upsertProduct(productId, product)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertProductList(items: List<Product>) = runWithState(
        block = {
            productRepository.upsertProductList(items)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertIsLiked(productId: Int, isLiked: Boolean) = runWithState(
        block = {
            userLikedRepository.addLike(productId)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun deleteProduct(productId: Int) = runWithState(
        block = {
            productRepository.deleteProduct(productId)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertCategory(categoryId: Int, category: Category) = runWithState(
        block = {
            categoryRepository.upsertCategory(categoryId, category)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertCategorieList(items: List<Category>) = runWithState(
        block = {
            categoryRepository.upsertCategoryList(items)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertLiked(productId: Int) = runWithState(
        block = {
            userLikedRepository.addLike(productId)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun deleteLiked(productId: Int) = runWithState(
        block = {
            userLikedRepository.removeLike(productId)
            fetchProductsCategoriesAndLikes()
        },
        onMapToState = { (products, categoryMap, likedProductIds) ->
            val uiProducts = products.toUiProducts(categoryMap, likedProductIds)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )


    private inline fun <T> runWithState(
        crossinline block: suspend () -> T,
        crossinline onMapToState: (T) -> UiProductState
    ) = viewModelScope.launch {
        _state.value = UiProductState.Loading
        try {
            val result = block()
            _state.value = onMapToState(result)
        } catch (e: Exception) {
            _state.value = UiProductState.Error(e.message ?: "Unknown error")
        }
    }

    // id로 단일 UiProduct 반환, 없으면 null
    fun getUiProductById(id: Int): UiProduct? {
        val s = state.value
        Log.d("uiVM", "getUiProductById: $s")
        Log.d("uiVM", "getUiProductById: ${s is UiProductState.Data}")
        return if (s is UiProductState.Data) s.products.firstOrNull { it.id == id } else null
    }

    // 상품, 카테고리, userLiked 동시 조회
    private suspend fun fetchProductsCategoriesAndLikes(): Triple<List<Product>, Map<Int, Category>, Set<Int>> =
        kotlinx.coroutines.coroutineScope {
            val productsDef = async { productRepository.fetchProductsOnce() }
            val categoriesDef = async { categoryRepository.fetchCategorysOnce() }
            val likesDef = async { userLikedRepository.fetchLikedProductIds() }

            val products = productsDef.await()
            val categoryMap = categoriesDef.await().associateBy { it.id }
            val likedProductIds = likesDef.await()

            Triple(products, categoryMap, likedProductIds)
        }
}
