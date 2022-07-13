package com.saranggujrati.adapter

import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.databinding.ItemNewsChannelBinding
import com.saranggujrati.model.NewsData


class PagingDemoAdapter :
    PagingDataAdapter<NewsData, PagingDemoAdapter.ItemViewHolder>(PlayersDiffCallback()) {

    var adapterListener: AdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsChannelBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder constructor(private var binding: ItemNewsChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsData?) {
            binding.tvName.text = data?.company_name
            Glide.with(AppClass.appContext)
                .load(data?.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                )
                .into(binding.ivNews);

            binding.llMain.setOnClickListener {
                adapterListener?.onClick(binding.llMain, adapterPosition)
            }
        }

    }

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    private class PlayersDiffCallback : DiffUtil.ItemCallback<NewsData>() {
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }


}


