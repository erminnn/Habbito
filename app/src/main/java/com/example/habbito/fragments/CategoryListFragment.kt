package com.example.habbito.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        setTitle()
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repository)
        vm = ViewModelProvider(this, factory).get(CategoryListViewModel::class.java)
        vm.allCategories.observe(viewLifecycleOwner, Observer { items ->
            categoryRecyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = CategoryAdapter(items, this)
            }
        })

        btnAddNewCategory.setOnClickListener {
            val addCategoryFragment = AddCategoryFragment()
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, addCategoryFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    //kada se klikne item u category fragment
    override fun onItemClick(category: Category) {
        uiScope.launch {
            val properties = ArrayList<String>()
            val activityListFragment = ActivityListFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("properties", properties)
            bundle.putLong("categoryId", category.id)
            activityListFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, activityListFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onItemClickEdit(category: Category) {
        val editCategoryFragment = EditCategoryFragment()
        val bundle = Bundle()
        bundle.putLong("id", category.id)
        editCategoryFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, editCategoryFragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onItemClickDelete(category: Category) {
        uiScope.launch { vm.deleteCategoryWithProperties(category.id) }
    }

    private fun setTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.habito)
    }

}
