package com.example.saranggujrati.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import com.example.saranggujrati.R
import com.example.saranggujrati.databinding.ActivityLoginBinding
import com.example.saranggujrati.ui.startNewActivity
import com.example.saranggujrati.ui.viewModel.LoginViewModel
import com.example.saranggujrati.webservice.Resource
import androidx.lifecycle.Observer
import com.example.saranggujrati.ui.SavedPrefrence
import com.example.saranggujrati.ui.isOnline
import com.example.saranggujrati.ui.visible
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.performly.ext.obtainViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import timber.log.Timber
import java.net.URL
import java.util.*


class LoginActivity : BaseActicvity<LoginViewModel>(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        // Request code that will be used to verify if the result comes from correct activity
        // Can be any integer
        private const val RC_SIGN_IN = 9001
    }


    override fun getBaseLayoutView(): View? {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeViewModel(): LoginViewModel {
        return obtainViewModel(LoginViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        setupUI()
    }


    private fun setupUI() {

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        // [END config_signin]
        // getting the value of gso inside the GoogleSigninClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        // firebaseAuth= FirebaseAuth.getInstance()
        // [END initialize_auth]


        //facebook
        FacebookSdk.sdkInitialize(this.applicationContext)
        callbackManager = CallbackManager.Factory.create()

        attachListeners()
    }

    private fun attachListeners() {
        binding.llFacebook.setOnClickListener(this)
        binding.loginButton.setOnClickListener(this)
        binding.llGoogle.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
        binding.tvSignIn.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.tvGuest.setOnClickListener(this)

    }


    private fun facebookLogin() {
        val permissionNeeds: List<String> = Arrays.asList("user_photos", "email",
            "user_birthday", "public_profile", "AccessToken")

        binding.loginButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult?) {
                System.out.println("onSuccess");
                val accessToken = loginResult?.getAccessToken()?.getToken()
                if (accessToken != null) {
                    Log.i("accessToken", accessToken)
                };
                val request = GraphRequest.newMeRequest(
                    loginResult?.accessToken
                ) { fbObject, response ->
                    Log.v("Login Success", response.toString())


                    //For safety measure enclose the request with try and catch
                    try {

                        Log.i("LoginActivity", response.toString());

                        val firstName = fbObject.getString("first_name")
                        val lastName = fbObject.getString("last_name")
                        val gender = fbObject.getString("gender")
                        val email = fbObject.getString("email")
                        val id = fbObject.getString("id")
                        val birthday = fbObject.getString("birthday")
                        val profile_pic =
                            URL("http://graph.facebook.com/" + id + "/picture?type=large");

                        Log.i("FirstName", "onSuccess: firstName $firstName")
                        Log.i("LastName", "onSuccess: lastName $lastName")
                        Log.i("Gender", "onSuccess: gender $gender")
                        Log.i("Email", "onSuccess: email $email")
                        Log.i("id", "onSuccess: id $id")
                        Log.i("birthday", "onSuccess: birthday $birthday")
                        Log.i("profile_pic", "onSuccess: profile_pic $profile_pic")


                        viewModel.socialLogin(email,
                            firstName + " " + lastName,
                            profile_pic.toURI().toString(),
                            "",
                            "",
                            "FB")
                        setupObserversSocialLogin()

                    } //If no data has been retrieve throw some error
                    catch (e: JSONException) {
                        e.printStackTrace();
                    }

                }
                val bundle = Bundle()
                bundle.putString("fields",
                    "id, email, first_name, last_name, gender,age_range,birthday")
                //Set the bundle's data as Graph's object data
                request.setParameters(bundle)

                //Execute this Graph request asynchronously
                request.executeAsync()
            }

            override fun onCancel() {
                Log.i("onCancel", "onCancel: called")
            }

            override fun onError(error: FacebookException?) {
                Log.i("OnError", "onError: called")
            }
        })
    }


    // [START Sign in]
    private fun googleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // [END Sign in]
    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)

        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            Timber.d("firebaseAuthWithGoogle:%s", account?.id)
            if (account != null) {
                Log.e("success", account.toString())
                updateUI(account)
            }
        } catch (e: ApiException) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            Timber.e(e)
        }
    }


    // UpdateUI() function
    private fun updateUI(account: GoogleSignInAccount) {
        val email = account.email.toString()
        val name = account.displayName.toString()
        val image = account.photoUrl.toString()
        val googleId = account.id.toString()


        Log.e("email", email)
        Log.e("name", name)
        Log.e("googleId", googleId)

        viewModel.socialLogin(email, name, image, googleId, "", "google")
        setupObserversSocialLogin()


    }

    private fun openSignUpDialogue() {
// on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialogue_signup, null)


        val tvSignup1 = view.findViewById<TextView>(R.id.tvSignUpDialogue)
        val layout = view.findViewById<CoordinatorLayout>(R.id.dialogLayout)
        val tvSignIn = view.findViewById<TextView>(R.id.tvSignInDialogue)
        val etEmail = view.findViewById<EditText>(R.id.etSignupEmail)
        val etPassword = view.findViewById<EditText>(R.id.etSignupPassword)
        val etName = view.findViewById<EditText>(R.id.etSignupName)
        val etPhone = view.findViewById<EditText>(R.id.etSignupPhoneNumber)
        val progressbar = view.findViewById<ProgressBar>(R.id.progressbar)

        tvSignup1.setOnClickListener {
            if (etName.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_name), Toast.LENGTH_SHORT)
                    .show();

            } else if (etName.text.toString().length < 4) {
                Toast.makeText(this, resources.getString(R.string.v_valid_name), Toast.LENGTH_SHORT)
                    .show();

            } else if (etPhone.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_phone), Toast.LENGTH_SHORT)
                    .show();

            } else if (etPhone.text.toString().length < 10 || etPhone.text.toString().length > 10) {
                Toast.makeText(this,
                    resources.getString(R.string.v_valid_phone),
                    Toast.LENGTH_SHORT).show();

            } else if (etEmail.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_email), Toast.LENGTH_SHORT)
                    .show();
            } else if (!etEmail.text.toString()
                    .isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()
            ) {
                Toast.makeText(this,
                    resources.getString(R.string.v_valid_email),
                    Toast.LENGTH_SHORT).show();
            } else if (etPassword.text.toString().isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.v_password), Toast.LENGTH_SHORT)
                    .show();

            } else if (etPassword.text.toString().length < 8) {
                Toast.makeText(this,
                    resources.getString(R.string.v_valid_password),
                    Toast.LENGTH_SHORT).show();

            } else {
                viewModel.signUp(etEmail.text.toString(),
                    etPassword.text.toString(),
                    etPhone.text.toString(),
                    etName.text.toString())
                setupObserversSignUp(dialog, layout, progressbar)


            }


        }

        tvSignIn.setOnClickListener {
            dialog.dismiss()
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
                Toast.makeText(this,
                    resources.getString(R.string.v_valid_email),
                    Toast.LENGTH_SHORT).show();
            } else {
                viewModel.forgotPassword(etPasswordEmail.text.toString())
                setupObserversForgotPassword(dialog, layout, progressbar)
                Toast.makeText(this,
                    resources.getString(R.string.otp_send_success),
                    Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(this,
                        resources.getString(R.string.otp_verified),
                        Toast.LENGTH_SHORT).show();
                    dialog.dismiss()
                    openResetPasswordDialogue()
                } else {
                    Toast.makeText(this,
                        resources.getString(R.string.v_valid_code),
                        Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this,
                    resources.getString(R.string.v_valid_password),
                    Toast.LENGTH_SHORT).show();

            } else if (confirmPassword.text.toString().isEmpty()) {
                Toast.makeText(this,
                    resources.getString(R.string.v_confirm_password),
                    Toast.LENGTH_SHORT).show();

            } else {

                if (password.text.toString() == confirmPassword.text.toString()) {
                    viewModel.resetPassword(SavedPrefrence.getUserId(applicationContext).toString(),
                        SavedPrefrence.getOtp(applicationContext).toString(),
                        SavedPrefrence.getEmail(applicationContext).toString(),
                        password.text.toString(),
                        confirmPassword.text.toString())
                    setupObserversResetPassword(dialog, layout, progressbar)
                } else {
                    Toast.makeText(this,
                        resources.getString(R.string.v_valid_password_match),
                        Toast.LENGTH_SHORT).show();
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

    private fun validate() {
        if (binding.etEmail.text.toString().isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.v_email), Toast.LENGTH_SHORT).show();
        } else if (!binding.etEmail.text.toString().isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(
                binding.etEmail.text.toString()).matches()
        ) {
            Toast.makeText(this, resources.getString(R.string.v_valid_email), Toast.LENGTH_SHORT)
                .show();
        } else if (binding.etPassword.text.toString().isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.v_password), Toast.LENGTH_SHORT)
                .show();

        } else {
            viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            setupObserversLogin()


        }

    }


    private fun clickOnGuest() {
        SavedPrefrence.is_LOGIN = true
        SavedPrefrence.setGuest(this,true)
        startNewActivity(MainActivity::class.java)
    }



    //setup Observer

    private fun setupObserversLogin() {

        viewModel.loginResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {

                    binding.progressbar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        binding.progressbar.visible(false)
                        lifecycleScope.launch {
                            SavedPrefrence.is_LOGIN = true
                            SavedPrefrence.is_Guest = false

                            SavedPrefrence.setUser(it.value.data, applicationContext)
                            SavedPrefrence.setUserName(applicationContext, it.value.data.name)
                            SavedPrefrence.setUserId(applicationContext, it.value.data.id.toString())
                            SavedPrefrence.setEmail(applicationContext, it.value.data.email)
                            SavedPrefrence.setApiToken(applicationContext, it.value.data.api_token)


                            Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                                .show();

                            startNewActivity(MainActivity::class.java)

                        }

                    }  else  {
                        Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressbar.visible(false)

                    }

                }
                is Resource.Failure -> {
                    binding.progressbar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(binding.tvSignUp,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                                .show()

                        }


                    }


                }
            }
        })

    }

    //signup
    private fun setupObserversSignUp(
        dialog: BottomSheetDialog,
        view: View,
        progressBar: ProgressBar,
    ) {

        viewModel.signUpResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    Log.e("Progress", "true")
                    progressBar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        progressBar.visible(false)
                        lifecycleScope.launch {

                            SavedPrefrence.is_LOGIN = true
                            SavedPrefrence.is_Guest = false

                            SavedPrefrence.setUser(it.value.data, applicationContext)
                            SavedPrefrence.setUserName(applicationContext, it.value.data.name)
                            SavedPrefrence.setUserId(applicationContext, it.value.data.id.toString())
                            SavedPrefrence.setEmail(applicationContext, it.value.data.email)
                            SavedPrefrence.setApiToken(applicationContext, it.value.data.api_token)
                            Snackbar.make(view, it.value.message, Snackbar.LENGTH_LONG).show()
                            dialog.dismiss()
                            startNewActivity(MainActivity::class.java)


                        }

                    } else  {
                        Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressbar.visible(false)

                    }

                }
                is Resource.Failure -> {
                    progressBar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(view,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG).show()
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

                    } else  {
                        Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressbar.visible(false)

                    }

                }
                is Resource.Failure -> {
                    progressBar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(view,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG).show()
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

                    } else  {
                        Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressbar.visible(false)

                    }

                }
                is Resource.Failure -> {
                    progressBar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(view,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG).show()
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
//Social Login


    private fun setupObserversSocialLogin() {

        viewModel.socialLoginResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        binding.progressbar.visible(false)
                        lifecycleScope.launch {


                            SavedPrefrence.is_LOGIN=true
                            SavedPrefrence.is_Guest = false


                            SavedPrefrence.setUser(it.value.data, applicationContext)
                            SavedPrefrence.setUserName(applicationContext, it.value.data.name)
                            SavedPrefrence.setUserId(applicationContext, it.value.data.id.toString())
                            SavedPrefrence.setEmail(applicationContext, it.value.data.email)
                            SavedPrefrence.setApiToken(applicationContext, it.value.data.api_token)


                            Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_SHORT)
                                .show();

                            startNewActivity(MainActivity::class.java)
                        }

                    } else  {
                        Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressbar.visible(false)

                    }

                }
                is Resource.Failure -> {
                    binding.progressbar.visible(false)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(applicationContext)) {
                                Snackbar.make(binding.tvSignUp,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.tvSignUp, it.value.message, Snackbar.LENGTH_LONG)
                                .show()

                        }


                    }


                }
            }
        })

    }



    override fun onClick(p0: View?) {
        when (p0) {
            binding.loginButton -> facebookLogin()
            binding.llFacebook -> binding.loginButton.performClick()
            binding.llGoogle -> googleSignIn()
            binding.tvGuest -> clickOnGuest()
            binding.tvSignIn -> validate()
            binding.tvSignUp -> openSignUpDialogue()
            binding.tvForgotPassword -> openForgotPasswordDialogue()

        }

    }
}