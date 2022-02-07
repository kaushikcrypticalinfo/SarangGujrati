package com.example.saranggujrati.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.google.android.youtube.player.*
import android.media.MediaPlayer

import android.view.SurfaceHolder


import android.view.SurfaceView
import android.view.View
import java.lang.IllegalStateException
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.saranggujrati.databinding.ActivityMainBinding
import com.example.saranggujrati.databinding.ActivityWebviewBinding
import com.example.saranggujrati.databinding.ActivityYoutubeBinding


import com.google.android.youtube.player.YouTubeInitializationResult
import java.util.regex.Matcher
import java.util.regex.Pattern


class WebViewActivity: AppCompatActivity(){


    private lateinit var binding: ActivityWebviewBinding
    private var urlLink: String? = null
    var title: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()

        setupToolbar(binding.toolbar.toolbar)
        loadWebView()


    }

    private fun getIntentData(){

        if(intent.getStringExtra("url")!=null){
            urlLink=intent.getStringExtra("url")

        }
        if(intent.getStringExtra("title")!=null){
            title=intent.getStringExtra("title")

        }

    }


    private fun setupToolbar(toolbar: Toolbar){
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbar.title=title
        supportActionBar!!.title=title

    }

    private fun loadWebView(){
        binding.webview.webViewClient = WebViewClient()
        Log.e("url",urlLink.toString())

        // this will load the url of the website
        binding.webview.loadUrl(urlLink.toString())

        // this will enable the javascript settings
        binding.webview.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webview.settings.setSupportZoom(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
