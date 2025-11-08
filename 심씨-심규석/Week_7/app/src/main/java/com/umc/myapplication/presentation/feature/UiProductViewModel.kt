package com.umc.myapplication.presentation.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.myapplication.data.CategoryRepository
import com.umc.myapplication.data.ProductRepository
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
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiProductState>(UiProductState.Idle)
    val state: StateFlow<UiProductState> = _state.asStateFlow()

    fun loadOnce()  = runWithState(
        block = {   fetchProductsAndCategories() },
        onMapToState = { (products, categoryMap) ->
            val uiProducts = products.toUiProducts(categoryMap)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }

    )

    fun upsertProduct(productId: Int, product: Product) = runWithState(
        block = {
            productRepository.upsertProduct(productId, product)
            fetchProductsAndCategories()
        },
        onMapToState = { (products, categoryMap) ->
            val uiProducts = products.toUiProducts(categoryMap)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun upsertProductList(items: List<Product>) = runWithState(
        block = {
            productRepository.upsertProductList(items)
            fetchProductsAndCategories()
        },
        onMapToState = { (products, categoryMap) ->
            val uiProducts = products.toUiProducts(categoryMap)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }
    )

    fun deleteProduct(productId: Int) = runWithState(
        block = {
            productRepository.deleteProduct(productId)
            fetchProductsAndCategories()
        },
        onMapToState = { (products, categoryMap) ->
            val uiProducts = products.toUiProducts(categoryMap)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }

    )

    fun upsertCategory(categoryId: Int, category: Category) = runWithState(
        block = {
            categoryRepository.upsertCategory(categoryId, category)
            fetchProductsAndCategories()
        },
        onMapToState = { (products, categoryMap) ->
            val uiProducts = products.toUiProducts(categoryMap)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }

    )

    fun upsertCategorieList(items: List<Category>) = runWithState(
        block = {
            categoryRepository.upsertCategoryList(items)
            fetchProductsAndCategories()
        },
        onMapToState = { (products, categoryMap) ->
            val uiProducts = products.toUiProducts(categoryMap)
            if (uiProducts.isNotEmpty()) UiProductState.Data(uiProducts) else UiProductState.Empty
        }

    )

    //로딩, 에러처리 자동화
    //실행할 것, 실행완료후 데이터 처리넣을 것
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
        // finally에서 Idle로 덮어쓰지 않기: 성공/에러가 바로 사라지는 문제 방지
    }



    private suspend fun fetchProductsAndCategories(): Pair<List<Product>, Map<Int, Category>> =
        kotlinx.coroutines.coroutineScope {
            val productsDef = async { productRepository.fetchProductsOnce() }
            val categoriesDef = async { categoryRepository.fetchCategorysOnce() }
            val products = productsDef.await()
            val categoryMap = categoriesDef.await().associateBy { it.id }
            products to categoryMap
        }

}