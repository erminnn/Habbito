package com.example.habbito.repository

import androidx.lifecycle.LiveData
import com.example.habbito.dao.ActivityDao
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.Timer

class ActivityRepository(private val activityDao: ActivityDao) {
    val allTimers: LiveData<List<Timer>> = activityDao.getAllTimers()
    val allCategoryActivities: LiveData<List<CategoryActivity>> = activityDao.getCategoryActivities()

    suspend fun getActivitiesByCategoryId(categoryId: Long): List<CategoryActivity>? {
        return activityDao.getActivitiesByCategoryId(categoryId)
    }

    suspend fun insertTimer(timer: Timer): Long {
        return activityDao.insertTimer(timer)
    }

    suspend fun getTimerById(id: Long): Timer {
        return activityDao.getTimerById(id) as Timer
    }

    suspend fun updateTimer(timerBase: Long, timerPauseOffset: Long, id: Long, timerState: String) {
        activityDao.updateTimer(timerBase, timerPauseOffset, id, timerState)
    }

    suspend fun insertTimeActivityWithTimer(categoryActivity: CategoryActivity) {
        activityDao.insertTimeActivityWithTimer(categoryActivity)
    }

    suspend fun getTimerByTimeActivity(id: Long): Timer {
        return activityDao.getTimerByTimeActivity(id)
    }

    suspend fun getCategory(categoryId: Long): Category {
        return activityDao.getCategory(categoryId)
    }

    suspend fun updateActivityValue(newValue: Long) {
        return activityDao.updateActivityValue(newValue)
    }

    fun getAllPropertiesForCategory(id: Long): LiveData<List<CategoryAdditionalProperty>> {
        return activityDao.getAllPropertiesForCategory(id)
    }

}
