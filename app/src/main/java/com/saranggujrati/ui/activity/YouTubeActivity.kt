package com.saranggujrati.ui.activity

import android.os.Bundle

import com.saranggujrati.R
import com.google.android.youtube.player.*


import androidx.annotation.NonNull
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError


import com.google.android.youtube.player.YouTubeInitializationResult
import com.saranggujrati.databinding.ActivityYoutubeBinding
import java.util.regex.Pattern


class YouTubeActivity : YouTubeBaseActivity() {

    private val TAG = "YoutubeActivity"
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var binding: ActivityYoutubeBinding
    var urlLink: String? = null
    var videoName: String? = null
    var videoId: String? = null

    private lateinit var mActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mActivity = MainActivity()

        loadBannerAd()


        binding.tvTitle.text = getString(R.string.back_to_channel)
        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvTitle.setOnClickListener {
            onBackPressed()
        }

        if (intent.getStringExtra("url") != null) {
            urlLink = intent.getStringExtra("url")

        }
        if (intent.getStringExtra("videoName") != null) {
            videoName = intent.getStringExtra("videoName")
        }

        binding.tvVideoTitle.text = videoName
        getYouTubeId(urlLink!!)
        if (videoId != null) {
            playVideo()
        } else {
        }

    }


    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(@NonNull p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
            }
        }
    }

    override fun onPause() {
        if (binding.adView != null) {
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
            videoId = matcher.group()

        }
        //Log.e("videoId", videoId.toString())

        return videoId;
    }


    private fun playVideo() {
        binding.youtubePlayer.initialize(
            getString(R.string.api_key),
            object : YouTubePlayer.OnInitializedListener {
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
//                    Toast.makeText(AppClass.appContext, "Video player Success", Toast.LENGTH_SHORT)
//                        .show()
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
//                    Toast.makeText(AppClass.appContext, "Video player Failed", Toast.LENGTH_SHORT)
//                        .show()
                }
            })

    }

}
