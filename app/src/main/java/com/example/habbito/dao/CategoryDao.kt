package com.example.habbito.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.CategoryProperty

@Dao
abstract class CategoryDao {

    @Query("SELECT * FROM category")
    abstract fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT c.*,cp.* from category c inner join category_property cp on c.category_id = cp.category_idFK")
    abstract fun getAllCategoriesWithProperties(): LiveData<List<CategoryProperty>>

    @Insert
    abstract suspend fun insertCategory(category: Category) : Long

    @Insert
    abstract suspend fun insertCategoryProperty(categoryAdditionalProperty: List<CategoryAdditionalProperty>)


    suspend fun insertCategoryWithProperty(category: Category, categoryAdditionalProperty: List<CategoryAdditionalProperty>){
        val categoryId : Long = insertCategory(category)
        for(cap in categoryAdditionalProperty){
            cap.categoryId = categoryId
        }
        insertCategoryProperty(categoryAdditionalProperty)
    }

}