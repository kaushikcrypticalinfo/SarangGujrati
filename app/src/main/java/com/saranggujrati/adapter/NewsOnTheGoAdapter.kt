package com.saranggujrati.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.databinding.ItemNewsOnTheGoBinding
import com.saranggujrati.model.RssFeedModelData
import com.saranggujrati.ui.SavedPrefrence

class NewsOnTheGoAdapter(private var newsList: ArrayList<RssFeedModelData>) :
    RecyclerView.Adapter<ViewHolder>() {

    var adapterListener: AdapterListener? = null
    lateinit var binding: ItemNewsOnTheGoBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemNewsOnTheGoBinding.inflate(inflater, parent, false)
        return NewsOnTheGoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is NewsOnTheGoAdapter.NewsOnTheGoViewHolder) {
            val response: RssFeedModelData = newsList[position]
            holder.bind(response)
        }

        if (SavedPrefrence.getIsDarkMode(binding.dividerLineTwo.context)!!) {
            binding.dividerLineOne.setBackgroundColor(Color.parseColor("#ffffff"))
            binding.dividerLineTwo.setBackgroundColor(Color.parseColor("#ffffff"))
            binding.dividerLineThree.setBackgroundColor(Color.parseColor("#ffffff"))
            binding.dividerLineFour.setBackgroundColor(Color.parseColor("#ffffff"))
        }

        if (position % 3 == 0) {
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
            binding.adView.adListener = object : AdListener() {
            }
            binding.groupAdView.visibility = View.VISIBLE
        } else {
            binding.groupAdView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsOnTheGoViewHolder(private val binding: ItemNewsOnTheGoBinding) :
        ViewHolder(binding.root) {
        fun bind(data: RssFeedModelData) {

            binding.tvNewsHighLightNewsOnTheGo.text = data.title

            binding.tvNewsPaperNameNewsOnTheGo.text = data.feedType

            Glide.with(AppClass.appContext)
                .load(data.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.ic_placeholder)
                ).into(binding.imgNewsOnTheGo)

            binding.txtReadMoreNewsOnTheGo.setOnClickListener {
                adapterListener?.onClick(it, absoluteAdapterPosition)
            }
        }
    }
}