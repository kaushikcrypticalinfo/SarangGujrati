package com.example.saranggujrati.adapter

import android.content.Context
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.saranggujrati.model.BlogData
import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*


class AllBlogViewPagerAdapter constructor (private var blogList: ArrayList<BlogData>): PagerAdapter() {

    private var mLayoutInflater: LayoutInflater? = null
    var adapterListener: AdapterListener? = null
     var adView: AdView? = null
    override fun getCount(): Int {
        return blogList.size
    }

    override fun isViewFromObject(v: View, `object`: Any): Boolean {
        // Return the current view
        return v === `object` as View
    }




    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            LayoutInflater.from(container.context).inflate(R.layout.item_news_detail, container, false)

        val data= blogList[position]

        val imageView: ImageView = (itemView).findViewById(R.id.iv_news_image)
        val ivBack: ImageView = (itemView).findViewById(R.id.ic_back)
        val ivShare: ImageView = (itemView).findViewById(R.id.ic_share)
        val tvNewsHighlight: TextView = (itemView).findViewById(R.id.tvnewsHighLight)
        val tvNewsDetail: TextView = (itemView).findViewById(R.id.tvnewsDetail)
        val tvFullStory: TextView = (itemView).findViewById(R.id.tvFullStory)
        val tvPaperName: TextView = (itemView).findViewById(R.id.tvNewsPaperName)
        adView = (itemView).findViewById(R.id.adView)

        loadBannerAd()



        tvNewsHighlight.text=data.title
        tvNewsDetail.text=data.description
        tvPaperName.text=data.category_name

        Glide.with(AppClass.appContext)
            .load(data.image)
            .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(imageView)
        ivBack.setOnClickListener {
            adapterListener?.onClick( ivBack, position)
        }

        tvFullStory.setOnClickListener {
            adapterListener?.onClick( tvFullStory, position)
        }

        ivShare.setOnClickListener {
            adapterListener?.onClick( ivShare, position)
        }


        container.addView(itemView)
        return itemView

    }



    private fun loadBannerAd(){
        val adRequest = AdRequest.Builder().build()
        adView!!.loadAd(adRequest)

        adView!!.adListener  = object : AdListener(){
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                /*val toastMessage: String = "ad fail to load"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdLoaded() {
                super.onAdLoaded()
                /*val toastMessage: String = "ad loaded"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdOpened() {
                super.onAdOpened()
               /* val toastMessage: String = "ad is open"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdClicked() {
                super.onAdClicked()
               /* val toastMessage: String = "ad is clicked"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }

            override fun onAdClosed() {
                super.onAdClosed()
                /*val toastMessage: String = "ad is closed"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdImpression() {
                super.onAdImpression()
              /*  val toastMessage: String = "ad impression"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
              /*  val toastMessage: String = "ad left application"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
        }



    }


    interface AdapterListener {
        fun onClick(view: View, position: Int)
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
