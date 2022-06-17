package com.example.saranggujrati.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView


import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.saranggujrati.databinding.ActivityWebviewBinding


class WebViewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWebviewBinding
    private var urlLink: String? = null
    var title: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readIntentData()

        setupToolbar(binding.toolbar.toolbar)

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


    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbar.title = title
        supportActionBar!!.title = title

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
        binding.webview.settings.domStorageEnabled  = true

        // if you want to enable zoom feature
        binding.webview.settings.setSupportZoom(true)

        binding.webview.loadUrl(urlLink.toString())

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
