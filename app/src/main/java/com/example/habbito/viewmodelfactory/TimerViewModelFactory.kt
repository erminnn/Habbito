package com.example.habbito.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.ActivityListViewModel

class TimerViewModelFactory(private val repository: TimerRepository, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityListViewModel::class.java)) {
            return ActivityListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
