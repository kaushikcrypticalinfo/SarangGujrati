package com.saranggujrati.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saranggujrati.databinding.ItemTopCitiesBinding
import com.saranggujrati.model.CityCatageoryChild


class TopCitiesAdapter constructor(private var selfList: ArrayList<CityCatageoryChild>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var adapterListener: AdapterListener? = null
    lateinit var binding: ItemTopCitiesBinding

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemTopCitiesBinding.inflate(inflater, parent, false)
        return CitiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CitiesViewHolder) {
            val response: CityCatageoryChild = selfList[position]
            holder.bind(response)

        }
    }

    override fun getItemCount(): Int {
        return selfList.size
    }

    inner class CitiesViewHolder constructor(private var binding: ItemTopCitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CityCatageoryChild) {
            binding.tvCity.text = data.name
            binding.tvCity.setOnClickListener {
                adapterListener?.onClick(binding.tvCity, absoluteAdapterPosition)
            }

        }
    }


}