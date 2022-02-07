package com.example.saranggujrati.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.AllNewsChannelAdapter
import com.example.saranggujrati.databinding.FragmentAllNewsChannelBinding
import com.example.saranggujrati.loadmore.EndlessScrollListener
import com.example.saranggujrati.model.NewsChannelListRespnse
import com.example.saranggujrati.model.NewsData
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.YouTubeActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.NewsChannelViewModel
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import kotlin.collections.ArrayList

class FragmentLiveTempleDarshanChannelList:BaseFragment<NewsChannelViewModel> (){

    private lateinit var mActivity: MainActivity
    lateinit var newsChannelAdapter: AllNewsChannelAdapter
    private var newsChannelList = ArrayList<NewsData?>()
    lateinit var binding: FragmentAllNewsChannelBinding
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var endlessScrollListener: EndlessScrollListener
    var nextPageUrl: String? = null
    lateinit var newsChannelResponse: NewsChannelListRespnse

    lateinit var swipeContainer: SwipeRefreshLayout



    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): NewsChannelViewModel {
        return obtainViewModel(NewsChannelViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {

        mActivity=(activity as MainActivity)
        mActivity.toolbar.title=getString(R.string.live_mandir_darshan)
        mActivity.enableViews(true)
        setAdapter()
        setRVLayoutManager()
        setRVScrollListener()
        fetchInitialNews(1)
        setHasOptionsMenu(true)

        binding.swipeContainer.setOnRefreshListener {
            endlessScrollListener.resetState()
            fetchInitialNews(1)
        }

        setupObservers()

    }


    override fun onPrepareOptionsMenu(menu: Menu) {

        if( menu.findItem(R.id.logo)!=null){
            menu.findItem(R.id.logo).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)

    }


    private fun setAdapter() {

        newsChannelAdapter = AllNewsChannelAdapter(newsChannelList)
        newsChannelAdapter.notifyDataSetChanged()
        binding.rvAllNewsChannel.recyclerview.adapter = newsChannelAdapter

        newsChannelAdapter.adapterListener = object : AllNewsChannelAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
               // mActivity.pushFragment(YouTubeFragment(newsChannelList[position]?.url))

                    val i = Intent(requireContext(), YouTubeActivity::class.java)
                    i.putExtra("url",newsChannelList[position]?.url)
                    startActivity(i)

                }

            }

        }


    }
    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager((AppClass.appContext))
        binding.rvAllNewsChannel.recyclerview.layoutManager = mLayoutManager
        binding.rvAllNewsChannel.recyclerview.setHasFixedSize(true)
        binding.rvAllNewsChannel.recyclerview.layoutManager = GridLayoutManager(AppClass.appContext, 2)

        (mLayoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL


    }


    private fun setRVScrollListener() {

        endlessScrollListener =
            object : EndlessScrollListener(mLayoutManager as LinearLayoutManager, 1) {

                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (nextPageUrl != null) {
                        getNewsChannel(page)
                    } else {
                        endlessScrollListener.isLastPage()
                    }
                }
            }
        binding.rvAllNewsChannel.recyclerview.addOnScrollListener(endlessScrollListener)

    }

    private fun fetchInitialNews(page: Int) {
        newsChannelList.clear()

        val insertSize: Int = newsChannelList.size
        val insertPos: Int = newsChannelList.size - insertSize
        binding.rvAllNewsChannel.recyclerview.post {
            newsChannelAdapter.notifyItemRangeInserted(insertPos, insertSize)
        }
        endlessScrollListener.resetState()
        getNewsChannel(1)

    }

    private fun getNewsChannel(page: Int){

        viewModel.getAllNewsChannel(page.toString())

    }


    //setup observer
    private fun setupObservers() {
        viewModel.newsChannelResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvAllNewsChannel.progressbar.isVisible=true
                }

                is Resource.Success -> {
                    if (it.value.status) {


                        newsChannelResponse = it.value
                        binding.rvAllNewsChannel.progressbar.isVisible=false
                        binding.swipeContainer.isRefreshing = false
                        nextPageUrl = newsChannelResponse.data.next_page_url

                        addNewsData(it.value)
                    }


                }
                is Resource.Failure -> {
                    binding.rvAllNewsChannel.progressbar.isVisible=false
                    binding.swipeContainer.isRefreshing = false

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
    private fun addNewsData(response: NewsChannelListRespnse) {


        if(response.data.data.isEmpty() ){
            binding.rvAllNewsChannel.tvNoData.visibility=View.VISIBLE
            binding.rvAllNewsChannel.recyclerview.visibility=View.GONE
        }else{
            for(i in response.data.data.indices){
                if(response.data.data[i].category=="1"){
                    newsChannelList.add(response.data.data[i])

                }
            }
            newsChannelAdapter.notifyDataSetChanged()
        }
        }





}