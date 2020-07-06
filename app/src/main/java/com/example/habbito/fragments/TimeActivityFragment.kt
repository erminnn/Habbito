package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbito.R
import com.example.habbito.adapters.TimeAdapter
import com.example.habbito.adapters.TimeAdapter.OnTimeItemClickListener
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.TimeActivity
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.TimerViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_time_activity.*
import kotlinx.coroutines.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class TimeActivityFragment : Fragment(), OnTimeItemClickListener {
    var properties: ArrayList<String>? = null
    private lateinit var vm: TimerViewModel
    private val fragmentJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + fragmentJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_time_activity, container, false)
        try {
            val bundle: Bundle = this.requireArguments()
            properties = bundle.getStringArrayList("properties")
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.timerDao
        val repository = TimerRepository(dao)
        val factory = TimerViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(TimerViewModel::class.java)
        vm.allTimeActivities.observe(this, Observer { items ->
            timeActivityRecyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = TimeAdapter(items, this)
            }
        })

        btnAddNewTimeActivity.setOnClickListener {
            val fragmentManager = activity!!.supportFragmentManager
            val addTimeActivity = AddTimeActivity()
            val bundle = Bundle()
            bundle.putStringArrayList("properties", properties)
            addTimeActivity.arguments = bundle
            fragmentBegin(fragmentManager, addTimeActivity)
        }
    }

    override fun onItemClick(timeActivity: TimeActivity) {
        uiScope.launch {
            val id = vm.getTimerIdFromTimeActivityAsync(timeActivity.id)
            val fragmentManager = activity!!.supportFragmentManager
            val timer = Timer()
            val bundle = Bundle()
            bundle.putLong("timer_id", id)
            timer.arguments = bundle
            fragmentBegin(fragmentManager, timer)
        }
    }

    private fun fragmentBegin(
        fragmentManager: FragmentManager,
        fragment: Fragment
    ) {
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            addToBackStack(null)
            commit()
        }
    }

}
