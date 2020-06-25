package com.example.habbito.viewmodelfactory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryViewModel

class CategoryViewModelFactory (private val repository: CategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}