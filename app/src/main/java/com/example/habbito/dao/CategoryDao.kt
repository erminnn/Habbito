package com.example.habbito.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Dao
interface  CategoryDao {

    @Query("SELECT * FROM category where category_id = :id")
    suspend fun getCategoryById(id : Long): Category

    @Query("SELECT * FROM category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * from category_property cp  where cp.category_idFK = :id")
    fun getAllPropertiesForCategory(id : Long): LiveData<List<CategoryAdditionalProperty>>

    @Insert
    suspend fun insertCategory(category: Category) : Long

    @Insert
    suspend fun insertCategoryProperty(categoryAdditionalProperty: List<CategoryAdditionalProperty>)


     fun insertCategoryWithProperty(category: Category, categoryAdditionalProperty: List<CategoryAdditionalProperty>) = runBlocking{
         val asyncInsertCategory = async { insertCategory(category) }
         val categoryId: Long = asyncInsertCategory.await()
         for (cap in categoryAdditionalProperty) {
             cap.categoryId = categoryId
         }
         insertCategoryProperty(categoryAdditionalProperty)
    }

    @Query("DELETE from category where category_id = :id")
    suspend fun deleteCategory(id : Long)

    @Query("DELETE from category_property where category_idFK = :id")
    suspend fun deleteCategoryProperties(id : Long)

    suspend fun deleteCategoryWithProperties(id : Long){
        deleteCategory(id)
        deleteCategoryProperties(id)
    }

}
