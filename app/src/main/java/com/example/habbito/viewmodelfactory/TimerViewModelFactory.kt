package com.example.habbito.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.CategoryViewModel
import com.example.habbito.viewmodel.TimerViewModel

class TimerViewModelFactory(private val repository: TimerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}