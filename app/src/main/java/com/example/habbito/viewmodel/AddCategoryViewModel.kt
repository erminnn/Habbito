package com.example.habbito.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.CategoryProperty
import com.example.habbito.repository.CategoryRepository
import kotlinx.coroutines.launch

class AddCategoryViewModel(private val repository: CategoryRepository) : ViewModel(){
    val properties = mutableListOf<String>()
    var selectedType = ""
    val types = listOf("Time","Numerical","Quantitative")



    val allCategories: LiveData<List<Category>>
    val getAllCategoriesWithProperties : LiveData<List<CategoryProperty>>

    init {
        allCategories = repository.allCategories
        getAllCategoriesWithProperties= repository.getAllCategoriesWithProperties
    }
    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }
    fun insertCategoryProperty(categoryAdditionalProperty: List<CategoryAdditionalProperty>) = viewModelScope.launch {
        repository.insertCategoryProperty(categoryAdditionalProperty)
    }
    fun insertCategoryWithProperty(category: Category, categoryAdditionalProperty: List<CategoryAdditionalProperty>) = viewModelScope.launch {
        repository.insertCategoryWithProperty(category,categoryAdditionalProperty)
    }
}