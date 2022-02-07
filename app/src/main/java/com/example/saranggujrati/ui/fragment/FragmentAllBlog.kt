package com.example.saranggujrati.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.BuildConfig
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.AllBlogListAdapter
import com.example.saranggujrati.adapter.AllBlogViewPagerAdapter
import com.example.saranggujrati.adapter.AllNewsChannelAdapter
import com.example.saranggujrati.adapter.TopCitiesAdapter
import com.example.saranggujrati.databinding.FragmentAllNewsBlogBinding
import com.example.saranggujrati.loadmore.EndlessScrollListener
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.WebViewActivity
import com.example.saranggujrati.ui.activity.YouTubeActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.AllBlogListViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import kotlin.collections.ArrayList

class FragmentAllBlog:BaseFragment<AllBlogListViewModel> (),View.OnClickListener{

    private lateinit var mActivity: MainActivity
    lateinit var allBlogAdapter: AllBlogViewPagerAdapter
    private var bloglList = ArrayList<BlogData>()
    lateinit var binding: FragmentAllNewsBlogBinding




    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): AllBlogListViewModel {
        return obtainViewModel(AllBlogListViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        allBlogAdapter = AllBlogViewPagerAdapter(bloglList)

        mActivity=(activity as MainActivity)
        mActivity.enableViews(true)
        mActivity.supportActionBar?.hide()
        getAllBlogList()

/*
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mActivity.supportActionBar?.show()
                mActivity.onBackPressed()
            }
        })
*/
    }


    override fun onPause() {
        if (allBlogAdapter.adView!=null) {
            allBlogAdapter.adView!!.pause();
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (allBlogAdapter.adView != null) {
            allBlogAdapter.adView!!.resume();
        }


        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()

        requireView().setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i(tag, "onKey Back listener is working!!!");
                    mActivity.supportActionBar?.show()
                    mActivity.onBackPressed()
/*
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
*/
                    return true;
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        if (allBlogAdapter.adView != null) {
            allBlogAdapter.adView!!.destroy();
        }
        super.onDestroy();
    }

    private fun attachListeners() {
       // binding.llLiveNews.setOnClickListener(this)


    }


    private fun getAllBlogList(){
        viewModel.getAllBlogList()
        setupObservers()

    }


    //setup observer
    private fun setupObservers() {
        viewModel.allBlogListResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.isVisible=true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        bloglList.clear()
                        if (it.value.status) {
                            binding.progressbar.visible(false)
                            getAllBlogList(it.value)
                        }

                    }


                }
                is Resource.Failure -> {
                    binding.progressbar.isVisible=false
                    when {
                        it.isNetworkError -> {
                            if (!isOnline(AppClass.appContext)) {
                                Snackbar.make(binding.layout,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()

                        }


                    }

                }

            }
        })
    }
    private fun getAllBlogList(response: AllBlogListResponse) {
        if(response.data.isEmpty() ){
            binding.tvNoData.visibility=View.VISIBLE
            binding.verticalViewPager.visibility=View.GONE
            binding.appBarLayout.visibility=View.VISIBLE

            binding.tvTitle.text=getString(R.string.app_name)
            binding.icBack.setOnClickListener {
                mActivity.onBackPressed()
            }

            binding.toolbar.setOnClickListener {
                mActivity.onBackPressed()
            }
        }else{
            bloglList.addAll(response.data)
            binding.verticalViewPager.adapter = allBlogAdapter
            allBlogAdapter.notifyDataSetChanged()


            allBlogAdapter.adapterListener = object : AllBlogViewPagerAdapter.AdapterListener{
                override fun onClick(view: View, position: Int) {
                    if (view.id == R.id.ic_back) {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }

                    if (view.id == R.id.tvFullStory) {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", bloglList[position].url)
                        i.putExtra("title", bloglList[position].title)
                        startActivity(i)

                    }
                    if (view.id == R.id.ic_share) {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out this link:"+getString(R.string.empty)+ bloglList[position].url + BuildConfig.APPLICATION_ID)
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    }

                }

            }

        }






}



    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}