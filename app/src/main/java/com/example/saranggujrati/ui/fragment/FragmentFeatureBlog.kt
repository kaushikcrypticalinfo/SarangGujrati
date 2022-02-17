package com.example.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.BuildConfig
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.*
import com.example.saranggujrati.databinding.FragmentFeatureBlogBinding
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.WebViewActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.FeatureBlogListViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import kotlin.collections.ArrayList


class FragmentFeatureBlog(val b:Bundle):BaseFragment<FeatureBlogListViewModel> (),View.OnClickListener{

    private lateinit var mActivity: MainActivity
    lateinit var allBlogAdapter: FeatureBlogViewPagerAdapter
    private var bloglList = ArrayList<FeatureData>()
    lateinit var binding: FragmentFeatureBlogBinding


    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentFeatureBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): FeatureBlogListViewModel {
        return obtainViewModel(FeatureBlogListViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        allBlogAdapter = FeatureBlogViewPagerAdapter(bloglList)
        val bundle=b
        val pos= bundle.getInt("position")
        Log.e("pos2", pos.toString())
        mActivity=(activity as MainActivity)
        mActivity.enableViews(true)
        mActivity.supportActionBar?.hide()
        if (pos != null) {
            getAllBlogList(pos)
        }
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



    private fun getAllBlogList(pos:Int){
        viewModel.getFeatureBlogList()
        setupObservers(pos)

    }


    //setup observer
    private fun setupObservers(pos:Int) {
        viewModel.featureBlogListResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.isVisible=true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        bloglList.clear()
                        if (it.value.status) {
                            binding.progressbar.visible(false)

                            getAllBlogList(it.value,pos)

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
    private fun getAllBlogList(response: BlogFeatureList,pos:Int) {
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
            binding.verticalViewPager.currentItem=pos
            allBlogAdapter.notifyDataSetChanged()





            allBlogAdapter.adapterListener = object : FeatureBlogViewPagerAdapter.AdapterListener{
                override fun onClick(view: View, position: Int) {
                    if (view.id == R.id.ic_back) {
                        mActivity.supportActionBar?.show()
                        mActivity.onBackPressed()
                    }

                    if (view.id == R.id.txtReadMore) {
                        val i = Intent(requireContext(), WebViewActivity::class.java)
                        i.putExtra("url", bloglList[position].url)
                        i.putExtra("title", bloglList[position].title)
                        startActivity(i)

                    }
                    /*if (view.id == R.id.txtReadMore) {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out this link:"+getString(R.string.empty)+ bloglList[position].url + BuildConfig.APPLICATION_ID)
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    }*/

                }

            }

        }






}



    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}