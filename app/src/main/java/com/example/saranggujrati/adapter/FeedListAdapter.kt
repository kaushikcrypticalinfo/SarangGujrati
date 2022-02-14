package com.example.saranggujrati.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.ItemNewsDetailBinding
import com.example.saranggujrati.extensions.formatHtmlText
import com.example.saranggujrati.model.BlogData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError

class FeedListAdapter constructor(private var categoryList: ArrayList<BlogData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var adapterListener: AdapterListener? = null
    lateinit var binding: ItemNewsDetailBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemNewsDetailBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val response: BlogData = categoryList[position]
            holder.bind(response)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryViewHolder(private var binding: ItemNewsDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: BlogData) {

            binding.tvNewsHighLight.text = data.title

            binding.tvNewsPaperName.text = data.category_name

            binding.tvNewsDetail.formatHtmlText(
                Html.fromHtml(data.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            )

            loadBannerAd(binding)

            Glide.with(AppClass.appContext)
                .load(data.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                ).into(binding.ivNewsImage)

            loadBannerAd(binding)

            binding.icBack.setOnClickListener {
                adapterListener?.onClick(it, adapterPosition)
            }

            binding.txtReadMore.setOnClickListener {
                adapterListener?.onClick(it, adapterPosition)
            }

        }
    }


    private fun loadBannerAd(binding: ItemNewsDetailBinding) {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(@NonNull p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
            }
        }
    }


}