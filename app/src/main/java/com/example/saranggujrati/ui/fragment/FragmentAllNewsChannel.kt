package com.example.saranggujrati.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.PagingDemoAdapter
import com.example.saranggujrati.adapter.PlayersLoadingStateAdapter
import com.example.saranggujrati.databinding.FragmentAllNewsChannelBinding
import com.example.saranggujrati.model.NewsChannelListRespnse
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.activity.YouTubeActivity
import com.example.saranggujrati.ui.viewModel.NewsChannelViewModel
import com.example.saranggujrati.ui.visible
import com.performly.ext.obtainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentAllNewsChannel : BaseFragment<NewsChannelViewModel>() {

    private lateinit var mActivity: MainActivity

    lateinit var pagingDemoAdapter: PagingDemoAdapter

    lateinit var binding: FragmentAllNewsChannelBinding

    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentAllNewsChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): NewsChannelViewModel {
        return obtainViewModel(NewsChannelViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.all_channels)
        mActivity.enableViews(true)

        binding.rvAllNewsChannel.progressbar.visible(true)

        setRVLayoutManager()

        setAdapter()

        binding.swipeContainer.setOnRefreshListener {
            pagingDemoAdapter.refresh()
        }

        setupObservers()

        getNewsChannel()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (menu.findItem(R.id.logo) != null) {
            menu.findItem(R.id.logo).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun retry() {
        pagingDemoAdapter.retry()
    }

    private fun setAdapter() {

        pagingDemoAdapter = PagingDemoAdapter()

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

        pagingDemoAdapter.adapterListener = object : PagingDemoAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val i = Intent(requireContext(), YouTubeActivity::class.java)
                    i.putExtra("url", pagingDemoAdapter.snapshot()[position]?.url)
                    startActivity(i)
                }
            }
        }

        binding.rvAllNewsChannel.recyclerview.adapter = pagingDemoAdapter
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager((AppClass.appContext))
        binding.rvAllNewsChannel.recyclerview.layoutManager = mLayoutManager
        binding.rvAllNewsChannel.recyclerview.setHasFixedSize(true)
        binding.rvAllNewsChannel.recyclerview.layoutManager =
            GridLayoutManager(AppClass.appContext, 2)

        (mLayoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL
    }

    private fun getNewsChannel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagingData().collectLatest {
                pagingDemoAdapter.submitData(lifecycle, it)
            }
        }
    }


    //setup observer
    private fun setupObservers() {
    }

    private fun addNewsData(response: NewsChannelListRespnse) {
        if (response.data.data.isEmpty()) {
            binding.rvAllNewsChannel.tvNoData.visibility = View.VISIBLE
            binding.rvAllNewsChannel.recyclerview.visibility = View.GONE
        }
    }


}