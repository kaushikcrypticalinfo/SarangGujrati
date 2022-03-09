package com.example.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.SharedMemory
import android.util.Log
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
import com.example.saranggujrati.adapter.FeedListAdapter
import com.example.saranggujrati.databinding.FragmentAllNewsBlogBinding
import com.example.saranggujrati.databinding.RRssFeedItemBinding
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.WebViewActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.AllBlogListViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.utils.*
import com.example.saranggujrati.webservice.Resource
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FragmentAllBlog : BaseFragment<AllBlogListViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    lateinit var binding: FragmentAllNewsBlogBinding

    lateinit var allBogAdapter: FeedListAdapter

    private var blogList = ArrayList<BlogData>()
    private lateinit var liveFeedlist: ArrayList<CategoryDataModel>
    private lateinit var dummyFeedlist: ArrayList<CategoryDataModel>

    var callCount = 0

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): AllBlogListViewModel {
        return obtainViewModel(AllBlogListViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {

        loadBannerAd()

        binding.icBack.setOnClickListener {
            mActivity.supportActionBar?.show()
            mActivity.onBackPressed()
        }

        binding.imgRefresh.setOnClickListener {
            viewModel.getRssfeedList("0")
        }

        binding.tvTitle.text = getString(R.string.latest_gujrati_news)

        liveFeedlist = ArrayList()
        dummyFeedlist = ArrayList()

        allBogAdapter = FeedListAdapter(blogList)

        allBogAdapter.adapterListener = object : FeedListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.ic_back -> {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }
                    R.id.txtReadMore -> {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", blogList[position].url)
                        i.putExtra("title", blogList[position].title)
                        startActivity(i)
                    }
                }
            }
        }
        val snapHelper: SnapHelper = PagerSnapHelper()
        val linearLayoutManager = LinearLayoutManager(context)
        with(binding.rvNewsFeed)
        {
            layoutManager = linearLayoutManager
            snapHelper.attachToRecyclerView(this)
            adapter = allBogAdapter
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

        mActivity = (activity as MainActivity)
        mActivity.enableViews(true)
        mActivity.supportActionBar?.hide()
        getAllBlogList()
    }


    override fun onResume() {
        super.onResume()

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    mActivity.supportActionBar?.show()
                    mActivity.onBackPressed()
                    return true;
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy();
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

    private fun getAllBlogList() {
        setupObservers()
        viewModel.getRssfeedList("0")

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

                            data.forEach {
                                liveFeedlist.add(it)
                            }

                            dummyFeedlist.addAll(data)

                            if (dummyFeedlist.isNotEmpty())
                                callLiveFeesListApi(dummyFeedlist, callCount)
                            else {
                                binding.tvNoData.visibility = View.VISIBLE
                                binding.rvNewsFeed.visibility = View.GONE
                            }

                            binding.tvNoData.visibility =
                                if (dummyFeedlist.isNotEmpty()) View.GONE else View.VISIBLE

                            binding.rvNewsFeed.visibility =
                                if (dummyFeedlist.isNotEmpty()) View.VISIBLE else View.GONE
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
                        blogData.time = rssItem.pubDate
                        blogData.url = rssItem.link
                        blogList.add(blogData)
                    }

                    callCount += 1
                    if (callCount < dummyFeedlist.size - 1) {
                        callLiveFeesListApi(dummyFeedlist, callCount)
                    } else {
                        getAllBlogList(blogList)
                    }

                }
                is Resource.Failure -> {
                    binding.progressbar.isVisible = false
                    getAllBlogList(blogList)
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

    private fun getAllBlogList(response: ArrayList<BlogData>) {
        if (response.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvNewsFeed.visibility = View.GONE
            binding.appBarLayout.visibility = View.VISIBLE
        } else {
            // Sorting all list data recent new first show
            blogList.sortByDescending {
                parseDate(it.time)
            }

            var tempIndex = 2
            SavedPrefrence.getAdsCard(requireContext())?.data?.forEachIndexed { index, cardData ->
                val blogData = BlogData()
                blogData.isBanner = true
                blogData.image = cardData.image
                if (tempIndex < blogList.size - 1) {
                    blogList[tempIndex] = blogData
                    tempIndex += 6
                }
            }

            allBogAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    @Throws(Exception::class)
    fun parseDate(strDate: String?): Date? {
        if (strDate != null && !strDate.isEmpty()) {
            val formats = arrayOf(
                SimpleDateFormat(RSS_FEED_DATE_FORMAT),
                SimpleDateFormat(RSS_FEED_DATE_FORMAT_GMT)
            )
            var parsedDate: Date? = null
            for (i in formats.indices) {
                return try {
                    parsedDate = formats[i].parse(strDate)
                    parsedDate
                } catch (e: ParseException) {
                    continue
                }
            }
        }
        throw Exception("Unknown date format: '$strDate'")
    }
}