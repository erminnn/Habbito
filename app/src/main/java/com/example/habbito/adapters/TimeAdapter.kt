package com.example.habbito.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habbito.R
import com.example.habbito.models.CategoryActivity
import kotlinx.android.synthetic.main.time_activity_item.view.*


class TimeAdapter(private val categoryActivityList: List<CategoryActivity>?, private val listner : OnTimeItemClickListener) : RecyclerView.Adapter<TimeAdapter.TimeActivityViewHolder>() {

    inner class TimeActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTimeActivityName : TextView = itemView.tvTimeActivityItemName
        val tvTimeActivityProperty : TextView = itemView.tvTimeActivityItemProperty
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_activity_item,parent,false)
        return TimeActivityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (categoryActivityList?.size == null) 0 else categoryActivityList.size
    }

    override fun onBindViewHolder(holder: TimeActivityViewHolder, position: Int) {
        val currentItem = categoryActivityList?.get(position)
        holder.tvTimeActivityName.text = currentItem?.name
        holder.tvTimeActivityProperty.text = currentItem?.property
        holder.itemView.setOnClickListener {
            listner.onItemClick(currentItem!!)
        }

    }

    interface OnTimeItemClickListener {
        fun onItemClick(categoryActivity: CategoryActivity)
    }

}
