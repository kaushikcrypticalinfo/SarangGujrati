package com.example.saranggujrati.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitcoroutineexample.data.api.RetrofitBuilder
import com.example.saranggujrati.R
import com.example.saranggujrati.ViewModelFactory
import com.example.saranggujrati.databinding.ActivityBaseBinding
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.viewModel.BaseViewModel
import androidx.fragment.app.FragmentTransaction


abstract class BaseFragment <VM : BaseViewModel> : Fragment() {
    lateinit var baseBinding: ActivityBaseBinding
    lateinit var viewModel: VM



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        baseBinding = ActivityBaseBinding.inflate(inflater, container, false)
        val layoutView = getLayoutView(inflater, container)
        baseBinding.aBaseLayoutContent.addView(layoutView)

        viewModel = initializeViewModel()

        setUpChildUI(savedInstanceState)
        return baseBinding.root
    }
    protected abstract fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): View?



/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       */
/* if (activity is ActionBarActivity<*>) {
            setContentView(getBaseLayoutView())
        } else {*//*

            baseBinding = ActivityBaseBinding.inflate(layoutInflater)
            setContentView(baseBinding.root)

            baseBinding.aBaseLayoutContent.addView(getBaseLayoutView())

            //initialize view model
            */
/*viewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService))
                .get(initializeViewModel())*//*


            viewModel = initializeViewModel()
        //}

        setUpChildUI(savedInstanceState)
    }
*/


    protected abstract fun initializeViewModel(): VM

    protected abstract fun setUpChildUI(savedInstanceState: Bundle?)


}