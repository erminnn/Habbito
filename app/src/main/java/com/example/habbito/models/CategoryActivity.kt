package com.example.habbito.models

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "category_activity",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"]
        )]
)
data class CategoryActivity(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "property")
    var property: String,
    @ColumnInfo(name = "category_id")
    var categoryId: Long,
    @ColumnInfo(name = "current_value")
    var currentValue: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}
