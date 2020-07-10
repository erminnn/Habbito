package com.example.habbito.fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.repository.ActivityRepository
import com.example.habbito.viewmodel.ActivityListViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory

private const val CURRENT_VALUE = "currValue"

class QuantityFragment : Fragment() {
    private lateinit var value: EditText
    private var currentValue: Long = 0L
    private lateinit var vm: ActivityListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentValue = it.getLong(CURRENT_VALUE)
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
        val view = inflater.inflate(R.layout.fragment_quantity, container, false)
        value = view.findViewById(R.id.etQuantityValue)
        val saveBtn = view.findViewById<Button>(R.id.btnQuantitySave)
        saveBtn.setOnClickListener{ onSaveBtnClick(it)}
        value.setText (currentValue.toString())
        return view
    }

    private fun onSaveBtnClick(view: View) {
        val newVal = value.text.toString().toLong()
        vm.onUpdateActivityClick(newVal)
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuantityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(currValue: Long) =
            QuantityFragment().apply {
                arguments = Bundle().apply {
                    putLong(CURRENT_VALUE, currValue)
                }
            }
    }
}
