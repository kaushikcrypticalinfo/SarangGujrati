package com.example.saranggujrati.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.BuildConfig
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.*
import com.example.saranggujrati.databinding.ActivityCitCatBlogBinding
import com.example.saranggujrati.databinding.ActivityLoginBinding
import com.example.saranggujrati.databinding.FragmentAllNewsBlogBinding
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.activity.BaseActicvity
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.WebViewActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.CityCatBlogDetailViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import timber.log.Timber


class FragmentCityCatBlogDetail : BaseActicvity<CityCatBlogDetailViewModel>(),
    View.OnClickListener {

    private var id: String = ""

    private lateinit var liveFeedlist: ArrayList<CategoryDataModel>
    private lateinit var dummyFeedlist: ArrayList<CategoryDataModel>
    private lateinit var mActivity: MainActivity

    lateinit var feedListAdapter: FeedListAdapter

    private var blogList = ArrayList<BlogData>()

    lateinit var binding: ActivityCitCatBlogBinding

    var callCount = 0

    companion object {
        const val REQ_CODE_CITY_CAT_BLOG = 1001
        private const val EXTRA_ID = "EXTRA_ID"
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, FragmentCityCatBlogDetail::class.java)
            activity.startActivityForResult(intent, REQ_CODE_CITY_CAT_BLOG)
        }

        fun startActivity(activity: Activity, id: String) {
            val intent = Intent(activity, FragmentCityCatBlogDetail::class.java)
            intent.putExtra(EXTRA_ID, id)
            activity.startActivityForResult(intent, REQ_CODE_CITY_CAT_BLOG)
        }

    }


    override fun initializeViewModel(): CityCatBlogDetailViewModel {
        return obtainViewModel(CityCatBlogDetailViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
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
                        val i = Intent(this@FragmentCityCatBlogDetail, WebViewActivity::class.java)
                        i.putExtra("url", blogList[position].url)
                        i.putExtra("title", blogList[position].title)
                        startActivity(i)
                    }
                    /*R.id.txtReadMore -> {
                         val sendIntent = Intent()
                         sendIntent.action = Intent.ACTION_SEND
                         sendIntent.putExtra(
                             Intent.EXTRA_TEXT,
                             "Hey check out this link:" + getString(R.string.empty) + blogList[position].url + BuildConfig.APPLICATION_ID
                         )
                         sendIntent.type = "text/plain"
                         startActivity(sendIntent)
                     }*/
                }

            }
        }
        val snapHelper: SnapHelper = PagerSnapHelper()
        with(binding.rvNewsFeed)
        {
            layoutManager = LinearLayoutManager(context)
            snapHelper.attachToRecyclerView(this)
            adapter = feedListAdapter
        }

        readIntent()

        if (id.isNotEmpty()) {
            getAllBlogList(id)
        }
    }

    private fun readIntent() {
        id = intent.getStringExtra(EXTRA_ID).toString()
        Timber.e(id)
    }

    private fun getAllBlogList(id: String) {
        setupObservers(id)
        viewModel.getCityCatBlogDetail(id)
        viewModel.getRssfeedList(id)
    }

    //setup observer
    private fun setupObservers(id: String) {
        viewModel.cityCatBlogDetailResponse.observe(this, Observer { it ->
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        blogList.clear()
                        if (it.value.status) {
                            binding.progressbar.visible(false)
                            getAllBlogList(it.value, id)
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

        viewModel.feedList.observe(this, Observer { it ->
            when (it) {

                is Resource.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        if (it.value.status) {
                            binding.progressbar.visible(false)
                            val data = it.value.data

                            data.forEach {
                                liveFeedlist.add(it)
                            }

                            dummyFeedlist.addAll(data)

                            if (dummyFeedlist.isNotEmpty())
                                callLiveFeesListApi(dummyFeedlist, callCount)
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

        viewModel.feedLiveData.observe(this, Observer { it ->
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    binding.progressbar.isVisible = false
                    it.value.newsChannel?.newsItems?.forEach { rssItem ->
                        val blogData = BlogData()
                        blogData.title = rssItem.title
                        blogData.description = rssItem.description
                        blogData.category_name = liveFeedlist[callCount].rssName
                        blogData.image = rssItem.thumbnail?.thumbnailUrl.toString()
                        blogList.add(blogData)
                    }

                    feedListAdapter.notifyDataSetChanged()

                    callCount += 1
                    if (callCount < dummyFeedlist.size - 1) {
                        callLiveFeesListApi(dummyFeedlist, callCount)
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
                    }
                }
            }
        })
    }

    private fun callLiveFeesListApi(data: List<CategoryDataModel>, callCount: Int) {
        viewModel.getLiveData(data[callCount].rssUrl)
    }

    private fun getAllBlogList(response: CityCategoryBlogDetailResponse, id: String) {
        if (response.data.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.appBarLayout.visibility = View.VISIBLE

            binding.tvTitle.text = getString(R.string.app_name)
            binding.icBack.setOnClickListener {
                mActivity.onBackPressed()
            }

            binding.toolbar.setOnClickListener {
                mActivity.onBackPressed()
            }
        } else {
            for (i in response.data.indices) {
                blogList.addAll(response.data[i].blog)
            }

            feedListAdapter.notifyDataSetChanged()
        }


    }


    override fun onClick(p0: View?) {
    }

    override fun getBaseLayoutView(): View? {
        binding = ActivityCitCatBlogBinding.inflate(layoutInflater)
        return binding.root
    }

}