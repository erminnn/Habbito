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
abstract class CategoryDao {

    @Query("SELECT * FROM category where category_id = :id")
    abstract suspend fun getCategoryById(id : Long): Category

    @Query("SELECT * FROM category")
    abstract fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * from category_property cp  where cp.category_idFK = :id")
    abstract fun getAllPropertiesForCategory(id : Long): LiveData<List<CategoryAdditionalProperty>>

    @Insert
    abstract suspend fun insertCategory(category: Category) : Long

    @Insert
    abstract suspend fun insertCategoryProperty(categoryAdditionalProperty: List<CategoryAdditionalProperty>)


     fun insertCategoryWithProperty(category: Category, categoryAdditionalProperty: List<CategoryAdditionalProperty>) = runBlocking{
        val asyncInsertCategory = async { insertCategory(category) }
        val categoryId : Long = asyncInsertCategory.await()
        val categoryAdditionalPropertyWithId = categoryAdditionalProperty
        for(cap in categoryAdditionalPropertyWithId){
            cap.categoryId = categoryId
        }
        insertCategoryProperty(categoryAdditionalPropertyWithId)
    }

    @Query("DELETE from category where category_id = :id")
    abstract suspend fun deleteCategory(id : Long)

    @Query("DELETE from category_property where category_idFK = :id")
    abstract suspend fun deleteCategoryProperties(id : Long)

    suspend fun deleteCategoryWithProperties(id : Long){
        deleteCategory(id)
        deleteCategoryProperties(id)
    }

}