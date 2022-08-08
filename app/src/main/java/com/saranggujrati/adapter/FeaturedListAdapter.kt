package com.saranggujrati.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.databinding.ItemFeaturedStoriesBinding
import com.saranggujrati.model.FeatureData


class FeaturedListAdapter constructor(private var featureList: ArrayList<FeatureData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var adapterListener: AdapterListener? = null
    lateinit var binding: ItemFeaturedStoriesBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemFeaturedStoriesBinding.inflate(inflater, parent, false)
        return FeatureListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FeatureListViewHolder) {
            val response: FeatureData = featureList[position]
            holder.bind(response)


        }
    }

    override fun getItemCount(): Int {
        return featureList.size
    }


    inner class FeatureListViewHolder constructor(private var binding: ItemFeaturedStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FeatureData) {


            binding.tvName.text = data.title
            binding.tvCount.text = data.view_count.toString()

            data.banner_image?.let {
                if (it.isNotEmpty()) {
                    Glide.with(AppClass.appContext)
                        .load(data.banner_image[0])
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                        ).into(binding.ivImage)
                }
            }

            binding.llMain.setOnClickListener {
                adapterListener?.onClick(
                    it,
                    absoluteAdapterPosition
                )
            }


        }
    }


}