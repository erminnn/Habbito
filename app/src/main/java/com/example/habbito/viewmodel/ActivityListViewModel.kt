package com.example.habbito.viewmodel

import androidx.lifecycle.*
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.Timer
import com.example.habbito.repository.ActivityRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class ActivityListViewModel(private val repository: ActivityRepository) : ViewModel() {
    val allTimers: LiveData<List<Timer>> = repository.allTimers
    var categoryId = 0L
    var allCategoryActivities: MutableLiveData<List<CategoryActivity>?> = MutableLiveData()

    private val _navigateToActivityDetail = MutableLiveData<Boolean>()

    val navigateToActivityDetail: LiveData<Boolean>
        get() = _navigateToActivityDetail

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
//        initializeActivities()
    }

    fun onUpdateActivityClick(newValue: Long) {
        uiScope.launch {
            updateActivityValue(newValue)
        }
    }

    suspend fun updateActivityValue(newValue: Long) {
        withContext(IO){
            repository.updateActivityValue(newValue)
        }
    }

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

    fun insertTimeActivityWithTimer(categoryActivity: CategoryActivity) = viewModelScope.launch {
        repository.insertTimeActivityWithTimer(categoryActivity)
    }

    fun onActivityClicked() {
        uiScope.launch {
            val id = getTimerByTimeActivity(categoryId)
            _navigateToActivityDetail.value = true
        }
    }

    suspend fun getTimerByTimeActivity(id: Long): Timer {
        return withContext(IO) { repository.getTimerByTimeActivity(id) }
    }

    fun initializeActivities() {
        uiScope.launch {
            allCategoryActivities.value = getActivitiesFromDB()
        }
    }

    private suspend fun getActivitiesFromDB(): List<CategoryActivity>?{
        return withContext(IO) {
            repository.getActivitiesByCategoryId(categoryId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    suspend fun getCategory(): Category {
        return withContext(IO) {
            repository.getCategory(categoryId)
        }

    }

    fun initializeCategoryProperties(categoryId: Long): LiveData<List<CategoryAdditionalProperty>> {
        return repository.getAllPropertiesForCategory(categoryId)
    }
}
