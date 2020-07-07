package com.example.habbito.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbito.R
import com.example.habbito.adapters.CategoryAdapter
import com.example.habbito.adapters.CategoryAdapter.OnItemClickListener
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryListViewModel
import com.example.habbito.viewmodelfactory.CategoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CategoryListFragment : Fragment(), OnItemClickListener {
    private lateinit var vm: CategoryListViewModel
    private val fragmentJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + fragmentJob)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(CategoryListViewModel::class.java)
        vm.allCategories.observe(this, Observer { items ->
            categoryRecyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = CategoryAdapter(items, this)
            }
        })

        btnAddNewCategory.setOnClickListener {
            val fragmentManager = activity!!.supportFragmentManager
            val addCategoryFragment = AddCategoryFragment()
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, addCategoryFragment)
                commit()
            }
        }
    }

    //kada se klikne item u category fragment
    override fun onItemClick(category: Category) {
        uiScope.launch {
            val allPropertiesForCategory = vm.getAllPropertiesForCategory(category.id).value
            val properties = ArrayList<String>()
            val fragmentManager = activity!!.supportFragmentManager
            val activityListFragment = ActivityListFragment()
            val bundle = Bundle()
            if (allPropertiesForCategory != null && allPropertiesForCategory.isNotEmpty()) {
                allPropertiesForCategory.map { e -> properties.add(e.name) }
                bundle.putStringArrayList("properties", properties)
                activityListFragment.arguments = bundle

            } else {
                bundle.putStringArrayList("properties", properties)
                bundle.putLong("categoryId", category.id)
                activityListFragment.arguments = bundle
            }
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, activityListFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onItemClickEdit(category: Category) {
        val fragmentManager = activity!!.supportFragmentManager
        val editCategoryFragment = EditCategoryFragment()
        val bundle: Bundle = Bundle()
        bundle.putLong("id", category.id)
        editCategoryFragment.arguments = bundle
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, editCategoryFragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onItemClickDelete(category: Category) {
        vm.deleteCategoryWithProperties(category.id)
    }

}