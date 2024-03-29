package com.example.saranggujrati.loadmore

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessScrollListener : RecyclerView.OnScrollListener {
    var LOG_TAG: String = EndlessScrollListener::class.java.getSimpleName()


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 6

    // The current offset index of data you have loaded
    private var currentPage = 1

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 1

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private var startingPageIndex = 1
    lateinit var mLayoutManager: RecyclerView.LayoutManager



    constructor (layoutManager: LinearLayoutManager?, startingPageIndex: Int) {
        mLayoutManager = layoutManager!!
        this.startingPageIndex = startingPageIndex
        currentPage = startingPageIndex
    }

    constructor(layoutManager: GridLayoutManager, startingPageIndex: Int) {
        mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
        this.startingPageIndex = startingPageIndex
        currentPage = startingPageIndex
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
    }


    open fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if(!recyclerView.canScrollVertically(1)) {
            // LOAD MORE

        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition =
                (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition =
                (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount + 1) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold shoud reflect how many total columns there are too
        if (loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, recyclerView)
            loading = true
        }
        }

    }




    // Call this method whenever performing new searches
    open fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 1
        loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)

    open fun isLastPage(): Boolean {
        return false
    }

}