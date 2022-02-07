package com.example.saranggujrati.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitcoroutineexample.data.api.RetrofitBuilder
import com.example.saranggujrati.R
import com.example.saranggujrati.ViewModelFactory
import com.example.saranggujrati.databinding.ActivityBaseBinding
import com.example.saranggujrati.ui.viewModel.BaseViewModel

abstract class BaseActicvity <VM : BaseViewModel> : AppCompatActivity() {
    lateinit var baseBinding: ActivityBaseBinding
    lateinit var viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* if (activity is ActionBarActivity<*>) {
             setContentView(getBaseLayoutView())
         } else {*/
            baseBinding = ActivityBaseBinding.inflate(layoutInflater)
            setContentView(baseBinding.root)

            baseBinding.aBaseLayoutContent.addView(getBaseLayoutView())

            //initialize view model
            /*viewModel= ViewModelProvider(this, ViewModelFactory(RetrofitBuilder.apiService))
                .get(initializeViewModel())*/

            viewModel = initializeViewModel()
        //}

        setUpChildUI(savedInstanceState)
    }

    protected abstract fun getBaseLayoutView(): View?

    protected abstract fun initializeViewModel(): VM

    protected abstract fun setUpChildUI(savedInstanceState: Bundle?)


}