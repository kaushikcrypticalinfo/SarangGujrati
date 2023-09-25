package com.saranggujrati.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.adapter.*
import com.saranggujrati.databinding.FragmentHomeBinding
import com.saranggujrati.model.*
import com.saranggujrati.model.onDemand.OnDemandData
import com.saranggujrati.ui.SavedPrefrence
import com.saranggujrati.ui.activity.ActivityCityCatBlogDetail
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.YouTubeActivity
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.viewModel.HomeViewModel
import com.saranggujrati.ui.visible
import com.saranggujrati.utils.KEY
import com.saranggujrati.utils.VALUE
import com.saranggujrati.utils.topMenuName
import com.saranggujrati.webservice.Resource
import java.util.*

const val REQ_CODE_CITY_CAT_BLOG = 1001
private const val EXTRA_ID = "EXTRA_ID"
private const val EXTRA_PARENT_ID = "EXTRA_PARENT_ID"
private const val EXTRA_NAME = "EXTRA_NAME"

class HomeFragment : BaseFragment<HomeViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentHomeBinding

    lateinit var topCitiesAdapter: TopCitiesAdapter
    private var topCitiesList = ArrayList<CityCatageoryChild>()

    lateinit var featureAdapter: FeaturedListAdapter
    lateinit var onDemandAdapter: OnDemandListAdapter
    private var featureList = ArrayList<FeatureData>()
    private var onDemandList = ArrayList<OnDemandData>()

    lateinit var categoryAdapter: CategoryListAdapter
    private var categoryList = ArrayList<CityCatageoryChild>()

    lateinit var topMenuAdapter: TopMenuAdapter
    private var topMenuItemList = ArrayList<ApiRecordData>()

    var fragmentBackFromButton: Boolean = false

    var lblTemple: String = ""

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): HomeViewModel {
        return obtainViewModel(HomeViewModel::class.java)
    }

    private var launchActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                if (result.data?.getStringExtra(KEY) == VALUE) {
                    Log.e("RESULT", "TRUE")
                    fragmentBackFromButton = true
                } else {
                    Log.e("RESULT", "FALSE")
                    fragmentBackFromButton = false
                }
            }
        }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.app_name)
        mActivity.enableViews(false)

        setRVLayoutManager()

        setAdapter()

        attachListeners()

        callApi()

        if (SavedPrefrence.is_Guest) {
            setHasOptionsMenu(false)
        } else {
            setHasOptionsMenu(true)
        }

        viewModel.apiRecord.observe(this) { it ->
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }

                is Resource.Success -> {
                    binding.progressbar.visible(false)
                    if (it.value.status) {
                        if (it.value.data.isEmpty()) {
                            binding.rtlBtn.visible(false)
                            binding.tvLiveTempleDarshan.visible(false)
                        } else {
                            topMenuItemList.apply {
                                clear()
                                addAll(it.value.data.filter { it.id != 5 && it.found == true })
                            }
                            it.value.data.let { it ->
                                it.find { it.id == 5 && it.found == true && it.name != null }?.let {
                                    binding.tvLiveTempleDarshan.visible(true)
                                    binding.txtTempleDarshan.text = it.name
                                    lblTemple = it.name!!
                                } ?: kotlin.run {
                                    binding.tvLiveTempleDarshan.visible(false)
                                    binding.txtTempleDarshan.text = ""
                                    lblTemple = ""
                                }
                            }
                        }
                        topMenuAdapter.notifyDataSetChanged()
                    }
                }

                is Resource.Failure -> {
                    binding.progressbar.visible(false)
                    when {
                        it.isNetworkError -> {
                            if (!isOnline(requireContext())) {
                                Snackbar.make(
                                    binding.layout,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }

                        else -> {
                            Snackbar.make(
                                binding.layout,
                                getString(R.string.something_went_wrong),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        if (fragmentBackFromButton) {
            callApi()
        }

    }

    private fun setAdapter() {
        topCitiesAdapter = TopCitiesAdapter(topCitiesList)
        topCitiesAdapter.adapterListener = object : TopCitiesAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.tvCity) {
                    val data = topCitiesList[position]
                    /*ActivityCityCatBlogDetail.startActivity(
                        activity!!, data.parent_id, data.id.toString(), data.name
                    )*/
                    val intent = Intent(context, ActivityCityCatBlogDetail::class.java)
                    intent.putExtra(EXTRA_PARENT_ID, data.parent_id)
                    intent.putExtra(EXTRA_ID, data.id.toString())
                    intent.putExtra(EXTRA_NAME, data.name)
                    launchActivity.launch(intent)
                }
            }
        }
        binding.rvTopCities.recyclerview.adapter = topCitiesAdapter

        featureAdapter = FeaturedListAdapter(featureList)
        binding.rvFeaturedStories.recyclerview.adapter = featureAdapter

        onDemandAdapter = OnDemandListAdapter(onDemandList)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvOnDemand.recyclerview)
        onDemandAdapter.adapterListener = object : OnDemandListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                val i = Intent(requireContext(), YouTubeActivity::class.java)
                i.putExtra("url", onDemandList[position].url)
                i.putExtra("videoName", onDemandList[position].title)
                startActivity(i)
            }
        }
        val speedScroll = 3000L
        val handler = Handler(Looper.getMainLooper())
        val runnable: Runnable = object : Runnable {
            var count = 0
            override fun run() {
                if (count < onDemandList.size) count += 1 else count = 0
                binding.rvOnDemand.recyclerview.smoothScrollToPosition(count)
                handler.postDelayed(this, speedScroll)
            }
        }
        handler.postDelayed(runnable, speedScroll)

        binding.rvOnDemand.recyclerview.adapter = onDemandAdapter

        categoryAdapter = CategoryListAdapter(categoryList)
        categoryAdapter.adapterListener = object : CategoryListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val data = categoryList[position]
                    /*ActivityCityCatBlogDetail.startActivity(
                        activity!!, data.parent_id, data.id.toString(), data.name
                    )*/
                    val intent = Intent(activity, ActivityCityCatBlogDetail::class.java)
                    intent.putExtra(EXTRA_PARENT_ID, data.parent_id)
                    intent.putExtra(EXTRA_ID, data.id.toString())
                    intent.putExtra(EXTRA_NAME, data.name)
                    launchActivity.launch(intent)
                }
            }
        }
        binding.rvTopCategory.recyclerview.adapter = categoryAdapter

        featureAdapter.adapterListener = object : FeaturedListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val b = Bundle()
                    b.putInt("position", position)
                    mActivity.pushFragment(FragmentFeatureBlog(b), "FragmentFeatureBlog")
                }
            }
        }

        topMenuAdapter = TopMenuAdapter(topMenuItemList)
        topMenuAdapter.adapterListener = object : TopMenuAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                val correspondingItem =
                    topMenuItemList.find { it.id == position }
                when (position) {
                    1 -> {
                        bundle.putString(topMenuName, correspondingItem?.name)
                        mActivity.pushFragment(FragmentAllBlog(), "FragmentAllBlog", bundle)
                    }

                    2 -> {
                        bundle.putString(topMenuName, correspondingItem?.name)
                        mActivity.pushFragment(
                            FragmentAllNewsPaper(),
                            "FragmentAllNewsPaper",
                            bundle
                        )
                    }

                    3 -> {
                        bundle.putString(topMenuName, correspondingItem?.name)
                        mActivity.pushFragment(
                            FragmentLatestNewsOnTheGo(),
                            "FragmentLatestNewsOnTheGo",
                            bundle
                        )
                    }

                    4 -> {
                        bundle.putString(topMenuName, correspondingItem?.name)
                        mActivity.pushFragment(
                            FragmentAllNewsChannel(),
                            "FragmentAllNewsChannel",
                            bundle
                        )
                    }
                }
            }
        }
        binding.rvTopMenu.adapter = topMenuAdapter
    }

    private fun setRVLayoutManager() {
        binding.rvTopCities.recyclerview.setHasFixedSize(true)
        binding.rvTopCities.recyclerview.layoutManager = GridLayoutManager(AppClass.appContext, 3)

        binding.rvFeaturedStories.recyclerview.setHasFixedSize(true)
        binding.rvFeaturedStories.recyclerview.layoutManager =
            LinearLayoutManager(AppClass.appContext, LinearLayoutManager.HORIZONTAL, false)

//        Live temple
        binding.rvOnDemand.recyclerview.setHasFixedSize(true)
        binding.rvOnDemand.recyclerview.layoutManager =
            LinearLayoutManager(AppClass.appContext, LinearLayoutManager.HORIZONTAL, false)

        binding.rvTopCategory.recyclerview.setHasFixedSize(true)
        binding.rvTopCategory.recyclerview.layoutManager = GridLayoutManager(AppClass.appContext, 3)

        binding.rvTopMenu.setHasFixedSize(true)
        binding.rvTopMenu.layoutManager = GridLayoutManager(AppClass.appContext, 2)

    }

    private fun attachListeners() {
        binding.tvLiveTempleDarshan.setOnClickListener(this)
        binding.swipeRefresh.setOnRefreshListener {
            callApi()
        }
    }

    private fun callApi() {
        viewModel.getApiRecordFound()

        getFeaturedListData()

        getCity()

        getCategory()
    }

    private fun getCity() {
        setupObserversTopCities()
        viewModel.gettTopCitiesCategories()
    }
    //Top Cities

    private fun setupObserversTopCities() {
        viewModel.topCitiesResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvTopCities.progressbar.visible(true)
                }

                is Resource.Success -> {
                    topCitiesList.clear()
                    binding.swipeRefresh.isRefreshing = false
                    if (it.value.status) {
                        binding.rvTopCities.progressbar.visible(false)
                        getTopCitiesList(it.value)
                    } else {
                        binding.rvTopCities.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }

                }

                is Resource.Failure -> {
                    binding.rvTopCities.progressbar.visible(false)
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

    private fun getTopCitiesList(response: CitCategoryListResponse) {
        if (response.data.isEmpty()) {
            binding.rvTopCities.tvNoData.visibility = View.VISIBLE
            binding.rvTopCities.recyclerview.visibility = View.GONE
        } else {
            response?.let { res ->
                res.data?.let { data ->
                    data.find { it.id == 18 }?.let { cateData ->
                        cateData.child?.let {
                            topCitiesList.addAll(it)
                            topCitiesAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    //Feature List

    private fun getFeaturedListData() {
        setupObserversFeatureList()

        viewModel.getFeatureList()

        viewModel.getOnDemandList()
    }

    private fun setupObserversFeatureList() {
        viewModel.featureListResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvFeaturedStories.progressbar.visible(true)
                }

                is Resource.Success -> {
                    featureList.clear()
                    binding.swipeRefresh.isRefreshing = false
                    if (it.value.status) {
                        binding.rvFeaturedStories.progressbar.visible(false)
                    } else {
                        binding.rvFeaturedStories.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }
                    getFeatureList(it.value)
                }

                is Resource.Failure -> {
                    binding.rvFeaturedStories.progressbar.visible(false)
                    setFeatureStoryLblVisisbility()
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

        viewModel.onDemandList.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvOnDemand.progressbar.visible(true)
                }

                is Resource.Success -> {
                    onDemandList.clear()
                    binding.swipeRefresh.isRefreshing = false
                    if (it.value.status) {
                        binding.rvOnDemand.progressbar.visible(false)
                        it.value.data.data?.let { it1 -> onDemandList.addAll(it1) }
                        onDemandLblVisibility()
                        onDemandAdapter.notifyDataSetChanged()
                    } else {
                        binding.rvOnDemand.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Failure -> {
                    onDemandLblVisibility()
                    binding.rvOnDemand.progressbar.visible(false)
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

    private fun onDemandLblVisibility() {
        binding.txtLblOnDemand.visible(onDemandList.isNotEmpty())
    }

    private fun getFeatureList(response: BlogFeatureList) {
        if (response.data.isEmpty()) {
            //binding.rvFeaturedStories.tvNoData.visibility = View.VISIBLE
            binding.rvFeaturedStories.recyclerview.visibility = View.GONE
        } else {
            featureList.addAll(response.data)
            featureAdapter.notifyDataSetChanged()
        }
        setFeatureStoryLblVisisbility()
    }

    private fun setFeatureStoryLblVisisbility() {
        binding.tvFeaturedStories.visible(featureList.isNotEmpty())
    }

    private fun getCategory() {
        setupObserversTopCategory()
        viewModel.gettTopCategories()
    }

    //Top Category
    private fun setupObserversTopCategory() {
        viewModel.topCategoryResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvTopCategory.progressbar.visible(true)
                }

                is Resource.Success -> {
                    categoryList.clear()
                    binding.swipeRefresh.isRefreshing = false
                    if (it.value.status) {
                        binding.rvTopCategory.progressbar.visible(false)
                        getTopCategoryList(it.value)
                    } else {
                        binding.rvTopCategory.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Failure -> {
                    binding.rvTopCategory.progressbar.visible(false)
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

    private fun getTopCategoryList(response: CitCategoryListResponse) {
        if (response.data.isEmpty()) {
            binding.rvTopCategory.tvNoData.visibility = View.VISIBLE
            binding.rvTopCategory.recyclerview.visibility = View.GONE
        } else {
            response.let { res ->
                res.data.let { data ->
                    data.find { it.id == 21 }?.let { cateData ->
                        cateData.child?.let {
                            categoryList.addAll(it)
                            categoryAdapter.notifyDataSetChanged()
                        }
                    }
                }
            } ?: kotlin.run { }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logo -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        val bundle = Bundle()
        when (p0) {
            binding.tvLiveTempleDarshan -> {
                bundle.putString(topMenuName, lblTemple)
                mActivity.pushFragment(
                    FrLiveTempleDarshanChannel(), "FrLiveTempleDarshanChannel"
                )
            }
        }
    }

}