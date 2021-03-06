package com.example.habbito.dao

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.Timer
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Dao
interface ActivityDao {
    @Insert
    suspend fun insertTimer(timer: Timer): Long

    @Query("SELECT * FROM timer")
    fun getAllTimers(): LiveData<List<Timer>>

    @Query("Select * FROM timer where timer_id = :id")
    suspend fun getTimerById(id: Long): Timer?

    @Query("UPDATE timer set timer_base = :timerBase,timer_pause_offset = :timerPauseOffset,timer_state = :timerState where timer_id = :id")
    suspend fun updateTimer(timerBase: Long, timerPauseOffset: Long, id: Long, timerState: String)

    @Insert
    suspend fun insertTimeActivity(categoryActivity: CategoryActivity): Long

    suspend fun insertTimeActivityWithTimer(categoryActivity: CategoryActivity) = runBlocking {
        val asyncInsertTimeActivity = async { insertTimeActivity(categoryActivity) }
        val activityId = asyncInsertTimeActivity.await()
        insertTimer(Timer(SystemClock.elapsedRealtime(), 0, "Stopped", activityId))
    }

    @Query("SELECT * FROM category_activity")
    fun getCategoryActivities(): LiveData<List<CategoryActivity>>

    @Query("SELECT t.* from timer t where t.time_activity_id = :activityId")
    suspend fun getTimerByTimeActivity(activityId: Long): Timer

    @Query("select * from category_activity a where a.category_id = :categoryId")
    suspend fun getActivitiesByCategoryId(categoryId: Long): List<CategoryActivity>?

    @Query("select * from category a where a.category_id = :categoryId")
    suspend fun getCategory(categoryId: Long): Category

    @Query("update category_activity  set current_value = :newValue")
    suspend fun updateActivityValue(newValue: Long)

    @Query("SELECT * from category_property cp  where cp.category_idFK = :id")
    fun getAllPropertiesForCategory(id : Long): LiveData<List<CategoryAdditionalProperty>>

}
