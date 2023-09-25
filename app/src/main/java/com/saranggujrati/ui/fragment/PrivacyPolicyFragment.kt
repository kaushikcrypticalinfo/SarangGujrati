package com.saranggujrati.ui.fragment

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.performly.ext.obtainViewModel
import com.saranggujrati.BuildConfig
import com.saranggujrati.R
import com.saranggujrati.databinding.FragmentPrivacyPolicyBinding
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.viewModel.HomeViewModel
import com.saranggujrati.utils.kathiyawadi_khamir

class PrivacyPolicyFragment : BaseFragment<HomeViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentPrivacyPolicyBinding
    var name = "Privacy Policy"

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): HomeViewModel {
        return obtainViewModel(HomeViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.str_privacy_policy)
        mActivity.enableViews(true)
        setHasOptionsMenu(true);
        loadWebView()

        /*binding.txtPrivacyPolicy.formatHtmlText(
            getString(R.string.p_strong_privacy_policy)
        )*/
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (menu.findItem(R.id.logo) != null) {
            menu.findItem(R.id.logo).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onClick(p0: View?) {
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
        when (BuildConfig.FLAVOR) {
            kathiyawadi_khamir -> binding.webview.loadUrl("https://kathiyawadikhamir.com/app-privacy-policy")
            else -> binding.webview.loadUrl("https://www.sarangnews.app/app-policy-terms/")
        }
    }
}
