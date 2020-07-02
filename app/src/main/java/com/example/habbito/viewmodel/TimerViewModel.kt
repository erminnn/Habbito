package com.example.habbito.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habbito.models.TimeActivity
import com.example.habbito.models.Timer
import com.example.habbito.repository.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TimerViewModel(private val repository: TimerRepository) : ViewModel(){
    val allTimers: LiveData<List<Timer>>
    val allTimeActivities: LiveData<List<TimeActivity>>
    init {
        allTimers = repository.allTimers
        allTimeActivities = repository.allTimeActivities
    }

    fun getTimerById(id : Long): MutableLiveData<Timer> {
        val timer =  MutableLiveData<Timer>()
        viewModelScope.launch {
            timer.value = repository.getTimerById(id)
        }
        return timer
    }
   fun updateTimer(timerBase : Long,timerPauseOffset : Long,id : Long,timerState : String) = viewModelScope.launch{
        repository.updateTimer(timerBase,timerPauseOffset,id,timerState)
    }
    fun insertTimer(timer: Timer): MutableLiveData<Long> {
        val id =  MutableLiveData<Long>()
        viewModelScope.launch {
            id.value = repository.insertTimer(timer)
        }

        return id
    }

    fun insertTimeActivityWithTimer(timeActivity: TimeActivity) = viewModelScope.launch {
        repository.insertTimeActivityWithTimer(timeActivity)
    }

    fun getTimerIdFromTimeActivity(id : Long) : MutableLiveData<Long>  {
        val timer_id =  MutableLiveData<Long>()
        viewModelScope.launch {
            timer_id.value = repository.getTimerIdFromTimeActivity(id)
        }
        return timer_id
    }


}