package com.example.saranggujrati.adapter

import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.ItemNewsChannelBinding
import com.example.saranggujrati.databinding.RLoadingBinding
import com.example.saranggujrati.loadmore.LoadMoreConstant
import com.example.saranggujrati.model.NewsData
import com.example.saranggujrati.ui.activity.MainActivity


class AllNewsChannelAdapter constructor (private var itemList: ArrayList<NewsData?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var adapterListener: AdapterListener? = null
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == LoadMoreConstant. VIEW_TYPE_ITEM) {

                val binding = ItemNewsChannelBinding.inflate(inflater, parent, false)
                return ItemViewHolder(binding)

            }else{

               val binding = RLoadingBinding.inflate(inflater, parent, false)
                return LoadingViewHolder(binding)



        }



    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(itemList[position])
        } else if (holder is LoadingViewHolder) {
            holder.bind()
        }

    }

    override fun getItemCount(): Int {

        return if (itemList == null) 0 else itemList.size


     //   return itemList.size
    }



    override fun getItemViewType(position: Int): Int {

        return if (itemList.get(position) == null)   LoadMoreConstant.VIEW_TYPE_LOADING else  LoadMoreConstant.VIEW_TYPE_ITEM

       /*return if (itemList[position] == null) {
            LoadMoreConstant.VIEW_TYPE_LOADING
        } else {
            LoadMoreConstant.VIEW_TYPE_ITEM
        }*/


    }

     fun remove(position: Int) {
         itemList.removeAt(position)
         notifyItemRemoved(position)



     }

    fun getList(): ArrayList<NewsData?> {
       return itemList



    }
    fun clear() {
        this.itemList.clear()
        notifyDataSetChanged()
    }



    fun setList(list: ArrayList<NewsData?>) {
        itemList = list
        notifyDataSetChanged()
    }



    inner  class ItemViewHolder constructor (private var binding: ItemNewsChannelBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(data: NewsData?) {


            binding.tvName.text=data?.company_name



            Glide.with(AppClass.appContext)
                .load(data?.image)
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(binding.ivNews);

            binding.llMain.setOnClickListener {
                adapterListener?.onClick( binding.llMain, adapterPosition)
            }
        }

    }

  inner  class LoadingViewHolder(private var binding: RLoadingBinding) : RecyclerView.ViewHolder(binding.root){
      fun bind(){
          binding.progressBar.visibility = View.VISIBLE
      }
  }


    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

}


