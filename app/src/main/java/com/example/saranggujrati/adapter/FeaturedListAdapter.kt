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
import com.example.saranggujrati.databinding.ItemTopCitiesBinding
import com.example.saranggujrati.model.FeatureData
import com.example.saranggujrati.model.TopCitiesData


class FeaturedListAdapter constructor (private var featureList: ArrayList<FeatureData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    var adapterListener: AdapterListener? = null
    lateinit var binding :ItemFeaturedStoriesBinding

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
            val response: FeatureData =featureList[position]
            holder.bind(response)


            binding.llMain.setOnClickListener {
                adapterListener?.onClick( binding.llMain, position)
            }


        }
    }
    override fun getItemCount(): Int {
        return featureList.size
}


      inner class FeatureListViewHolder constructor (private var binding: ItemFeaturedStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FeatureData) {

            binding.tvName.text = data.title
            binding.tvCount.text = data.view_count.toString()
           /* if(data.view_count!=null && data.view_count.equals("")){
                binding.tvCount.text = 0.toString()
            }else{

            }*/

            Glide.with(AppClass.appContext)
                .load(data.banner_image)
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(binding.ivImage)
        }
          }





}