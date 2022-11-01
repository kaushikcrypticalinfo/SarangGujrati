package com.saranggujrati.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import com.saranggujrati.R
import com.saranggujrati.databinding.ActivityStartMainBinding
import com.saranggujrati.ui.SavedPrefrence
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.startNewActivity
import com.saranggujrati.ui.viewModel.LoginViewModel
import com.saranggujrati.ui.visible
import com.saranggujrati.webservice.Resource
import kotlinx.coroutines.launch


class StartMainActivity : BaseActicvity<LoginViewModel>(), View.OnClickListener {

    private lateinit var binding: ActivityStartMainBinding

    companion object {
        // Request code that will be used to verify if the result comes from correct activity
        // Can be any integer
        private const val RC_SIGN_IN = 9001
    }


    override fun getBaseLayoutView(): View? {
        binding = ActivityStartMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeViewModel(): LoginViewModel {
        return obtainViewModel(LoginViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        Log.e("sta","One Time")
        setupUI()
    }


    private fun setupUI() {
        attachListeners()
    }

    private fun attachListeners() {
        binding.tvForgotPassword.setOnClickListener(this)
        binding.tvGuest.setOnClickListener(this)
        binding.tvTerms.setOnClickListener(this)
        binding.tvPrivacyPolicy.setOnClickListener(this)
    }

    private fun openForgotPasswordDialogue() {
// on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialogue_forgot_password, null)


        val tvResetPassword = view.findViewById<TextView>(R.id.tvResetPassword)
        val etPasswordEmail = view.findViewById<EditText>(R.id.etForgotPasswordEmail)
        val layout = view.findViewById<CoordinatorLayout>(R.id.layout)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressbar)

        tvResetPassword.setOnClickListener {
            if (etPasswordEmail.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_email), Toast.LENGTH_SHORT)
                    .show();

            } else if (!etPasswordEmail.text.toString()
                    .isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(etPasswordEmail.text.toString())
                    .matches()
            ) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.v_valid_email),
                    Toast.LENGTH_SHORT
                ).show();
            } else {
                viewModel.forgotPassword(etPasswordEmail.text.toString())
                setupObserversForgotPassword(dialog, layout, progressbar)
                Toast.makeText(
                    this,
                    resources.getString(R.string.otp_send_success),
                    Toast.LENGTH_SHORT
                ).show();

            }


        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()


    }

    private fun openverifyOtpDialogue() {
// on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialogue_verify_otp, null)


        val etcode = view.findViewById<EditText>(R.id.etcode)
        val tvResetPassword = view.findViewById<TextView>(R.id.tvVerifyOtp)
        val layout = view.findViewById<CoordinatorLayout>(R.id.layout)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressbar)

        tvResetPassword.setOnClickListener {
            if (etcode.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_code), Toast.LENGTH_SHORT)
                    .show();

            } else {
                if (etcode.text.toString() == SavedPrefrence.getOtp(applicationContext)) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.otp_verified),
                        Toast.LENGTH_SHORT
                    ).show();
                    dialog.dismiss()
                    openResetPasswordDialogue()
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.v_valid_code),
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }


        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()


    }

    private fun openResetPasswordDialogue() {
// on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialogue_reset_password, null)


        val tvResetPassword = view.findViewById<TextView>(R.id.tvResetPassword)
        val layout = view.findViewById<CoordinatorLayout>(R.id.layout)
        val password = view.findViewById<EditText>(R.id.etpassword)
        val confirmPassword = view.findViewById<EditText>(R.id.etconfirmPassword)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressbar)

        tvResetPassword.setOnClickListener {
            if (password.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_password), Toast.LENGTH_SHORT)
                    .show();

            } else if (password.text.toString().length < 8) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.v_valid_password),
                    Toast.LENGTH_SHORT
                ).show();

            } else if (confirmPassword.text.toString().isEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.v_confirm_password),
                    Toast.LENGTH_SHORT
                ).show();

            } else {

                if (password.text.toString() == confirmPassword.text.toString()) {
                    viewModel.resetPassword(
                        SavedPrefrence.getUserId(applicationContext).toString(),
                        SavedPrefrence.getOtp(applicationContext).toString(),
                        SavedPrefrence.getEmail(applicationContext).toString(),
                        password.text.toString(),
                        confirmPassword.text.toString()
                    )
                    setupObserversResetPassword(dialog, layout, progressbar)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.v_valid_password_match),
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    private fun clickOnGuest() {
        if (binding.cbAgree.isChecked) {
            SavedPrefrence.is_LOGIN = true
            SavedPrefrence.setGuest(this, true)
            startNewActivity(MainActivity::class.java)
        } else {
            Toast.makeText(this, "Please first check Terms & Condition", Toast.LENGTH_SHORT)
                .show();
        }
    }

    //Forgot Password
    private fun setupObserversForgotPassword(
        dialog: BottomSheetDialog,
        view: View,
        progressBar: ProgressBar,
    ) {

        viewModel.forgotPasswordResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    progressBar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        progressBar.visible(false)
                        lifecycleScope.launch {

                            dialog.dismiss()
                            Snackbar.make(view, it.value.message, Snackbar.LENGTH_LONG).show()
                            SavedPrefrence.setOtp(applicationContext, it.value.data.otp.toString())
                            openverifyOtpDialogue()


                        }

                    } else {

                        binding.progressbar.visible(false)

                    }

                }
                is Resource.Failure -> {
                    progressBar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(
                                    view,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(view, it.value.message, Snackbar.LENGTH_LONG).show()

                        }


                    }

                }


            }
        })
    }
    //Reset Password
    private fun setupObserversResetPassword(
        dialog: BottomSheetDialog,
        view: View,
        progressBar: ProgressBar,
    ) {

        viewModel.resetPasswordResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    progressBar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        progressBar.visible(false)
                        lifecycleScope.launch {
                            Snackbar.make(view, it.value.message, Snackbar.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                    } else {
                        binding.progressbar.visible(false)
                    }
                }
                is Resource.Failure -> {
                    progressBar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(
                                    view,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(view, it.value.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.tvGuest -> clickOnGuest()
            binding.tvForgotPassword -> openForgotPasswordDialogue()
            binding.tvTerms -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.sarangnews.app/terms-conditions/")
                )
                startActivity(browserIntent)
            }

            binding.tvPrivacyPolicy -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.sarangnews.app/privacy-policy/")
                )
                startActivity(browserIntent)
            }
        }
    }
}