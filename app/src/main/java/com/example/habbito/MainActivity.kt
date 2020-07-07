package com.example.habbito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.habbito.fragments.CategoryListFragment

class MainActivity : AppCompatActivity(){
    //RIJESEN PROBLEM ZADNJEG AKTIVNOG FRAGMENTA KADA SE APP PONOVO KREIRA POSTAVLJA ZADNJI FRAGMENT KOJI JE BIO AKTIVAN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null) {
            //when app runs first time we put CategoryFragment in FragmentHolder(id of the frame layout in activity_main)
            val categoryFragment = CategoryListFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, categoryFragment)
                commit()
            }
        }else{
            //if app is destroyed and started again it puts the last acitve fragment in FragmentHolder
            val currentFragment = getTopFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentHolder, currentFragment)
                commit()
            }
        }
    }

    //Get last active fragment
    fun getTopFragment(): Fragment {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
        return currentFragment as Fragment
    }
}
