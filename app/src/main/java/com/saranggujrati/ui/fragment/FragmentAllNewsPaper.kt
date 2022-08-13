package com.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.adapter.AllNewsPaperAdapter
import com.saranggujrati.loadmore.EndlessScrollListener
import com.saranggujrati.model.NewsPaperData
import com.saranggujrati.model.NewsPaperListResponse
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.WebViewActivity
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.viewModel.NewsPaperViewModel
import com.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import com.saranggujrati.databinding.FragmentAllNewsChannelBinding
import kotlin.collections.ArrayList

class FragmentAllNewsPaper : BaseFragment<NewsPaperViewModel>() {

    private lateinit var mActivity: MainActivity
    lateinit var newsPaperAdapter: AllNewsPaperAdapter
    private var newPaperlList = ArrayList<NewsPaperData?>()
    lateinit var binding: FragmentAllNewsChannelBinding
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var endlessScrollListener: EndlessScrollListener
    var nextPageUrl: String? = null
    lateinit var newsPaperResponse: NewsPaperListResponse
    lateinit var swipeContainer: SwipeRefreshLayout


    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): NewsPaperViewModel {
        return obtainViewModel(NewsPaperViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {

        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.all_news_paper)
        mActivity.enableViews(true)
        setAdapter()
        setRVLayoutManager()
        setRVScrollListener()
        fetchInitialNews()
        setHasOptionsMenu(true);

        binding.swipeContainer.setOnRefreshListener {
            endlessScrollListener.resetState()
            fetchInitialNews()
        }

        setupObservers()

    }


    override fun onPrepareOptionsMenu(menu: Menu) {

        if (menu.findItem(R.id.logo) != null) {
            menu.findItem(R.id.logo).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)

    }


    private fun setAdapter() {

        newsPaperAdapter = AllNewsPaperAdapter(newPaperlList)
        newsPaperAdapter.adapterListener = object : AllNewsPaperAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val i = Intent(requireContext(), WebViewActivity::class.java)
                    i.putExtra("url", newPaperlList[position]?.url)
                    i.putExtra("title", newPaperlList[position]?.title)
                    startActivity(i)
                }
            }
        }
        binding.rvAllNewsChannel.recyclerview.adapter = newsPaperAdapter
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager((AppClass.appContext))
        binding.rvAllNewsChannel.recyclerview.layoutManager = mLayoutManager
        binding.rvAllNewsChannel.recyclerview.setHasFixedSize(true)
        binding.rvAllNewsChannel.recyclerview.layoutManager =
            GridLayoutManager(AppClass.appContext, 2)

        (mLayoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL
    }


    private fun setRVScrollListener() {
        endlessScrollListener =
            object : EndlessScrollListener(mLayoutManager as LinearLayoutManager, 1) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (nextPageUrl != null) {
                        getNewsPaperChannel(page)
                    } else {
                        endlessScrollListener.isLastPage()
                    }
                }
            }
        binding.rvAllNewsChannel.recyclerview.addOnScrollListener(endlessScrollListener)
    }

    private fun fetchInitialNews() {
        newPaperlList.clear()

        val insertSize: Int = newPaperlList.size
        val insertPos: Int = newPaperlList.size - insertSize
        binding.rvAllNewsChannel.recyclerview.post {
            newsPaperAdapter.notifyItemRangeInserted(insertPos, insertSize)
        }
//        endlessScrollListener.resetState()
        getNewsPaperChannel(1)
    }

    private fun getNewsPaperChannel(page: Int) {
        viewModel.getAllNewsPaper(page.toString())
    }

    //setup observer
    private fun setupObservers() {
        viewModel.newsPaperResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvAllNewsChannel.progressbar.isVisible = true
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        newsPaperResponse = it.value
                        binding.rvAllNewsChannel.progressbar.isVisible = false
                        binding.swipeContainer.isRefreshing = false

                        nextPageUrl = newsPaperResponse.data.next_page_url

                        addNewsPaperData(it.value)
                    }
                }

                is Resource.Failure -> {
                    binding.rvAllNewsChannel.progressbar.isVisible = false
                    binding.swipeContainer.isRefreshing = false

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

    private fun addNewsPaperData(response: NewsPaperListResponse) {
        if (response.data.data.isEmpty()) {
            binding.rvAllNewsChannel.tvNoData.visibility = View.VISIBLE
            binding.rvAllNewsChannel.recyclerview.visibility = View.GONE
        } else {
            newPaperlList.addAll(response.data.data)
            newsPaperAdapter.notifyDataSetChanged()

        }
    }


}