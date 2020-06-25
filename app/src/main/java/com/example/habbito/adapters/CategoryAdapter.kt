package com.example.habbito.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habbito.R
import com.example.habbito.models.Category
import kotlinx.android.synthetic.main.category_item.view.*


class CategoryAdapter(private val categoryList: List<Category>,private val listner : OnItemClickListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName : TextView = itemView.tvCategoryName
        val tvCategoryType : TextView = itemView.tvCategoryType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.tvCategoryName.text = currentItem.name
        holder.tvCategoryType.text = currentItem.type
        holder.itemView.setOnClickListener {
            listner.onItemClick(currentItem)
        }

    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }
}