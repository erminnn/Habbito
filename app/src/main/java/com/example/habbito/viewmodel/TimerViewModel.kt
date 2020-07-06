package com.example.habbito.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habbito.models.TimeActivity
import com.example.habbito.models.Timer
import com.example.habbito.repository.TimerRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class TimerViewModel(private val repository: TimerRepository) : ViewModel() {
    val allTimers: LiveData<List<Timer>> = repository.allTimers
    val allTimeActivities: LiveData<List<TimeActivity>> = repository.allTimeActivities

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun getTimerById(id: Long): MutableLiveData<Timer> {
        val timer = MutableLiveData<Timer>()
        viewModelScope.launch {
            timer.value = repository.getTimerById(id)
        }
        return timer
    }

    fun updateTimer(timerBase: Long, timerPauseOffset: Long, id: Long, timerState: String) =
        viewModelScope.launch {
            repository.updateTimer(timerBase, timerPauseOffset, id, timerState)
        }

    fun insertTimer(timer: Timer): MutableLiveData<Long> {
        val id = MutableLiveData<Long>()
        viewModelScope.launch {
            id.value = repository.insertTimer(timer)
        }

        return id
    }

    fun insertTimeActivityWithTimer(timeActivity: TimeActivity) = viewModelScope.launch {
        repository.insertTimeActivityWithTimer(timeActivity)
    }

    suspend fun getTimerIdFromTimeActivityAsync(id: Long): Long {
        return withContext(IO) { repository.getTimerIdFromTimeActivity(id) }
    }


}
