package com.example.saranggujrati.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.ItemFeaturedStoriesBinding
import com.example.saranggujrati.databinding.ItemOnDemandBinding
import com.example.saranggujrati.model.onDemand.OnDemandData


class OnDemandListAdapter(private var featureList: ArrayList<OnDemandData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var adapterListener: AdapterListener? = null
    lateinit var binding: ItemOnDemandBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemOnDemandBinding.inflate(inflater, parent, false)
        return FeatureListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FeatureListViewHolder) {
            val response: OnDemandData = featureList[position]
            holder.bind(response)
        }
    }

    override fun getItemCount(): Int {
        return featureList.size
    }

    inner class FeatureListViewHolder constructor(private var binding: ItemOnDemandBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OnDemandData) {
            binding.tvName.text = data.title
            Glide.with(AppClass.appContext)
                .load(data.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                ).into(binding.ivImage)

            binding.llMain.setOnClickListener {
                adapterListener?.onClick(binding.llMain, absoluteAdapterPosition)
            }
        }
    }

}