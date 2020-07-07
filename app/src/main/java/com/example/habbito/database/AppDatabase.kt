package com.example.habbito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.habbito.dao.CategoryDao
import com.example.habbito.dao.TimerDao
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.Timer


@Database(
    entities = [Category::class, CategoryAdditionalProperty::class, Timer::class, CategoryActivity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val timerDao: TimerDao

    /*val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE users "
                        + "ADD COLUMN address TEXT"
            )
        }
    }*/

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
