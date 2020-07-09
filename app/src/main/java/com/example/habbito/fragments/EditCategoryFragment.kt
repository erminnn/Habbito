package com.example.habbito.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.habbito.R
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryListViewModel
import com.example.habbito.viewmodelfactory.CategoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class EditCategoryFragment : Fragment() {
    var id: Long = 0
    val properties = mutableListOf<String>()
    var selectedType: String = ""
    lateinit var adapter: ArrayAdapter<String>
    private lateinit var vm: CategoryListViewModel

    private var fragmentJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + fragmentJob)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_category, container, false)
        setTitle()
        try {
            val bundle: Bundle = this.requireArguments()
            id = bundle.getLong("id")
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(CategoryListViewModel::class.java)
        observeCategoryProperties()
        vm.initCategory(id)
        observeCategoryToEdit()
        btnEditCategoryProperty.setOnClickListener {
            val property = etEditCategoryProperty.text.toString()
            if (property != "") {
                properties.add(property)
                adapter.notifyDataSetChanged()
                etEditCategoryProperty.setText("")
            } else {
                Toast.makeText(requireContext(), "Property name is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_category_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.btnSaveEditedCategory -> {
                uiScope.launch {
                    vm.deleteCategory(id)
                    val categoryName = etEditCategoryName.text.toString()
                    if (categoryName != "") {
                        val propertyList = mutableListOf<CategoryAdditionalProperty>()
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
                        Toast.makeText(
                            requireContext(),
                            "Category name is empty",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                true
            }
            else ->  super.onOptionsItemSelected(item)
        })
    }

    private fun observeCategoryToEdit() {
        vm.categoryToEdit.observe(viewLifecycleOwner, Observer {
            etEditCategoryName.setText(it.name)
            selectedType = it.type
            val types = listOf("Time", "Numerical", "Quantitative")
            val selectedTypeId = types.indexOf(selectedType)

            spinnerEditCategoryType.adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                types
            )
            spinnerEditCategoryType.setSelection(selectedTypeId)

            spinnerEditCategoryType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        selectedType = types.get(position)
                    }
                }
        })
    }

    private fun observeCategoryProperties() {
        vm.getAllPropertiesForCategory(id).observe(viewLifecycleOwner, Observer {
            it.map { el -> properties.add(el.name) }
            adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                properties
            )
            lvEditCategoryProperty.adapter = adapter
        })
    }

    private fun setTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.edit_category)
    }

}
