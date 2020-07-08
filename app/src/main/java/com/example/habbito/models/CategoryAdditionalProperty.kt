package com.example.habbito.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "category_property",
        foreignKeys = [
        androidx.room.ForeignKey(
            entity = com.example.habbito.models.Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_idFK"]
        )]
)
data class CategoryAdditionalProperty(
    @ColumnInfo(name = "property_name")
    var name : String,
    @ColumnInfo(name = "category_idFK")
    var categoryId : Long?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    var id : Long = 0
}
