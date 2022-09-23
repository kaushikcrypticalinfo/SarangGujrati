package com.saranggujrati.adapter

import android.os.Build
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.youtube.player.internal.v
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

            binding.tvNewsDetail.formatHtmlText(data.description).toString().trim()

            data.banner_image.let {
                Glide.with(AppClass.appContext)
                    .load(it[0])
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.placeholder)
                            .error(R.drawable.ic_placeholder)
                    ).into(binding.ivNewsImage)
            }

            binding.tvNewsDetail.movementMethod = ScrollingMovementMethod()

            /*val listener = OnTouchListener { v, event ->
                val isLarger: Boolean = (v as TextView).lineCount * v.lineHeight > v.getHeight()
                if (event.action === MotionEvent.ACTION_MOVE && isLarger) {
                    v.getParent().requestDisallowInterceptTouchEvent(true)
                } else {
                    v.getParent().requestDisallowInterceptTouchEvent(false)
                }
                false
            }
            binding.tvNewsDetail.setOnTouchListener(listener)*/

        }
    }
}