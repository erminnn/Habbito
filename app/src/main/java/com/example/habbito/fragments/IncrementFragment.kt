package com.example.habbito.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.repository.ActivityRepository
import com.example.habbito.viewmodel.ActivityListViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [IncrementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncrementFragment : Fragment() {
    private val CURRENT_VALUE = "currValue"
    private var currValue: Long? = null
    private lateinit var value: TextView
    private lateinit var vm: ActivityListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currValue = it.getLong(CURRENT_VALUE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dao = AppDatabase.getInstance(requireContext())!!.activityDao
        val repository = ActivityRepository(dao)
        val factory = TimerViewModelFactory(repository, activity?.application!!)
        vm = ViewModelProvider(this, factory).get(ActivityListViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_increment, container, false)
        value = view.findViewById<TextView>(R.id.tv_value)
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)
        val incrementBtn = view.findViewById<ImageButton>(R.id.bt_increase)
        val decrementBtn = view.findViewById<ImageButton>(R.id.bt_decrease)
        incrementBtn.setOnClickListener { v -> onIncrementClick(v) }
        decrementBtn.setOnClickListener { v -> onDecrementClick(v) }
        value.text = if (currValue == null) "0" else currValue.toString()
        saveBtn.setOnClickListener{ onSaveBtnClick(it)}

        return view
    }

    private fun onSaveBtnClick(view: View) {
        val newVal = value.text.toString().toLong()
        vm.onUpdateActivityClick(newVal)
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun onIncrementClick(view: View) {
        value.text = value.text.toString().toLong().inc().toString()
    }

    private fun onDecrementClick(view: View) {
        value.text = value.text.toString().toLong().dec().toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(currValue: Long) =
            IncrementFragment().apply {
                arguments = Bundle().apply {
                    putLong(CURRENT_VALUE, currValue)
                }
            }
    }
}
