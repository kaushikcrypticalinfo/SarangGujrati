package com.saranggujrati.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.performly.ext.obtainViewModel
import com.saranggujrati.R
import com.saranggujrati.databinding.FragmentContactUsBinding
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.isValidEmail
import com.saranggujrati.ui.sendMail
import com.saranggujrati.ui.viewModel.HomeViewModel


class AdvertiseWithUsFragment : BaseFragment<HomeViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentContactUsBinding

    var name = "Advertise with us"
    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): HomeViewModel {
        return obtainViewModel(HomeViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.str_advertise_with_us)
        mActivity.enableViews(true)
        setHasOptionsMenu(true);
        attachListeners()
        loadWebView()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (menu.findItem(R.id.logo) != null) {
            menu.findItem(R.id.logo).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun attachListeners() {
        binding.btnSend.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val strName = binding.edtName.text
        val strEmail = binding.edtEmail.text
        val strPhNo = binding.edtPhoneNo.text
        if (strName.toString().isEmpty()) {
            binding.edtName.error = "Please enter your name"
            binding.edtName.requestFocus()
        } else if (strEmail.toString().isEmpty()) {
            binding.edtEmail.error = "Please enter your mail address"
            binding.edtEmail.requestFocus()
        } else if (!isValidEmail(strEmail.toString())) {
            binding.edtEmail.error = "Please enter valid email address"
            binding.edtEmail.requestFocus()
        } else if (strPhNo.toString().isEmpty()) {
            binding.edtPhoneNo.error = "Please enter your phone number"
            binding.edtPhoneNo.requestFocus()
        } else {
            val body = "Name:: " + strName.toString() +
                    "\nEmail:: " + strEmail.toString() +
                    "\nPhone No:: " + strPhNo.toString() +
                    "\nMessage:: " + binding.edtMessage.text.toString()
            sendMail(requireContext(), name, body)
        }
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
        binding.webview.loadUrl("https://www.sarangnews.app/advertise-with-us/")
    }
}

