package com.example.saranggujrati.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.example.saranggujrati.ui.SavedPrefrence


@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)


        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = when {
                SavedPrefrence.getUserId(AppClass.appContext) != "" -> {
                    Intent(this, MainActivity::class.java)
                }
                SavedPrefrence.getIsGuest(this) == true -> {
                    Intent(this, MainActivity::class.java)
                }
                else -> {
                    Intent(this, LoginActivity::class.java)
                }
            }
            startActivity(intent)
            finish()
        }, 2500) // 3000 is the delayed time in milliseconds.
    }
}

