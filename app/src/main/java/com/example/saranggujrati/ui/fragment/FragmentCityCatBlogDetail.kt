package com.example.saranggujrati.ui.fragment

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
import com.example.saranggujrati.databinding.FragmentAllNewsBlogBinding
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.WebViewActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.CityCatBlogDetailViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import timber.log.Timber


class FragmentCityCatBlogDetail(val b: Bundle) : BaseFragment<CityCatBlogDetailViewModel>(),
    View.OnClickListener {

    private lateinit var liveFeedlist: ArrayList<CategoryDataModel>
    private lateinit var mActivity: MainActivity

    lateinit var feedListAdapter: FeedListAdapter

    private var blogList = ArrayList<BlogData>()

    lateinit var binding: FragmentAllNewsBlogBinding


    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): CityCatBlogDetailViewModel {
        return obtainViewModel(CityCatBlogDetailViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        liveFeedlist = ArrayList()
        feedListAdapter = FeedListAdapter(blogList)

        feedListAdapter.adapterListener = object : FeedListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.ic_back -> {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }
                    R.id.tvFullStory -> {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", blogList[position].url)
                        i.putExtra("title", blogList[position].title)
                        startActivity(i)
                    }
                    R.id.ic_share -> {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey check out this link:" + getString(R.string.empty) + blogList[position].url + BuildConfig.APPLICATION_ID
                        )
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    }
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


        val bundle = b
        val id = bundle.getString("id")
        Timber.e(id.toString())
        mActivity = (activity as MainActivity)
        mActivity.enableViews(true)
        mActivity.supportActionBar?.hide()
        if (id != null) {
            getAllBlogList(id)
        }
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    Timber.i("onKey Back listener is working!!!")
                    mActivity.supportActionBar?.show()
                    mActivity.onBackPressed()
                    return true
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
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
                        blogList.clear()
                        if (it.value.status) {
                            binding.progressbar.visible(false)
                            val data = it.value.data
                            liveFeedlist.addAll(data)


                            data.forEach {
                                viewModel.getLiveData(it.rssUrl)
                            }
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

                    liveFeedlist.map { Timber.e(it.rssUrl) }

                    val elements = it.value.newsChannel!!.links?.map { it.href }?.toList()!!
                    it.value.newsChannel?.newsItems?.forEach { rssItem ->
                        val blogData = BlogData()
                        blogData.title = rssItem.title
                        blogData.description = rssItem.description
                        blogData.category_name =
                            liveFeedlist.find {urlLink-> elements.contains(urlLink.rssUrl) }
                                ?.categoryName
                                ?: kotlin.run { "" }
                        blogData.image = rssItem.thumbnail?.thumbnailUrl.toString()

                        blogList.add(blogData)
                    }

                    feedListAdapter.notifyDataSetChanged()

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

    private fun getAllBlogList(response: CityCategoryBlogDetailResponse, id: String) {
        if (response.data.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.appBarLayout.visibility = View.VISIBLE
            binding.verticalViewPager.visibility = View.GONE

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
        TODO("Not yet implemented")
    }

}