package com.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.adapter.LiveTemplateAdapter
import com.saranggujrati.adapter.PlayersLoadingStateAdapter
import com.saranggujrati.model.NewsChannelListRespnse
import com.saranggujrati.model.NewsData
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.YouTubeActivity
import com.saranggujrati.ui.viewModel.NewsChannelViewModel
import com.saranggujrati.ui.visible
import com.performly.ext.obtainViewModel
import com.saranggujrati.databinding.FragmentAllNewsChannelBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class FrLiveTempleDarshanChannel : BaseFragment<NewsChannelViewModel>() {

    private lateinit var mActivity: MainActivity
    lateinit var binding: FragmentAllNewsChannelBinding
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    lateinit var pagingDemoAdapter: LiveTemplateAdapter

    var moreClick: Boolean = false

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentAllNewsChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): NewsChannelViewModel {
        return obtainViewModel(NewsChannelViewModel::class.java)
    }

    private fun pushFragment() {
        val fragmentManager = parentFragmentManager
        val FrLiveTempleDarshanChannel = fragmentManager.findFragmentByTag("FrLiveTempleDarshanChannel")
        if (FrLiveTempleDarshanChannel != null) {
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
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.str_live_darshan_temple)
        mActivity.enableViews(true)

        binding.rvAllNewsChannel.progressbar.visible(true)

        setRVLayoutManager()

        setAdapter()

        fetchInitialNews()

        setHasOptionsMenu(true)

        binding.swipeContainer.setOnRefreshListener {
            pagingDemoAdapter.refresh()
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
        pagingDemoAdapter = LiveTemplateAdapter()

        pagingDemoAdapter.withLoadStateFooter(PlayersLoadingStateAdapter { retry() })

        pagingDemoAdapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {
                if (pagingDemoAdapter.snapshot().isEmpty()) {
                    binding.rvAllNewsChannel.progressbar.visible(true)
                }
                binding.rvAllNewsChannel.tvNoData.visible(false)

            } else {
                binding.rvAllNewsChannel.progressbar.visible(false)
                binding.swipeContainer.isRefreshing = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    if (pagingDemoAdapter.snapshot().isEmpty()) {
                        binding.rvAllNewsChannel.tvNoData.visible(true)
                        binding.rvAllNewsChannel.tvNoData.text = it.error.localizedMessage
                    }
                }
            }
        }

        pagingDemoAdapter.adapterListener = object : LiveTemplateAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val i = Intent(requireContext(), YouTubeActivity::class.java)
                    i.putExtra("url", pagingDemoAdapter.snapshot()[position]?.url)
                    i.putExtra("videoName", pagingDemoAdapter.snapshot()[position]?.company_name)
                    moreClick = true
                    startActivity(i)
                }
            }
        }

        binding.rvAllNewsChannel.recyclerview.adapter = pagingDemoAdapter
    }

    private fun retry() {
        pagingDemoAdapter.retry()
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager((AppClass.appContext))
        binding.rvAllNewsChannel.recyclerview.layoutManager = mLayoutManager
        binding.rvAllNewsChannel.recyclerview.setHasFixedSize(true)
        binding.rvAllNewsChannel.recyclerview.layoutManager =
            GridLayoutManager(AppClass.appContext, 2)

        (mLayoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL
    }

    private fun fetchInitialNews() {
        getNewsChannel()
    }

    private fun getNewsChannel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagingData().collectLatest { it ->
                pagingDemoAdapter.submitData(lifecycle, it.filter { it.category == "1" })
            }
        }
    }

    //setup observer
    private fun setupObservers() {
    }

}