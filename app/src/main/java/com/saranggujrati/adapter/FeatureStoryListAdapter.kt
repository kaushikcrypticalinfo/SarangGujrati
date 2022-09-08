package com.saranggujrati.adapter

import android.os.Build
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.databinding.ItemNewsDetailBinding
import com.saranggujrati.extensions.formatHtmlText
import com.saranggujrati.model.FeatureData
import com.saranggujrati.ui.visible


class FeatureStoryListAdapter(private var categoryList: ArrayList<FeatureData>) :
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
            val response: FeatureData = categoryList[position]
            holder.bind(response)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryViewHolder(private var binding: ItemNewsDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(data: FeatureData) {
            binding.tvNewsPaperName.visible(false)
            binding.txtReadMore.visible(false)

            binding.tvNewsHighLight.text = data.title

            binding.tvNewsDetail.formatHtmlText(Html.fromHtml(data.description.trim(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString())
            binding.tvNewsDetail.movementMethod = ScrollingMovementMethod()

            data.banner_image?.let {
                Glide.with(AppClass.appContext)
                    .load(it[0])
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.placeholder)
                            .error(R.drawable.ic_placeholder)
                    ).into(binding.ivNewsImage)
            }


        }
    }
}