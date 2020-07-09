package com.example.habbito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habbito.dao.CategoryDao
import com.example.habbito.dao.ActivityDao
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.Timer


@Database(
    entities = [Category::class, CategoryAdditionalProperty::class, Timer::class, CategoryActivity::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val activityDao: ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}
