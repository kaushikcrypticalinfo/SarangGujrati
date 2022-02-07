package com.example.saranggujrati.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.ItemTopCategoriesBinding
import com.example.saranggujrati.databinding.ItemTopCitiesBinding
import com.example.saranggujrati.model.CityCatageoryChild
import com.example.saranggujrati.model.CityCategoryListData
import com.example.saranggujrati.model.TopCitiesData


class CategoryListAdapter constructor (private var categoryList: ArrayList<CityCatageoryChild>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    var adapterListener: AdapterListener? = null
    lateinit var binding :ItemTopCategoriesBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
         binding = ItemTopCategoriesBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val response: CityCatageoryChild =categoryList[position]
            holder.bind(response)


            binding.llMain.setOnClickListener {
                adapterListener?.onClick( binding.llMain, position)
            }


        }
    }
    override fun getItemCount(): Int {
        return categoryList.size
}


      inner class CategoryViewHolder constructor (private var binding: ItemTopCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
          fun bind(data: CityCatageoryChild) {


                  binding.tvCategory.text = data.name

                  Glide.with(AppClass.appContext)
                      .load(data.image)
                      .apply(RequestOptions.placeholderOf(R.drawable.placeholder)
                          .error(R.drawable.placeholder))
                      .into(binding.ivCategoryImage)




          }
      }



}