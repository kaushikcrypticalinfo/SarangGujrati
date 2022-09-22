package com.saranggujrati.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.databinding.RTopMenuBinding
import com.saranggujrati.model.ApiRecordData


class TopMenuAdapter constructor(private var categoryList: ArrayList<ApiRecordData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var adapterListener: AdapterListener? = null
    lateinit var binding: RTopMenuBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RTopMenuBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val response: ApiRecordData = categoryList[position]
            holder.bind(response)

        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


    inner class CategoryViewHolder constructor(private var binding: RTopMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ApiRecordData) {
            binding.tvLatestNews.text = data.name
            changeShapeBgColor(
                binding.liMain, when (data.id) {
                    1 -> R.color.purple
                    2 -> R.color.light_red
                    3 -> R.color.app_blue
                    4 -> R.color.skyblue
                    else -> R.color.purple
                }
            )


            Glide.with(AppClass.appContext)
                .load(
                    when (data.id) {
                        1 -> R.drawable.ic_letest_gujarati_news
                        2 -> R.drawable.ic_all_gujarati_news
                        3 -> R.drawable.ic_letest_gujarati_news
                        4 -> R.drawable.ic_live_news_channels
                        else -> R.drawable.ic_letest_gujarati_news
                    }
                )
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                )
                .into(binding.imgLeft)

            binding.liMain.setOnClickListener {
                adapterListener?.onClick(binding.liMain, data.id!!)
            }
        }

        private fun changeShapeBgColor(liMain: View, purple: Int) {
            val background: Drawable = liMain.background
            val color = ContextCompat.getColor(
                liMain.context,
                purple
            )
            when (background) {
                is ShapeDrawable -> {
                    background.paint.color =
                        color
                }
                is GradientDrawable -> {
                    background.setColor(
                        color
                    )
                }
                is ColorDrawable -> {
                    background.color =
                        color
                }
            }
        }
    }


}