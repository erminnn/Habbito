package com.example.habbito.repository

import androidx.lifecycle.LiveData
import com.example.habbito.dao.TimerDao
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.Timer

class TimerRepository(private val timerDao: TimerDao) {
    val allTimers: LiveData<List<Timer>> = timerDao.getAllTimers()
    val allCategoryActivities: LiveData<List<CategoryActivity>> = timerDao.getCategoryActivities()

    suspend fun getActivitiesByCategoryId(categoryId: Long): List<CategoryActivity>? {
        return timerDao.getActivitiesByCategoryId(categoryId)
    }

    suspend fun insertTimer(timer: Timer): Long {
        return timerDao.insertTimer(timer)
    }

    suspend fun getTimerById(id: Long): Timer {
        return timerDao.getTimerById(id) as Timer
    }

    suspend fun updateTimer(timerBase: Long, timerPauseOffset: Long, id: Long, timerState: String) {
        timerDao.updateTimer(timerBase, timerPauseOffset, id, timerState)
    }

    suspend fun insertTimeActivityWithTimer(categoryActivity: CategoryActivity) {
        timerDao.insertTimeActivityWithTimer(categoryActivity)
    }

    suspend fun getTimerByTimeActivity(id: Long): Timer {
        return timerDao.getTimerByTimeActivity(id)
    }

    suspend fun getCategory(categoryId: Long): Category {
        return timerDao.getCategory(categoryId)
    }

    suspend fun updateActivityValue(newValue: Long) {
        return timerDao.updateActivityValue(newValue)
    }

    fun getAllPropertiesForCategory(id: Long): LiveData<List<CategoryAdditionalProperty>> {
        return timerDao.getAllPropertiesForCategory(id)
    }

}
