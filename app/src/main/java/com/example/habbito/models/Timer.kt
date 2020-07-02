package com.example.habbito.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer")
data class Timer (
    @ColumnInfo(name = "timer_base")
    var startBase : Long,
    @ColumnInfo(name = "timer_pause_offset")
    var pauseOffset : Long?,
    @ColumnInfo(name = "timer_state")
    var timerState : String,
    @ColumnInfo(name = "time_activity_id")
    var time_activity_id : Long?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "timer_id")
    var id : Long = 0
}