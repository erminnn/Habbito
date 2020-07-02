package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.TimeActivity
import com.example.habbito.repository.TimerRepository
import com.example.habbito.viewmodel.TimerViewModel
import com.example.habbito.viewmodelfactory.TimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_add_time_activity.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class AddTimeActivity : Fragment(){
    var properties : ArrayList<String>? = null
    var selectedProperty = ""
    private lateinit var vm: TimerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_add_time_activity, container, false)
        val bundle : Bundle = this.requireArguments()
        properties = bundle.getStringArrayList("properties")
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.timerDao
        val repository = TimerRepository(dao)
        val factory = TimerViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(TimerViewModel::class.java)




        spinnerAddTimeActivityProperty.adapter  = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,properties as MutableList<String>)

        spinnerAddTimeActivityProperty.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedProperty = properties!!.get(position)
                Log.d("PROBAAAA",selectedProperty)
            }
        }


        btnSaveTimeActivity.setOnClickListener {
            val activityName = etAddTimeActivityName.text.toString()
            if(properties!!.size != 0 && selectedProperty == ""){
                Toast.makeText(requireContext(),"You need to select activity property",Toast.LENGTH_SHORT).show()
            }else{
                vm.insertTimeActivityWithTimer(TimeActivity(activityName,selectedProperty))
                val fragmentManager = activity!!.supportFragmentManager
                val timeActivityFragment = TimeActivityFragment()
                fragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentHolder,timeActivityFragment)
                    addToBackStack(null)
                    commit()
                }
            }

        }
    }

}
