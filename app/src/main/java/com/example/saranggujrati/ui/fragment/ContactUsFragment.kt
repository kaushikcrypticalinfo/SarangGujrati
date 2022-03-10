package com.example.saranggujrati.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.FragmentContactUsBinding
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.ui.activity.MainActivity
import com.example.saranggujrati.ui.isValidEmail
import com.example.saranggujrati.ui.sendMail
import com.example.saranggujrati.ui.viewModel.HomeViewModel
import com.performly.ext.obtainViewModel


class ContactUsFragment : BaseFragment<HomeViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentContactUsBinding
    var name = "Contact us"

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): HomeViewModel {
        return obtainViewModel(HomeViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.app_name)
        mActivity.enableViews(false)
        binding.txtTitle.text="Contact us"
        attachListeners()

        if (SavedPrefrence.is_Guest) {
            setHasOptionsMenu(false)
        } else {
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
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
            sendMail(
                requireContext(), name, "Name:: " + strName.toString() +
                        "\nEmail:: " + strEmail.toString() +
                        "\nPhone No:: " + strPhNo.toString() +
                        "\nMessage:: " + binding.edtMessage.text.toString()
            )
        }
    }
}