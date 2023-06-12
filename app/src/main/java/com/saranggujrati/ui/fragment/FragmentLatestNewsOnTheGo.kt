package com.saranggujrati.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.adapter.FeedListAdapter
import com.saranggujrati.adapter.NewsOnTheGoAdapter
import com.saranggujrati.databinding.FragmentAllNewsBlogBinding
import com.saranggujrati.model.RssFeedModelData
import com.saranggujrati.ui.SavedPrefrence
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.WebViewActivity
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.viewModel.AllBlogListViewModel
import com.saranggujrati.ui.visible
import com.saranggujrati.webservice.Resource

class FragmentLatestNewsOnTheGo : BaseFragment<AllBlogListViewModel>() {
    private lateinit var mActivity: MainActivity
    lateinit var binding: FragmentAllNewsBlogBinding

    var moreClick: Boolean = false
    var backFromWebView: Boolean = false

    lateinit var newsOnTheGoAdapter : NewsOnTheGoAdapter
    private var newsList = ArrayList<RssFeedModelData>()

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): AllBlogListViewModel {
        return obtainViewModel(AllBlogListViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) { // Check if the request code matches and the result is successful
            val receivedData =
                data?.getStringExtra("close") // Replace "key" with the actual key used in Activity A
            if (receivedData == "activity") {
                backFromWebView = true

            }
            // Handle the received data here
        }
    }

    private fun pushFragment() {
        val fragmentManager = parentFragmentManager
        val FragmentLatestNewsOnTheGo = fragmentManager.findFragmentByTag("FragmentLatestNewsOnTheGo")
        if (FragmentLatestNewsOnTheGo != null) {
            fragmentManager.popBackStack()
        }
    }

    override fun onPause() {
        if (!moreClick) {
            Log.e("onPause", "onPause")
            mActivity.supportActionBar?.show()
            pushFragment()
        }
        super.onPause()
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        binding.icBack.setOnClickListener {
            mActivity.supportActionBar?.show()
            mActivity.onBackPressed()
        }

        binding.imgRefresh.setOnClickListener {
            viewModel.getRssLatestNews()
        }

        binding.adView.visibility = View.GONE

        binding.tvTitle.text = getString(R.string.gujarati_news_on_the_go)

        newsOnTheGoAdapter = NewsOnTheGoAdapter(newsList)

        newsOnTheGoAdapter.adapterListener = object : NewsOnTheGoAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.ic_back -> {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }
                    R.id.txtReadMore_news_on_the_go -> {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", newsList[position].link)
                        i.putExtra("title", newsList[position].title)
                        moreClick = true
                        startActivityForResult(i, 1)
                    }
                }
            }
        }

        binding.rvNewsFeed.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = newsOnTheGoAdapter
        }

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

        if (backFromWebView) {
            Log.e("onResume", "onResume")
            mActivity.supportActionBar?.show()
            pushFragment()
        }
    }

    private fun getAllBlogList() {
        setupObservers()
        viewModel.getRssLatestNews()

    }

    private fun setupObservers() {

        viewModel.feedList.observe(this, Observer { it ->
            when (it) {

                is Resource.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    //when refresh data clear old data
                    newsList.clear()
                    if (it.value.status) {
                        if (it.value.status) {
                            binding.progressbar.visible(false)
                            val data = it.value.data
                            newsList.addAll(data.shuffled())

                            if (newsList.isNotEmpty())
                                getAllBlogList(newsList)
                            else {
                                binding.tvNoData.visibility = View.VISIBLE
                                binding.rvNewsFeed.visibility = View.GONE
                            }

                            binding.tvNoData.visibility =
                                if (newsList.isNotEmpty()) View.GONE else View.VISIBLE

                            binding.rvNewsFeed.visibility =
                                if (newsList.isNotEmpty()) View.VISIBLE else View.GONE
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

    private fun getAllBlogList(response: java.util.ArrayList<RssFeedModelData>) {
        if (response.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.rvNewsFeed.visibility = View.GONE
            binding.appBarLayout.visibility = View.VISIBLE
        } else {
            // Sorting all list data recent new first show
            /*blogList.sortByDescending {
                parseDate(it.publishDate)
            }*/

            /*var tempIndex = 2
            SavedPrefrence.getAdsCard(requireContext())?.data?.forEachIndexed { index, cardData ->
                val blogData = RssFeedModelData()
                blogData.isBanner = true
                blogData.image = cardData.image
                if (tempIndex < newsList.size - 1) {
                    newsList[tempIndex] = blogData
                    tempIndex += 6
                }
            }*/

            newsOnTheGoAdapter.notifyDataSetChanged()
        }
    }
}