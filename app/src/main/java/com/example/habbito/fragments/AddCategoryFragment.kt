package com.example.habbito.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.AddCategoryViewModel
import com.example.habbito.viewmodelfactory.AddCategoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_category.*


/**
 * A simple [Fragment] subclass.
 */
class AddCategoryFragment : Fragment() {
    lateinit var properties: MutableList<String>
    lateinit var selectedType: String
    private lateinit var vm: AddCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)
        setTitle()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = AddCategoryViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(AddCategoryViewModel::class.java)
        val types = vm.types
        properties = vm.properties
        selectedType = vm.selectedType

        spinnerAddCategoryType.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            types
        )

        initCategoryTypesListener(types)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            properties
        )
        lvCategoryProperty.adapter = adapter
        initAddCategoryPropertyListener(adapter)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_category_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.btnSaveCategory -> {
                val categoryName = etAddCategoryName.text.toString()
                if (categoryName != "") {
                    val propertyList = mutableListOf<CategoryAdditionalProperty>()
                    val fragmentManager = activity!!.supportFragmentManager
                    val categoryFragment = CategoryListFragment()
                    properties.map { el ->
                        propertyList.add(CategoryAdditionalProperty(el, null))
                    }
                    vm.insertCategoryWithProperty(
                        Category(categoryName, selectedType),
                        propertyList
                    )
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentHolder, categoryFragment)
                        commit()
                    }
                } else {
                    Toast.makeText(requireContext(), "Category name is empty", Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    private fun initAddCategoryPropertyListener(adapter: ArrayAdapter<String>) {
        btnAddCategoryProperty.setOnClickListener {
            val property = etAddCategoryProperty.text.toString()
            if (property != "") {
                properties.add(property)
                adapter.notifyDataSetChanged()
                etAddCategoryProperty.setText("")
            } else {
                Toast.makeText(requireContext(), "Property name is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initCategoryTypesListener(types: List<String>) {
        spinnerAddCategoryType.onItemSelectedListener =
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
                    selectedType = types[position]
                }
            }
    }

    private fun setTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.add_category)
    }

}
