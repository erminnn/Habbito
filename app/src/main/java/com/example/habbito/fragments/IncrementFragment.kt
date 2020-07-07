package com.example.habbito.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.habbito.R

/**
 * A simple [Fragment] subclass.
 * Use the [IncrementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncrementFragment : Fragment() {
    private val CURRENT_VALUE = "id"
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(CURRENT_VALUE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_increment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(currValue: Long) =
            IncrementFragment().apply {
                arguments = Bundle().apply {
                    putLong(CURRENT_VALUE, currValue)
                }
            }
    }
}
