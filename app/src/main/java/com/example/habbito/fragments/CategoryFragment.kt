package com.example.habbito.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.habbito.R
import com.example.habbito.adapters.CategoryAdapter
import com.example.habbito.database.AppDatabase
import com.example.habbito.models.Category
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryViewModel
import com.example.habbito.viewmodelfactory.CategoryViewModelFactory
import kotlinx.android.synthetic.main.fragment_category.*
import com.example.habbito.adapters.CategoryAdapter.OnItemClickListener

/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : Fragment(), OnItemClickListener {
    private lateinit var vm: CategoryViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext())!!.categoryDao
        val repository = CategoryRepository(dao)
        val factory = CategoryViewModelFactory(repository)
        vm = ViewModelProvider(this,factory).get(CategoryViewModel::class.java)
        vm.allCategories.observe(this, Observer { items ->
            categoryRecyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = CategoryAdapter(items,this)
            }
        })

        btnAddNewCategory.setOnClickListener{
            val fragmentManager = activity!!.supportFragmentManager
            val addCategoryFragment = AddCategoryFragment()
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder,addCategoryFragment)
                commit()
            }
        }
    }

    override fun onItemClick(category: Category) {
        val fragmentManager = activity!!.supportFragmentManager
        val timer = Timer()
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder,timer)
            commit()
        }
    }
}
