package com.example.habbito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.habbito.fragments.CategoryFragment

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
