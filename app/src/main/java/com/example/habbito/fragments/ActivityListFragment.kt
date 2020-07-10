package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbito.R
import com.example.habbito.adapters.ActivityAdapter
import com.example.habbito.adapters.ActivityAdapter.OnTimeItemClickListener
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryActivity
import com.example.habbito.repository.ActivityRepository
import com.example.habbito.viewmodel.ActivityListViewModel
import com.example.habbito.viewmodel.AddCategoryViewModel.*
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
        val view: View = inflater.inflate(R.layout.fragment_time_activity, container, false)
        try {
            val bundle: Bundle = this.requireArguments()
            val dao = AppDatabase.getInstance(requireContext())!!.activityDao
            val repository = ActivityRepository(dao)
            val factory = TimerViewModelFactory(repository, activity?.application!!)
            vm = ViewModelProvider(this, factory).get(ActivityListViewModel::class.java)
            vm.categoryId = bundle.getLong("categoryId")
            vm.initializeActivities()
            observeCategoryProperties()
            observeActivities()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnAddNewTimeActivity.setOnClickListener {
            val addTimeActivity = AddActivityFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("properties", properties)
            bundle.putLong("categoryId", vm.categoryId)
            addTimeActivity.arguments = bundle
            launchFragment(addTimeActivity)
        }
    }

    override fun onItemClick(categoryActivity: CategoryActivity) {
        uiScope.launch {
            val category = vm.getCategory()
            val fragment = getActivityFragment(category, categoryActivity)
            launchFragment(fragment)
        }
    }

    private suspend fun getActivityFragment(
        category: Category,
        categoryActivity: CategoryActivity
    ): Fragment {
        return when (category.type) {
            CategoryType.Time.toString() -> TimerFragment.newInstance(vm.getTimerByTimeActivity(categoryActivity.id).id)
            CategoryType.Incremental.toString() -> IncrementFragment.newInstance(categoryActivity.currentValue)
            else -> QuantityFragment.newInstance(categoryActivity.currentValue)
        }
    }

    private fun observeActivities() {
        vm.allCategoryActivities.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { items ->
                timeActivityRecyclerView.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = ActivityAdapter(items, this)
                }
            })
    }

    private fun observeCategoryProperties() {
        vm.initializeCategoryProperties(vm.categoryId).observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { t ->
                properties =
                    ArrayList(t.map { categoryAdditionalProperty -> categoryAdditionalProperty.name })
            }
        )
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            addToBackStack(null)
            commit()
        }
    }

}
