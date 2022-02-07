package com.example.saranggujrati.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.inflate
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.adapter.TopCitiesAdapter
import com.example.saranggujrati.databinding.ActivityBaseBinding
import com.example.saranggujrati.databinding.ActivityBaseBinding.inflate
import com.example.saranggujrati.databinding.ActivityLoginBinding
import com.example.saranggujrati.databinding.FragmentHomeBinding
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.viewModel.HomeViewModel
import com.example.saranggujrati.ui.visible
import com.example.saranggujrati.webservice.Resource
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.example.saranggujrati.adapter.AllNewsChannelAdapter
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.ui.activity.YouTubeActivity
import com.performly.ext.addFragmentInActivity
import com.performly.ext.addFragmentToActivity
import com.performly.ext.replaceFragmentInActivity
import android.widget.Toast
import com.example.saranggujrati.adapter.CategoryListAdapter
import com.example.saranggujrati.adapter.FeaturedListAdapter
import com.example.saranggujrati.model.*


class HomeFragment :BaseFragment<HomeViewModel>() ,View.OnClickListener{

    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentHomeBinding

    lateinit var adapter: TopCitiesAdapter
    private var topCitiesList = ArrayList<CityCatageoryChild>()


    lateinit var featureAdapter: FeaturedListAdapter
    private var featureList = ArrayList<FeatureData>()

    lateinit var categoryAdapter: CategoryListAdapter
    private var categoryList = ArrayList<CityCatageoryChild>()



    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var mLayoutManagerHorizontal: RecyclerView.LayoutManager

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun initializeViewModel(): HomeViewModel {
        return obtainViewModel(HomeViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity=(activity as MainActivity)
        mActivity.toolbar.title=getString(R.string.app_name)
        mActivity.enableViews(false)
        setAdapter()
        attachListeners()
        setRVLayoutManager()

        getFeaturedListData()
        getCity()
        getCategory()

        if(SavedPrefrence.is_Guest){
            setHasOptionsMenu(false)

        }else{
            setHasOptionsMenu(true)

        }

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setAdapter() {
        adapter = TopCitiesAdapter(topCitiesList)
        adapter.notifyDataSetChanged()
        binding.rvTopCities.recyclerview.adapter = adapter


        featureAdapter = FeaturedListAdapter(featureList)
        featureAdapter.notifyDataSetChanged()
        binding.rvFeaturedStories.recyclerview.adapter = featureAdapter


        categoryAdapter = CategoryListAdapter(categoryList)
        categoryAdapter.notifyDataSetChanged()
        binding.rvTopCategory.recyclerview.adapter = categoryAdapter


        categoryAdapter.adapterListener = object : CategoryListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val b = Bundle()
                    b.putString("id",categoryList.get(position).id.toString())
                    mActivity.pushFragment(FragmentCityCatBlogDetail(b))
                }

            }

        }

        featureAdapter.adapterListener = object : FeaturedListAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llMain) {
                    val b = Bundle()
                    b.putInt("position",position)
                    /*val fragobj = FragmentFeatureBlog()
                    fragobj.arguments=b*/
                    Log.e("pos1",position.toString())
                    mActivity.pushFragment(FragmentFeatureBlog(b))
                }

            }

        }




        adapter.adapterListener = object : TopCitiesAdapter.AdapterListener {
            override fun onClick(view: View, position: Int) {
                if (view.id == R.id.llCity) {
                    val b = Bundle()
                    b.putString("id",topCitiesList.get(position).id.toString())
                    mActivity.pushFragment(FragmentCityCatBlogDetail(b))
                }

            }

        }


    }





    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(AppClass.appContext)
        mLayoutManagerHorizontal = LinearLayoutManager(AppClass.appContext)

        binding.rvTopCities.recyclerview.layoutManager = mLayoutManager
        binding.rvTopCities.recyclerview.setHasFixedSize(true)
        binding.rvTopCities.recyclerview.layoutManager = GridLayoutManager(AppClass.appContext, 3)


        binding.rvFeaturedStories.recyclerview.layoutManager = mLayoutManagerHorizontal
        binding.rvFeaturedStories.recyclerview.setHasFixedSize(true)
        binding.rvFeaturedStories.recyclerview.layoutManager = LinearLayoutManager(AppClass.appContext,  LinearLayoutManager.HORIZONTAL, false)


        binding.rvTopCategory.recyclerview.layoutManager = mLayoutManager
        binding.rvTopCategory.recyclerview.setHasFixedSize(true)
        binding.rvTopCategory.recyclerview.layoutManager = GridLayoutManager(AppClass.appContext, 3)

        (mLayoutManager as LinearLayoutManager).orientation = RecyclerView.VERTICAL
        (mLayoutManagerHorizontal as LinearLayoutManager).orientation = RecyclerView.HORIZONTAL


    }
    private fun attachListeners() {
        binding.llLiveNews.setOnClickListener(this)
        binding.llallGujNews.setOnClickListener(this)
        binding.llLatestNews.setOnClickListener(this)
        binding.tvLiveTempleDarshan.setOnClickListener(this)


    }

    private fun getCity(){
        viewModel.gettTopCitiesCategories()
        setupObserversTopCities()
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
                    if (it.value.status) {
                        binding.rvTopCities.progressbar.visible(false)
                        getTopCitiesList(it.value)
                    }else{
                        binding.rvTopCities.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }

                }
                is Resource.Failure -> {
                    binding.rvTopCities.progressbar.visible(false)
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
    private fun getTopCitiesList(response: CitCategoryListResponse) {
        if(response.data.isEmpty() ){
            binding.rvTopCities.tvNoData.visibility=View.VISIBLE
            binding.rvTopCities.recyclerview.visibility=View.GONE
        }else{

            for(i in response.data.indices){
                if(response.data[i].index==1) {
                    topCitiesList.addAll(response.data[i].child)
                    adapter.notifyDataSetChanged()
                }

            }

        }

    }

    //Feature List

    private fun getFeaturedListData(){
        viewModel.gettFeatureList()
        setupObserversFeatureList()
    }
    private fun setupObserversFeatureList() {
        viewModel.featureListResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvFeaturedStories.progressbar.visible(true)
                }

                is Resource.Success -> {
                    featureList.clear()
                    if (it.value.status) {
                        binding.rvFeaturedStories.progressbar.visible(false)
                        getFeatureList(it.value)
                    }else{
                        binding.rvFeaturedStories.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                    }

                }
                is Resource.Failure -> {
                    binding.rvFeaturedStories.progressbar.visible(false)
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
    private fun getFeatureList(response: BlogFeatureList) {
        if(response.data.isEmpty() ){
            binding.rvFeaturedStories.tvNoData.visibility=View.VISIBLE
            binding.rvFeaturedStories.recyclerview.visibility=View.GONE
        }else{
            featureList.addAll(response.data)
            featureAdapter.notifyDataSetChanged()

        }

    }

    private fun getCategory(){
        viewModel.gettTopCategories()
        setupObserversTopCategory()
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
                    if (it.value.status) {
                        binding.rvTopCategory.progressbar.visible(false)
                        getTopCategoryList(it.value)
                    }else{
                        binding.rvTopCategory.progressbar.visible(false)
                        Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()

                    }

                }
                is Resource.Failure -> {
                    binding.rvTopCategory.progressbar.visible(false)
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
    private fun getTopCategoryList(response: CitCategoryListResponse) {
        if(response.data.isEmpty() ){
            binding.rvTopCategory.tvNoData.visibility=View.VISIBLE
            binding.rvTopCategory.recyclerview.visibility=View.GONE
        }else{
            for(i in response.data.indices){
                if(response.data[i].index==0) {
                    categoryList.addAll(response.data[i].child)
                    categoryAdapter.notifyDataSetChanged()
                }

            }

        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logo->mActivity.pushFragment(FragmentEditProfile())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.llLiveNews ->mActivity.pushFragment(FragmentAllNewsChannel())
            binding.llallGujNews ->mActivity.pushFragment(FragmentAllNewsPaper())
            binding.llLatestNews ->mActivity.pushFragment(FragmentAllBlog())
            binding.tvLiveTempleDarshan ->mActivity.pushFragment(FragmentLiveTempleDarshanChannelList())
        }
    }

}