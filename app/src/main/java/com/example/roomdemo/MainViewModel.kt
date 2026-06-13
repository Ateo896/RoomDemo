package com.example.roomdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.ProductRoomDatabase
import com.example.roomdemo.dao.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val productDb: ProductRoomDatabase = ProductRoomDatabase.getInstance(application)
    private val productDao: ProductDao = productDb.productDao()
    private val repository: ProductRepository = ProductRepository(productDao)

    val allProducts: LiveData<List<Product>> = repository.searchResults
    val searchResults: MutableLiveData<List<Product>> = MutableLiveData(emptyList())

    init {
        allProducts.observeForever { list ->
            if (!searching.value) {
                searchResults.value = list ?: emptyList()
            }
        }
    }

    val searching = MutableLiveData(false)

    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    fun findProduct(name: String) {
        repository.findProduct(name)
    }

    fun deleteProduct(name: String) {
        repository.deleteProduct(name)
    }
}