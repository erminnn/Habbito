package com.example.habbito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habbito.dao.CategoryDao
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty


@Database(entities = [Category::class,CategoryAdditionalProperty::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao : CategoryDao
    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context):AppDatabase?{
            if(INSTANCE==null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database").build()
                }
            }
            return INSTANCE
        }
    }
}