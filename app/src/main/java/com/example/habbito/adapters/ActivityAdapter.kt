package com.example.habbito.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habbito.R
import com.example.habbito.models.CategoryActivity
import kotlinx.android.synthetic.main.time_activity_item.view.*


class ActivityAdapter(private val activityList: List<CategoryActivity>?, private val listner : OnTimeItemClickListener) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvActivityName : TextView = itemView.tvActivityItemName
        val tvActivityProperty : TextView = itemView.tvActivityItemProperty
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_activity_item,parent,false)
        return ActivityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (activityList?.size == null) 0 else activityList.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val currentItem = activityList?.get(position)
        holder.tvActivityName.text = currentItem?.name
        holder.tvActivityProperty.text = currentItem?.property
        holder.itemView.setOnClickListener {
            listner.onItemClick(currentItem!!)
        }

    }

    interface OnTimeItemClickListener {
        fun onItemClick(categoryActivity: CategoryActivity)
    }

}
