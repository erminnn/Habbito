package com.example.habbito.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.repository.CategoryRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class CategoryListViewModel(private val repository: CategoryRepository) : ViewModel() {

    val allCategories: LiveData<List<Category>> = repository.allCategories
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var categoryToEdit: MutableLiveData<Category> = MutableLiveData()

    fun getAllPropertiesForCategory(id: Long): LiveData<List<CategoryAdditionalProperty>> {
        return repository.getAllPropertiesForCategory(id)
    }

    suspend fun insertCategory(category: Category) = withContext(IO) {
        repository.insertCategory(category)
    }

    suspend fun insertCategoryProperty(categoryAdditionalProperty: List<CategoryAdditionalProperty>) =
        withContext(IO) {
            repository.insertCategoryProperty(categoryAdditionalProperty)
        }

    suspend fun insertCategoryWithProperty(
        category: Category,
        categoryAdditionalProperty: List<CategoryAdditionalProperty>
    ) = withContext(IO) {
        repository.insertCategoryWithProperty(category, categoryAdditionalProperty)
    }

    suspend fun deleteCategoryWithProperties(id: Long) = withContext(IO){
        repository.deleteCategoryWithProperties(id)
    }

    fun initCategory(id: Long) {
        uiScope.launch {
            categoryToEdit.value = getCategory(id)
        }
    }

    private suspend fun getCategory(id: Long): Category {
        return withContext(IO) { repository.getCategoryById(id) }
    }

}
