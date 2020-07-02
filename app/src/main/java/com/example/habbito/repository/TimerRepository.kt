package com.example.habbito.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.habbito.dao.TimerDao
import com.example.habbito.models.TimeActivity
import com.example.habbito.models.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TimerRepository(private val timerDao: TimerDao) {
    val allTimers: LiveData<List<Timer>> = timerDao.getAllTimers()
    val allTimeActivities: LiveData<List<TimeActivity>> = timerDao.getAllTimeActivities()
    suspend fun insertTimer(timer: Timer): Long {
    return timerDao.insertTimer(timer)
    }
    suspend fun getTimerById(id : Long) : Timer  {
       return timerDao.getTimerById(id) as Timer
   }
    suspend fun updateTimer(timerBase : Long,timerPauseOffset : Long,id : Long,timerState : String){
        timerDao.updateTimer(timerBase,timerPauseOffset,id,timerState)
    }
    suspend fun insertTimeActivityWithTimer(timeActivity: TimeActivity){
        timerDao.insertTimeActivityWithTimer(timeActivity)
    }
    suspend fun getTimerIdFromTimeActivity(id : Long) : Long{
        return timerDao.getTimerIdFromTimeActivity(id)
    }

}