package com.saranggujrati.ui.activity

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.saranggujrati.R
import com.saranggujrati.adapter.ViewPagerAdapter
import com.saranggujrati.ui.SavedPrefrence
import com.saranggujrati.ui.startNewActivity
import com.saranggujrati.ui.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import com.performly.ext.obtainViewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.saranggujrati.AppClass
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat

import android.app.Activity
import androidx.appcompat.widget.SwitchCompat

import android.widget.*

import androidx.appcompat.app.AppCompatDelegate
import android.graphics.Bitmap

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

import com.saranggujrati.ui.fragment.*
import com.saranggujrati.webservice.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.saranggujrati.BuildConfig
import com.saranggujrati.databinding.ActivityMainBinding
import com.saranggujrati.ui.isOnline
import com.saranggujrati.ui.visible
import com.saranggujrati.utils.gujarati_flavors
import com.saranggujrati.utils.kathiyawadi_khamir
import kotlinx.coroutines.launch


class MainActivity : BaseActicvity<MainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mToolBarNavigationListenerIsRegistered: Boolean = false
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var toolbar: Toolbar
    var doubleBackToExitPressedOnce = false
    private lateinit var mySwitch: SwitchCompat
    private lateinit var icAdd: ImageView
    private lateinit var icMinus: ImageView
    private lateinit var tvValue: TextView
    var minteger = 0

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101

    override fun getBaseLayoutView(): View? {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeViewModel(): MainViewModel {
        return obtainViewModel(MainViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        setupUI()
        //setDarkMode(true)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //Log.w("Notification Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            //Log.e("Notification Token", token)
        })
    }

    private fun setupUI() {
//        throw RuntimeException("Test Crash") // Force a crash

        setupObservers()

        //Get Ads Card data list
        viewModel.fullScreenCardList()

        toolbar = binding.appBarToolbar.toolbar
        setSupportActionBar(toolbar)

        mDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarToolbar.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()

        val menu: Menu = binding.navView.menu

        val login = menu.findItem(R.id.nav_Login)
        val logout = menu.findItem(R.id.nav_Logout)

        mySwitch = menu.findItem(R.id.nav_dark_mode).actionView as SwitchCompat

        if (SavedPrefrence.getUserId(AppClass.appContext) == "") {
            login.isVisible = true
            logout.isVisible = false
            enableViews(false)

        } else {
            login.isVisible = false
            logout.isVisible = true
            enableViews(false)
        }
        setDarkMode()
        createViewPager()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun showAlertDialogForLogout() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle(R.string.logout)
        alertDialog.setMessage(R.string.logout_message)
        alertDialog.setPositiveButton(
            R.string.yes
        ) { _, _ ->
            SavedPrefrence.clearPrefrence(AppClass.appContext)
            startNewActivity(LoginActivity::class.java)
            binding.drawerLayout.closeDrawers()
        }
        alertDialog.setNegativeButton(
            R.string.no
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun setDarkMode() {
        val isDarkMode = SavedPrefrence.getIsDarkMode(this)!!
        mySwitch.isChecked = isDarkMode
        mySwitch.setOnCheckedChangeListener { _, isChecked ->
            // if the button is checked, i.e., towards the right or enabled
            // enable dark mode, change the text to disable dark mode
            // else keep the switch text to enable dark mode
            SavedPrefrence.setIsDarkMode(this, isChecked)
            setDarkMode(isChecked)
        }

    }

    private fun setDarkMode(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun displayValue(number: Int) {
        tvValue.text = number.toString()
    }

    fun enableViews(enable: Boolean) {

        // enableViews(false)
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (enable) {
            //You may not want to open the drawer on swipe from the left in this case
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            // Remove hamburger
            mDrawerToggle.isDrawerIndicatorEnabled = false
            // Show back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.toolbarNavigationClickListener =
                    View.OnClickListener { // Doesn't have to be onBackPressed
                        onBackPressed()
                    }
            }
            mToolBarNavigationListenerIsRegistered = true

        } else {
            //You must regain the power of swipe for the drawer.
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            binding.appBarToolbar.toolbar.title = getString(R.string.flavoured_app_name)

            val drawable = resources.getDrawable(R.drawable.ic_menu)
            val bitmap = drawable.toBitmap()
            val newdrawable: Drawable =
                BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 80, 80, true))
            binding.appBarToolbar.toolbar.navigationIcon = newdrawable
            binding.appBarToolbar.toolbar.overflowIcon = newdrawable

            // Remove back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            // Remove the/any drawer toggle listener
            mDrawerToggle.isDrawerIndicatorEnabled = true

            mDrawerToggle.toolbarNavigationClickListener =
                View.OnClickListener { // Doesn't have to be onBackPressed
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }

            mToolBarNavigationListenerIsRegistered = false
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        when (item.itemId) {
            R.id.nav_home -> {
                binding.appBarToolbar.viewPager.currentItem = 0
                binding.drawerLayout.closeDrawers()
                return true
            }

            R.id.nav_font_size -> {
                return true
            }

            R.id.nav_select_language -> {
                return true
            }

            R.id.nav_dark_mode -> {
                binding.drawerLayout.closeDrawers()
                mySwitch.isChecked = !SavedPrefrence.getIsDarkMode(this)!!
                return true
            }

            R.id.nav_about_us -> {
                return true
            }

            R.id.nav_join_us -> {
                return true
            }

            R.id.nav_advertise -> {
                pushFragment(AdvertiseWithUsFragment())
                return true
            }

            R.id.nav_contact_us -> {
                pushFragment(ContactUsFragment())
                return true
            }

            R.id.nav_policy_terms -> {
                pushFragment(PrivacyPolicyFragment())
                return true
            }

            R.id.nav_Login -> {
                startNewActivity(LoginActivity::class.java)
                return true
            }
            R.id.nav_Logout -> {
                showAlertDialogForLogout()
                return true
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }

    private fun createViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(HomeFragment())
        binding.appBarToolbar.viewPager.adapter = adapter
    }

    fun pushFragment(fragment: Fragment) {
//fragment not in back stack, create it.
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.a_header_layout_content, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val count = supportFragmentManager.backStackEntryCount
            when {
                count > 1 -> {
                    supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    enableViews(true)
                    mDrawerToggle.syncState()
                    super.onBackPressed()
                    return
                }
                count == 0 -> {
                    if (doubleBackToExitPressedOnce) {
                        enableViews(false)
                        mDrawerToggle.syncState()
                        super.onBackPressed()
                        return
                    }
                    this.doubleBackToExitPressedOnce = true
                    Toast.makeText(this, getString(R.string.back_again_text), Toast.LENGTH_SHORT)
                        .show()

                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
                else -> {
                    enableViews(false)
                    mDrawerToggle.syncState()
                    super.onBackPressed()
                    return
                }
            }
        }
    }

    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    //setup observer
    private fun setupObservers() {
        viewModel.cardLiveData.observe(this, Observer { it ->
            when (it) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    if (it.value.status) {
                        if (it.value.status) {
                            SavedPrefrence.setAdsCard(it.value.data, this)
                        }
                    }
                }
                is Resource.Failure -> {
                }
            }
        })
    }


}
