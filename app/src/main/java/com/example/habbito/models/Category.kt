package com.example.habbito.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category (
    @ColumnInfo(name = "category_name")
    var name : String,
    @ColumnInfo(name = "category_type")
    var type : String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var id : Long = 0
}