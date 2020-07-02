package com.example.habbito.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "time_activity")
data class TimeActivity (
    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "property")
    var property : String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Long = 0
}