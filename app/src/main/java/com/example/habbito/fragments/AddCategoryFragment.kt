package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.habbito.R
import com.example.habbito.adapters.CategoryAdapter
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.models.CategoryProperty
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.AddCategoryViewModel
import com.example.habbito.viewmodelfactory.AddCategoryViewModelFactory
import com.example.habbito.viewmodelfactory.CategoryViewModelFactory
import com.example.habbito.databinding.FragmentAddCategoryBinding
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_category.*
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class AddCategoryFragment : Fragment() {
    lateinit var properties : MutableList<String>
    lateinit var selectedType : String
    lateinit var binding : FragmentAddCategoryBinding
    private lateinit var vm: AddCategoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddCategoryBinding.inflate(inflater,container, false)
        Log.d("TAGGGGGGGgg","fragment 1 napravljen")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = AddCategoryViewModelFactory(repository)
        vm = ViewModelProvider(this,factory).get(AddCategoryViewModel::class.java)



        val types = vm.types
        properties = vm.properties
        selectedType = vm.selectedType


        spinnerAddCategoryType.adapter  = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,types)

            spinnerAddCategoryType.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedType = types.get(position)
                Log.d("Proba",selectedType)
            }
        }

        val adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,properties)
        lvCategoryProperty.adapter = adapter
        btnAddCategoryProperty.setOnClickListener {
            val property = etAddCategoryProperty.text.toString()
            properties.add(property)
            Log.d("Proba",properties.toString())
            adapter.notifyDataSetChanged()

            //clear input
            etAddCategoryProperty.setText("")
        }



    setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_category_menu, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.btnSaveCategory -> {
                val categoryName = etAddCategoryName.text.toString()
                val propertyList = mutableListOf<CategoryAdditionalProperty>()
                properties.map { el ->
                    propertyList.add(CategoryAdditionalProperty(el,null))
                }
               // vm.insertCategoryWithProperty(Category(categoryName,selectedType),propertyList)
                Log.d("TAGGGGGGGgg",supportFragmentManager.fragments.size.toString())
                val fragmentManager = activity!!.supportFragmentManager
                val categoryFragment = CategoryFragment()
                fragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentHolder,categoryFragment)
                    commit()
                }
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }
}
