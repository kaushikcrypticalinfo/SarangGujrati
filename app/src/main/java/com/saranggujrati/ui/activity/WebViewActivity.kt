package com.saranggujrati.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView


import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.saranggujrati.databinding.ActivityWebviewBinding


class WebViewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWebviewBinding
    private var urlLink: String? = null
    var title: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readIntentData()

        setupToolbar()

        loadWebView()
    }

    private fun readIntentData() {

        if (intent.getStringExtra("url") != null) {
            urlLink = intent.getStringExtra("url")

        }
        if (intent.getStringExtra("title") != null) {
            title = intent.getStringExtra("title")

        }

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = title!!.trim()
        supportActionBar!!.title = title!!.trim()
    }

    private fun loadWebView() {
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressbar.visibility = View.VISIBLE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                binding.progressbar.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressbar.visibility = View.GONE
            }

        }

        // this will enable the javascript settings
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.domStorageEnabled = true
        binding.webview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webview.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        binding.webview.settings.useWideViewPort = true


        // if you want to enable zoom feature
        binding.webview.settings.setSupportZoom(true)
        binding.webview.loadUrl(urlLink.toString())

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStop() {
        Log.e("onStop", "onStop")
        val resultIntent = Intent()
        resultIntent.putExtra("close", "activity") // Replace "key" and "value" with the actual data you want to pass back
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onStop()
    }
}
