package com.saranggujrati.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.ui.SavedPrefrence

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
        Log.e("SplashScreenActivity","=======>SplashScreenActivity")

        SavedPrefrence.getLoginFrom(this)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        AppCompatDelegate.setDefaultNightMode(if (SavedPrefrence.getIsDarkMode(this)!!) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)


        val intent = when {
            SavedPrefrence.getUserId(AppClass.appContext) != "" || SavedPrefrence.getIsGuest(this) == true -> {
                Intent(this, MainActivity::class.java)
            }
            /*SavedPrefrence.getIsGuest(this) == true -> {
                Intent(this, MainActivity::class.java)
            }*/
            else -> {
                Intent(this, StartMainActivity::class.java)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 4000) // 3000 is the delayed time in milliseconds.
    }
}

