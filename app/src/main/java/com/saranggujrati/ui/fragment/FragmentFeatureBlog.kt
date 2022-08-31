package com.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.adapter.*
import com.saranggujrati.databinding.FragmentFeatureBlogBinding
import com.saranggujrati.model.*
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.WebViewActivity
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.viewModel.FeatureBlogListViewModel
import com.saranggujrati.ui.visible
import com.saranggujrati.webservice.Resource


class FragmentFeatureBlog(val b: Bundle) : BaseFragment<FeatureBlogListViewModel>(),
    View.OnClickListener {

    private lateinit var mActivity: MainActivity

    private var mBlogList = ArrayList<FeatureData>()
    lateinit var binding: FragmentFeatureBlogBinding

    lateinit var allBogAdapter: FeatureStoryListAdapter

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentFeatureBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): FeatureBlogListViewModel {
        return obtainViewModel(FeatureBlogListViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.enableViews(true)
        mActivity.supportActionBar?.hide()

        binding.tvTitle.text = getString(R.string.Featured_stories)

        binding.icBack.setOnClickListener {
            mActivity.supportActionBar?.show()
            mActivity.onBackPressed()
        }

        binding.toolbar.setOnClickListener {
            mActivity.onBackPressed()
        }

        allBogAdapter = FeatureStoryListAdapter(mBlogList)
        allBogAdapter.adapterListener = object : FeatureStoryListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.txtReadMore -> {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", mBlogList[position].url)
                        i.putExtra("title", mBlogList[position].title)
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

        val bundle = b
        val pos = bundle.getInt("position")

        loadBannerAd()

        if (pos != null) {
            getAllBlogList(pos)
        }

        binding.imgRefresh.setOnClickListener {
            if (pos != null) {
                getAllBlogList(pos)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    Log.i(tag, "onKey Back listener is working!!!")
                    mActivity.supportActionBar?.show()
                    mActivity.onBackPressed()
                    return true
                }
                return false
            }
        })
    }

    private fun getAllBlogList(pos: Int) {
        viewModel.getFeatureBlogList()
        setupObservers(pos)
    }

    //setup observer
    private fun setupObservers(pos: Int) {
        viewModel.featureBlogListResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        mBlogList.clear()
                        if (it.value.status) {
                            binding.progressbar.visible(false)

                            getAllBlogList(it.value, pos)

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

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.setAdSize(getAdSize())
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



    private fun getAllBlogList(response: BlogFeatureList, pos: Int) {
        if (response.data.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.appBarLayout.visibility = View.VISIBLE
        } else {
            mBlogList.addAll(response.data)
            allBogAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(p0: View?) {
    }

}