package com.example.habbito.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habbito.R
import com.example.habbito.databinding.CategoryItemBindingImpl
import com.example.habbito.models.Category


class CategoryAdapter(private val categoryList: List<Category>,private val listner : CategoryRvItemClick) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding : CategoryItemBindingImpl) : RecyclerView.ViewHolder(binding.root){
        fun bind(category : Category){
            binding.tvCategoryName.text = category.name
            binding.tvCategoryType.text = category.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= CategoryViewHolder(
        DataBindingUtil.inflate<CategoryItemBindingImpl>(LayoutInflater.from(parent.context),R.layout.category_item,parent,false)
    )

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.category = categoryList.get(position)
        holder.binding.categoryItem.setOnClickListener{
            listner.onItemClick(categoryList.get(position))
        }
    }
}