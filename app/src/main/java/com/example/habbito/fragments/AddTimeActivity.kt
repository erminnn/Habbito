package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.CategoryActivity
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.ActivityListViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_time_activity.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddTimeActivity : Fragment() {
    var properties: ArrayList<String>? = null
    var selectedProperty = ""
    private lateinit var vm: ActivityListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_time_activity, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.timerDao
        val repository = TimerRepository(dao)
        val factory = TimerViewModelFactory(repository, activity?.application!!)
        vm = ViewModelProvider(this, factory).get(ActivityListViewModel::class.java)
        val bundle: Bundle = this.requireArguments()
        properties = bundle.getStringArrayList("properties")
        vm.categoryId = bundle.getLong("categoryId")

        spinnerAddTimeActivityProperty.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            properties as MutableList<String>
        )

        spinnerAddTimeActivityProperty.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedProperty = properties!!.get(position)
                    Log.d("PROBAAAA", selectedProperty)
                }
            }

        btnSaveTimeActivity.setOnClickListener {
            val activityName = etAddTimeActivityName.text.toString()
            if (properties!!.size != 0 && selectedProperty == "") {
                Toast.makeText(
                    requireContext(),
                    "You need to select activity property",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                vm.insertTimeActivityWithTimer(
                    CategoryActivity(
                        activityName,
                        selectedProperty,
                        vm.categoryId,
                        0
                    )
                )
                val bundle = Bundle()
                bundle.putLong("categoryId", vm.categoryId)

                val activityListFragment = ActivityListFragment()
                activityListFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentHolder, activityListFragment)
                    addToBackStack(null)
                    commit()
                }
            }

        }
    }

}
