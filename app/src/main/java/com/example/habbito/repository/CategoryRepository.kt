package com.example.habbito.repository

import androidx.lifecycle.LiveData
import com.example.habbito.dao.CategoryDao
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.CategoryProperty

class CategoryRepository(private val categoryDao: CategoryDao) {
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()


     fun getAllPropertiesForCategory(id : Long) : LiveData<List<CategoryAdditionalProperty>>{
      return  categoryDao.getAllPropertiesForCategory(id)
    }


    suspend fun insertCategory(category: Category){
        categoryDao.insertCategory(category)
    }

    suspend fun insertCategoryProperty(categoryAdditionalProperty: List<CategoryAdditionalProperty>){
        categoryDao.insertCategoryProperty(categoryAdditionalProperty)
    }

    suspend fun insertCategoryWithProperty(category: Category, categoryAdditionalProperty: List<CategoryAdditionalProperty>){
        categoryDao.insertCategoryWithProperty(category,categoryAdditionalProperty);
    }

}