package com.example.saranggujrati.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.ItemNewsDetailBinding
import com.example.saranggujrati.databinding.ItemTopCitiesBinding
import com.example.saranggujrati.model.BlogData
import com.example.saranggujrati.model.TopCitiesData


class AllBlogListAdapter constructor (private var blogList: ArrayList<BlogData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var adapterListener: AdapterListener? = null
    lateinit var binding :ItemNewsDetailBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
         binding = ItemNewsDetailBinding.inflate(inflater, parent, false)
        return AllBlogListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AllBlogListViewHolder) {
            val response: BlogData =blogList[position]
            holder.bind(response)
        }
    }
    override fun getItemCount(): Int {
        return blogList.size
}


      inner class AllBlogListViewHolder constructor (private var binding: ItemNewsDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BlogData) {

            binding.tvnewsHighLight.text=data.title
            binding.tvnewsDetail.text=data.description
            binding.tvNewsPaperName.text=data.category_name

            Glide.with(AppClass.appContext)
                .load(data?.image)
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(binding.ivNewsImage)

        }
          }





}