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
import com.example.saranggujrati.adapter.*
import com.example.saranggujrati.databinding.FragmentAllNewsBlogBinding
import com.example.saranggujrati.loadmore.EndlessScrollListener
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.WebViewActivity
import com.example.saranggujrati.ui.activity.YouTubeActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.AllBlogListViewModel
import com.example.saranggujrati.ui.viewModel.FeatureBlogListViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import kotlin.collections.ArrayList
import android.widget.Toast

import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.saranggujrati.ui.viewModel.CityCatBlogDetailViewModel


class FragmentCityCatBlogDetail(val b:Bundle):BaseFragment<CityCatBlogDetailViewModel> (),View.OnClickListener{

    private lateinit var mActivity: MainActivity
    lateinit var cityCatBlogDetailAdapter: CityCategoryBlogDetailViewPagerAdapter
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
        cityCatBlogDetailAdapter = CityCategoryBlogDetailViewPagerAdapter(blogList)
        val bundle=b
        val id= bundle.getString("id")
        Log.e("id", id.toString())
        mActivity=(activity as MainActivity)
        mActivity.enableViews(true)
        mActivity.supportActionBar?.hide()
        if (id != null) {
            getAllBlogList(id)
        }
    }


    override fun onPause() {
        if (cityCatBlogDetailAdapter.adView!=null) {
            cityCatBlogDetailAdapter.adView!!.pause();
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (cityCatBlogDetailAdapter.adView != null) {
            cityCatBlogDetailAdapter.adView!!.resume();
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
        if (cityCatBlogDetailAdapter.adView != null) {
            cityCatBlogDetailAdapter.adView!!.destroy();
        }
        super.onDestroy();
    }



    private fun getAllBlogList(id:String){
        viewModel.getCityCatBlogDetail(id)
        setupObservers(id)

    }


    //setup observer
    private fun setupObservers(id:String) {
        viewModel.cityCatBlogDetailResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.isVisible=true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        blogList.clear()
                        if (it.value.status) {
                            binding.progressbar.visible(false)

                            getAllBlogList(it.value,id)

                        }

                    }else{
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()

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
    private fun getAllBlogList(response: CityCategoryBlogDetailResponse,id:String) {
        if(response.data.isEmpty() ){
            binding.tvNoData.visibility=View.VISIBLE
            binding.appBarLayout.visibility=View.VISIBLE
            binding.verticalViewPager.visibility=View.GONE

            binding.tvTitle.text=getString(R.string.app_name)
            binding.icBack.setOnClickListener {
                mActivity.onBackPressed()
            }

            binding.toolbar.setOnClickListener {
                mActivity.onBackPressed()
            }
        }else{
            for(i in response.data.indices) {
                blogList.addAll(response.data[i].blog)

            }
            binding.verticalViewPager.adapter = cityCatBlogDetailAdapter
            cityCatBlogDetailAdapter.notifyDataSetChanged()




            cityCatBlogDetailAdapter.adapterListener = object : CityCategoryBlogDetailViewPagerAdapter.AdapterListener{
                override fun onClick(view: View, position: Int) {
                    if (view.id == R.id.ic_back) {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }

                    if (view.id == R.id.tvFullStory) {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", blogList[position].url)
                        i.putExtra("title", blogList[position].title)
                        startActivity(i)

                    }
                    if (view.id == R.id.ic_share) {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out this link:"+getString(R.string.empty)+ blogList[position].url + BuildConfig.APPLICATION_ID)
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