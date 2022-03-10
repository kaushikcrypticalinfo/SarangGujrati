package com.example.saranggujrati.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.FragmentContactUsBinding
import com.example.saranggujrati.model.*
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.isValidEmail
import com.example.saranggujrati.ui.sendMail
import com.example.saranggujrati.ui.viewModel.HomeViewModel
import com.performly.ext.obtainViewModel


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
}

