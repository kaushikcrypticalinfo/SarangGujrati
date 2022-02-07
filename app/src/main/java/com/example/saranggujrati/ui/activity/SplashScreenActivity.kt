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
import com.example.saranggujrati.ui.startNewActivity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target


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
            if(SavedPrefrence.getUserId(AppClass.appContext)!= "" ){
                val intenr = Intent(this, MainActivity::class.java)
                startActivity(intenr)
                finish()
            }else{

                val intenr = Intent(this, LoginActivity::class.java)
                startActivity(intenr)
                finish()
            }
        }, 2500) // 3000 is the delayed time in milliseconds.
    }
}

