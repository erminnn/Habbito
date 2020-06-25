package com.example.habbito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habbito.adapters.CategoryAdapter
import com.example.habbito.adapters.CategoryRvItemClick
import com.example.habbito.database.AppDatabase
import com.example.habbito.fragments.CategoryFragment
import com.example.habbito.models.Category
import com.example.habbito.models.CategoryAdditionalProperty
import com.example.habbito.repository.CategoryRepository
import com.example.habbito.viewmodel.CategoryViewModel
import com.example.habbito.viewmodelfactory.CategoryViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    //RIJESEN PROBLEM ZADNJEG AKTIVNOG FRAGMENTA KADA SE APP PONOVO KREIRA POSTAVLJA ZADNJI FRAGMENT KOJI JE BIO AKTIVAN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            val categoryFragment = CategoryFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, categoryFragment)
                commit()
            }
        }else{
            val currentFragment = getTopFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, currentFragment)
                commit()
            }
        }
    }

    fun getTopFragment(): Fragment {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
        return currentFragment as Fragment
    }
}
