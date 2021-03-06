package com.example.saranggujrati.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

import com.example.saranggujrati.AppClass
import com.example.saranggujrati.R
import com.google.android.youtube.player.*
import android.media.MediaPlayer

import android.view.SurfaceHolder


import android.view.SurfaceView
import android.view.View
import java.lang.IllegalStateException
import android.widget.FrameLayout
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.saranggujrati.databinding.ActivityMainBinding
import com.example.saranggujrati.databinding.ActivityYoutubeBinding
import com.example.saranggujrati.ui.mainActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


import com.google.android.youtube.player.YouTubeInitializationResult
import java.util.regex.Matcher
import java.util.regex.Pattern


class YouTubeActivity: YouTubeBaseActivity() {

    private val TAG = "YoutubeActivity"
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var binding: ActivityYoutubeBinding
    var urlLink: String? = null
    var videoId: String? = null

     private lateinit var mActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mActivity= MainActivity()

        loadBannerAd()


        binding.toolbar.tvTitle.text=getString(R.string.back_to_channel)
        binding.toolbar.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.toolbar.tvTitle.setOnClickListener {
            onBackPressed()
        }

        if(intent.getStringExtra("url")!=null){
            urlLink=intent.getStringExtra("url")

        }
        getYouTubeId(urlLink!!)
        if(videoId!=null){
            playVideo()

        }else{
            Toast.makeText(AppClass.appContext , "Error" , Toast.LENGTH_SHORT).show()

        }

    }


    private fun loadBannerAd(){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener  = object : AdListener(){
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                /*val toastMessage: String = "ad fail to load"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdLoaded() {
                super.onAdLoaded()
                /*val toastMessage: String = "ad loaded"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdOpened() {
                super.onAdOpened()
                /* val toastMessage: String = "ad is open"
                 Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdClicked() {
                super.onAdClicked()
              /*  val toastMessage: String = "ad is clicked"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }

            override fun onAdClosed() {
                super.onAdClosed()
                /*val toastMessage: String = "ad is closed"
                Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdImpression() {
                super.onAdImpression()
                /*  val toastMessage: String = "ad impression"
                  Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                /*  val toastMessage: String = "ad left application"
                  Toast.makeText(AppClass.appContext, toastMessage.toString(), Toast.LENGTH_LONG).show()*/
            }
        }



    }

    override fun onPause() {
        if (binding.adView!=null) {
            binding.adView!!.pause();
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (binding.adView != null) {
            binding.adView!!.resume();
        }
    }

    override fun onDestroy() {
        if (binding.adView != null) {
            binding.adView!!.destroy();
        }
        super.onDestroy();
    }
    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
         if (matcher.find()) {
           videoId= matcher.group()

        }
        Log.e("videoId", videoId.toString())

        return videoId;
    }



    private fun playVideo(){
        binding.youtubePlayer.initialize(getString(R.string.api_key), object : YouTubePlayer.OnInitializedListener{
            // Implement two methods by clicking on red error bulb
            // inside onInitializationSuccess method
            // add the video link or the
            // playlist link that you want to play
            // In here we also handle the play and pause functionality
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean,
            ) {
                Toast.makeText(AppClass.appContext , "Video player Success" , Toast.LENGTH_SHORT).show()
                //  player?.loadVideo(urlLink.toString())
                player?.loadVideo(videoId)
                player?.play()
            }

            // Inside onInitializationFailure
            // implement the failure functionality
            // Here we will show toast
            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?,
            ) {
                Toast.makeText(AppClass.appContext , "Video player Failed" , Toast.LENGTH_SHORT).show()
            }
        })

    }

}
