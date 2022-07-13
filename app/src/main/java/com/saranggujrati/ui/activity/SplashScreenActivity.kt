package com.saranggujrati.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.saranggujrati.AppClass
import com.saranggujrati.R
import com.saranggujrati.ui.SavedPrefrence

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        SavedPrefrence.getLoginFrom(this)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when {
                SavedPrefrence.getUserId(AppClass.appContext) != "" -> {
                    Intent(this, MainActivity::class.java)
                }
                SavedPrefrence.getIsGuest(this) == true -> {
                    Intent(this, MainActivity::class.java)
                }
                else -> {
                    Intent(this, StartMainActivity::class.java)
                }
            }
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}

