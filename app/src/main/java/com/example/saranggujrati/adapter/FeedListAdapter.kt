package com.example.saranggujrati.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.RRssFeedItemBinding
import com.example.saranggujrati.extensions.formatHtmlText
import com.example.saranggujrati.model.BlogData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError

class FeedListAdapter constructor(private var categoryList: ArrayList<BlogData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var adapterListener: AdapterListener? = null
    lateinit var binding: RRssFeedItemBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RRssFeedItemBinding.inflate(inflater, parent, false)
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

    inner class CategoryViewHolder(private var binding: RRssFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: BlogData) {

            binding.groupNews.visibility = if (data.isBanner) GONE else VISIBLE
            binding.imgBanner.visibility = if (data.isBanner) VISIBLE else GONE

            if (data.isBanner)
                Glide.with(AppClass.appContext)
                    .load(data.image)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                    ).into(binding.imgBanner)


            binding.tvNewsHighLight.text = data.title

            binding.tvNewsPaperName.text = data.category_name

            binding.tvNewsDetail.formatHtmlText(
                Html.fromHtml(data.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            )

            Glide.with(AppClass.appContext)
                .load(data.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                ).into(binding.ivNewsImage)

            binding.txtReadMore.setOnClickListener {
                adapterListener?.onClick(it, adapterPosition)
            }

        }
    }



}