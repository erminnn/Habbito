package com.example.habbito.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryListViewModel(private val repository: CategoryRepository) : ViewModel(){

    val allCategories: LiveData<List<Category>> = repository.allCategories

    fun getAllPropertiesForCategory(id : Long) : LiveData<List<CategoryAdditionalProperty>>{
        return  repository.getAllPropertiesForCategory(id)
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
    fun deleteCategoryWithProperties(id : Long) = viewModelScope.launch {
        repository.deleteCategoryWithProperties(id)
    }

    fun getCategoryById(id : Long): MutableLiveData<Category>{
        val category = MutableLiveData<Category>()
        viewModelScope.launch {
            category.value = repository.getCategoryById(id)
        }
        return category
    }

}
