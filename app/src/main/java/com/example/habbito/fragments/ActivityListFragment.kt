package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.FrameMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbito.R
import com.example.habbito.adapters.TimeAdapter
import com.example.habbito.adapters.TimeAdapter.OnTimeItemClickListener
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.CategoryActivity
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.ActivityListViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_time_activity.*
import kotlinx.coroutines.*
import java.util.*


class ActivityListFragment : Fragment(), OnTimeItemClickListener {
    var properties: ArrayList<String>? = null
    private lateinit var vm: ActivityListViewModel
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
            val dao = AppDatabase.getInstance(requireContext())!!.timerDao
            val repository = TimerRepository(dao)
            val factory = TimerViewModelFactory(repository, activity?.application!!)
            vm = ViewModelProvider(this, factory).get(ActivityListViewModel::class.java)
            vm.categoryId = bundle.getLong("categoryId")
            vm.initializeActivities()
            vm.allCategoryActivities.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { items ->
                    timeActivityRecyclerView.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = TimeAdapter(items, this)
                    }
                })
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnAddNewTimeActivity.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val addTimeActivity = AddTimeActivity()
            val bundle = Bundle()
            bundle.putStringArrayList("properties", properties)
            bundle.putLong("categoryId", vm.categoryId)
            addTimeActivity.arguments = bundle
            launchFragment(fragmentManager, addTimeActivity)
        }
    }

    override fun onItemClick(categoryActivity: CategoryActivity) {
        uiScope.launch {
            val fragmentManager = requireActivity().supportFragmentManager
            val category = vm.getCategory()
            val fragment: Fragment?
            fragment =  if (category.type == "Time") TimerFragment.newInstance(vm.getTimerByTimeActivity(categoryActivity.id).id)
                        else IncrementFragment.newInstance(categoryActivity.currentValue)
            launchFragment(fragmentManager, fragment)
        }
    }

    private fun launchFragment(
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
