package com.example.habbito.dao

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habbito.models.TimeActivity
import com.example.habbito.models.Timer
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Dao
abstract class TimerDao {
    @Insert
    abstract suspend fun insertTimer(timer: Timer) : Long

    @Query("SELECT * FROM timer")
    abstract  fun getAllTimers(): LiveData<List<Timer>>

    @Query("Select * FROM timer where timer_id = :id")
    abstract suspend fun getTimerById(id : Long) : Timer?

    @Query("UPDATE timer set timer_base = :timerBase,timer_pause_offset = :timerPauseOffset,timer_state = :timerState where timer_id = :id")
    abstract suspend fun updateTimer(timerBase : Long,timerPauseOffset : Long,id : Long,timerState : String)

    @Insert
    abstract suspend fun insertTimeActivity(timeActivity: TimeActivity) : Long

    suspend fun insertTimeActivityWithTimer(timeActivity: TimeActivity)= runBlocking{
            val asyncInsertTimeActivity = async { insertTimeActivity(timeActivity) }
            val activityId = asyncInsertTimeActivity.await()
            insertTimer(Timer(SystemClock.elapsedRealtime(),0,"Stopped",activityId))
    }

    @Query("SELECT * FROM time_activity")
    abstract fun getAllTimeActivities(): LiveData<List<TimeActivity>>

    @Query("SELECT t.timer_id from timer t where t.time_activity_id = :id")
    abstract  fun getTimerIdFromTimeActivity(id : Long) : Long


}
