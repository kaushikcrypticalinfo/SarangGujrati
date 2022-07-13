package com.saranggujrati.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import com.saranggujrati.R
import com.saranggujrati.databinding.FragmentPrivacyPolicyBinding
import com.saranggujrati.extensions.formatHtmlText
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.viewModel.HomeViewModel
import com.performly.ext.obtainViewModel

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

        binding.txtPrivacyPolicy.formatHtmlText(
            getString(R.string.p_strong_privacy_policy)
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (menu.findItem(R.id.logo) != null) {
            menu.findItem(R.id.logo).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onClick(p0: View?) {
    }
}
