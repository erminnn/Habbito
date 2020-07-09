package com.example.habbito.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryListViewModel

class CategoryViewModelFactory (private val repository: CategoryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryListViewModel::class.java)) {
            return CategoryListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
