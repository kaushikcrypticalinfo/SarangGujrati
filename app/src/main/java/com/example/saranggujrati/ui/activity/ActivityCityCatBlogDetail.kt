package com.example.saranggujrati.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.*
import com.example.saranggujrati.databinding.ActivityCitCatBlogBinding
import com.example.saranggujrati.model.*
import com.example.saranggujrati.model.rssFeed.RssItems
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.CityCatBlogDetailViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import timber.log.Timber
import kotlin.collections.ArrayList


class ActivityCityCatBlogDetail : BaseActicvity<CityCatBlogDetailViewModel>(),
    View.OnClickListener {

    private var id: String = ""
    private var parentId: String = ""
    private var name: String = ""

    private lateinit var liveFeedlist: ArrayList<CategoryDataModel>
    private lateinit var dummyFeedlist: ArrayList<CategoryDataModel>
    private lateinit var mActivity: MainActivity

    lateinit var feedListAdapter: FeedListAdapter

    private var blogList = ArrayList<RssFeedModelData>()

    lateinit var binding: ActivityCitCatBlogBinding

    var callCount = 0

    companion object {
        const val REQ_CODE_CITY_CAT_BLOG = 1001
        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_PARENT_ID = "EXTRA_PARENT_ID"
        private const val EXTRA_NAME = "EXTRA_NAME"
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, ActivityCityCatBlogDetail::class.java)
            activity.startActivityForResult(intent, REQ_CODE_CITY_CAT_BLOG)
        }

        fun startActivity(activity: Activity, parentId: String, id: String, name: String) {
            val intent = Intent(activity, ActivityCityCatBlogDetail::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_PARENT_ID, parentId)
            intent.putExtra(EXTRA_NAME, name)
            activity.startActivityForResult(intent, REQ_CODE_CITY_CAT_BLOG)
        }

    }

    override fun initializeViewModel(): CityCatBlogDetailViewModel {
        return obtainViewModel(CityCatBlogDetailViewModel::class.java)
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(@NonNull p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
            }
        }
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        loadBannerAd()
        binding.icBack.setOnClickListener { finish() }

        binding.imgRefresh.setOnClickListener {
            viewModel.getRssFeedList(parentId, id)
        }

        liveFeedlist = ArrayList()
        dummyFeedlist = ArrayList()

        feedListAdapter = FeedListAdapter(blogList)

        feedListAdapter.adapterListener = object : FeedListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.ic_back -> {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }
                    R.id.txtReadMore -> {
                        val i = Intent(this@ActivityCityCatBlogDetail, WebViewActivity::class.java)
                        i.putExtra("url", blogList[position].link)
                        i.putExtra("title", blogList[position].title)
                        startActivity(i)
                    }
                }
            }
        }

        val snapHelper: SnapHelper = PagerSnapHelper()
        val linearLayoutManager = LinearLayoutManager(this)
        with(binding.rvNewsFeed)
        {
            layoutManager = linearLayoutManager
            snapHelper.attachToRecyclerView(this)
            adapter = feedListAdapter
        }

        binding.rvNewsFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val pos = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if (pos >= 0) {
                    val banner = blogList[pos].isBanner
                    binding.relativeLayout.visible(!banner)
                    binding.adView.visible(!banner)
                }
            }
        })

        readIntent()

        binding.tvTitle.text = name

        if (id.isNotEmpty()) {
            getAllBlogList(parentId, id)
        }
    }

    private fun readIntent() {
        id = intent.getStringExtra(EXTRA_ID).toString()
        parentId = intent.getStringExtra(EXTRA_PARENT_ID).toString()
        name = intent.getStringExtra(EXTRA_NAME).toString()
        Timber.e(id)
    }

    private fun getAllBlogList(parentId: String, id: String) {
        setupObservers()
        viewModel.getRssFeedList(parentId, id)
    }

    //setup observer
    private fun setupObservers() {
        viewModel.feedList.observe(this, Observer { it ->
            when (it) {

                is Resource.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    //when refresh data clear old data
                    blogList.clear()
                    if (it.value.status) {
                        if (it.value.status) {
                            binding.progressbar.visible(false)
                            val data = it.value.data
                            blogList.addAll(data)
                            var tempIndex = 2
                            SavedPrefrence.getAdsCard(this)?.data?.forEachIndexed { index, cardData ->
                                val blogData = RssFeedModelData()
                                blogData.isBanner = true
                                blogData.image = cardData.image
                                if (tempIndex < blogList.size - 1) {
                                    blogList[tempIndex] = blogData
                                    tempIndex += 6
                                }
                            }
                            feedListAdapter.notifyDataSetChanged()


                            if (blogList.isNotEmpty())
//                                callLiveFeesListApi(dummyFeedlist, callCount)
                            else {
                                binding.tvNoData.visibility = View.VISIBLE
                                binding.rvNewsFeed.visibility = View.GONE
                            }

                            binding.tvNoData.visibility =
                                if (blogList.isNotEmpty()) View.GONE else View.VISIBLE

                            binding.rvNewsFeed.visibility =
                                if (blogList.isNotEmpty()) View.VISIBLE else View.GONE
                        }
                    } else {
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }
                }
                is Resource.Failure -> {
                    binding.progressbar.isVisible = false
                    when {
                        it.isNetworkError -> {
                            if (!isOnline(AppClass.appContext)) {
                                Snackbar.make(
                                    binding.layout,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        })

    }

    override fun onClick(p0: View?) {
    }

    override fun getBaseLayoutView(): View? {
        binding = ActivityCitCatBlogBinding.inflate(layoutInflater)
        return binding.root
    }

}