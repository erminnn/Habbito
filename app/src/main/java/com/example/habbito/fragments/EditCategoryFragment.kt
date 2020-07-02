package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryViewModel
import com.example.habbito.viewmodelfactory.CategoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_edit_category.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class EditCategoryFragment : Fragment() {
    var id : Long = 0
    val properties = mutableListOf<String>()
     var selectedType : String = ""
    lateinit var adapter : ArrayAdapter<String>
    private lateinit var vm: CategoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_edit_category, container, false)
        try {
            val bundle : Bundle = this.requireArguments()
            id = bundle.getLong("id")
        }catch (e : Exception){
            Log.d("Error",e.toString())
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repository)
        vm = ViewModelProvider(this,factory).get(CategoryViewModel::class.java)
        vm.getAllPropertiesForCategory(id).observe(this, Observer {
            it.map { el ->
                properties.add(el.name)
            }

            adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,properties)
            lvEditCategoryProperty.adapter = adapter
        })
        vm.getCategoryById(id).observe(this, Observer {
            etEditCategoryName.setText(it.name)
            selectedType = it.type
            val types =  listOf("Time","Numerical","Quantitative")
            val selectedTypeId = types.indexOf(selectedType)




            spinnerEditCategoryType.adapter  = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,types)
            spinnerEditCategoryType.setSelection(selectedTypeId)

            spinnerEditCategoryType.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedType = types.get(position)
                }
            }
        })

        btnEditCategoryProperty.setOnClickListener {
            val property = etEditCategoryProperty.text.toString()
            if(property != ""){
                properties.add(property)
                adapter.notifyDataSetChanged()
                etEditCategoryProperty.setText("")
            }else{
                Toast.makeText(requireContext(),"Property name is empty", Toast.LENGTH_SHORT).show()
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_category_menu, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.btnSaveEditedCategory -> {
                vm.deleteCategoryWithProperties(id)
                val categoryName = etEditCategoryName.text.toString()
                if(categoryName != "") {
                    val propertyList = mutableListOf<CategoryAdditionalProperty>()
                    val fragmentManager = activity!!.supportFragmentManager
                    val categoryFragment = CategoryFragment()
                    properties.map { el ->
                        propertyList.add(CategoryAdditionalProperty(el, null))
                    }
                    vm.insertCategoryWithProperty(Category(categoryName,selectedType),propertyList)
                    fragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentHolder,categoryFragment)
                        commit()
                    }
                }else{
                    Toast.makeText(requireContext(),"Category name is empty",Toast.LENGTH_SHORT).show()
                }
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

}
