package com.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.adapter.FeedListAdapter
import com.saranggujrati.model.*
import com.saranggujrati.ui.SavedPrefrence
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.WebViewActivity
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.viewModel.AllBlogListViewModel
import com.saranggujrati.ui.visible
import com.saranggujrati.webservice.Resource
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import com.saranggujrati.databinding.FragmentAllNewsBlogBinding
import java.util.*


class FragmentAllBlog : BaseFragment<AllBlogListViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    lateinit var binding: FragmentAllNewsBlogBinding

    lateinit var allBogAdapter: FeedListAdapter

    private var blogList = ArrayList<RssFeedModelData>()

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
            viewModel.getRssLatestNews()
        }

        binding.tvTitle.text = getString(R.string.latest_gujrati_news)

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
                        i.putExtra("url", blogList[position].link)
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
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    mActivity.supportActionBar?.show()
                    mActivity.onBackPressed()
                    return true
                }
                return false
            }
        })
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
//        binding.adView.setAdSize(getAdSize())
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
        }

    }
    private fun getAdSize(): AdSize {
        //Determine the screen width to use for the ad width.
        val display = requireActivity().windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), adWidth)
    }


    private fun getAllBlogList() {
        setupObservers()
        viewModel.getRssLatestNews()

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
                            blogList.addAll(data.shuffled())

                            if (blogList.isNotEmpty())
                                getAllBlogList(blogList)
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


    private fun getAllBlogList(response: ArrayList<RssFeedModelData>) {
        if (response.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvNewsFeed.visibility = View.GONE
            binding.appBarLayout.visibility = View.VISIBLE
        } else {
            // Sorting all list data recent new first show
            /*blogList.sortByDescending {
                parseDate(it.publishDate)
            }*/

            var tempIndex = 2
            SavedPrefrence.getAdsCard(requireContext())?.data?.forEachIndexed { index, cardData ->
                val blogData = RssFeedModelData()
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


}