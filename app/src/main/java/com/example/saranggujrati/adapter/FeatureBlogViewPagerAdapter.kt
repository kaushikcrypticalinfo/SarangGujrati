package com.example.saranggujrati.adapter

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.model.FeatureData
import com.example.saranggujrati.ui.visible
import com.google.android.gms.ads.*
import java.util.*


class FeatureBlogViewPagerAdapter constructor(private var blogList: ArrayList<FeatureData>) :
    PagerAdapter() {

    private var mLayoutInflater: LayoutInflater? = null
    var adapterListener: AdapterListener? = null

    override fun getCount(): Int {
        return blogList.size
    }

    override fun isViewFromObject(v: View, `object`: Any): Boolean {
        // Return the current view
        return v === `object` as View
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            LayoutInflater.from(container.context)
                .inflate(R.layout.item_news_detail, container, false)

        val data = blogList[position]

        val imageView: ImageView = (itemView).findViewById(R.id.iv_news_image)
        val txtReadMore: AppCompatTextView = (itemView).findViewById(R.id.txtReadMore)
        val tvNewsHighlight: TextView = (itemView).findViewById(R.id.tvNewsHighLight)
        val tvNewsDetail: TextView = (itemView).findViewById(R.id.tvNewsDetail)

        txtReadMore.visible(false)

        val tvPaperName: TextView = (itemView).findViewById(R.id.tvNewsPaperName)
        tvPaperName.visible(false)

        tvNewsHighlight.text = data.title
        tvNewsDetail.text = data.description
        tvPaperName.text = data.category_name

        data.banner_image?.let {
            Glide.with(AppClass.appContext)
                .load(it[0])
                .apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                )
                .into(imageView)
        }

        txtReadMore.setOnClickListener {
            adapterListener?.onClick(txtReadMore, position)
        }


        container.addView(itemView)
        return itemView

    }

    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
