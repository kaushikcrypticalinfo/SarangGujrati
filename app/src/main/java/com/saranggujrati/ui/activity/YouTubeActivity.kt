package com.saranggujrati.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener
import com.saranggujrati.R
import com.saranggujrati.databinding.ActivityYoutubeBinding
import com.saranggujrati.utils.KEY
import com.saranggujrati.utils.VALUE
import timber.log.Timber
import java.util.regex.Pattern


class YouTubeActivity : AppCompatActivity() {

    private val TAG = "YoutubeActivity"
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var binding: ActivityYoutubeBinding
    var urlLink: String? = null
    var videoName: String? = null
    var videoId: String? = null

    var state: PlayerConstants.PlayerState = PlayerConstants.PlayerState.UNKNOWN
        private set
    var currentSecond: Float = 0f
        private set
    var videoDuration: Float = 0f
        private set


    private lateinit var mActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mActivity = MainActivity()

        loadBannerAd()
        lifecycle.addObserver(binding.youtubePlayer)

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
//        binding.adView.setAdSize(getAdSize())
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
        }
    }

    private fun getAdSize(): AdSize {
        //Determine the screen width to use for the ad width.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density

        //you can also pass your selected width here in dp
        val adWidth = (widthPixels / density).toInt()

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    override fun onPause() {
        if (binding.adView != null) {
            binding.adView.pause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (binding.adView != null) {
            binding.adView.resume()
        }
    }

    override fun onDestroy() {
        if (binding.adView != null) {
            binding.adView.destroy()
        }
        super.onDestroy()
    }

    override fun onStop() {
        Log.e("onStop", "onStop")
        val resultIntent = Intent()
        resultIntent.putExtra(
            KEY,
            VALUE
        ) // Replace "key" and "value" with the actual data you want to pass back
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onStop()
    }

    private fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        if (matcher.find()) {
            videoId = matcher.group()

        }
        //Log.e("videoId", videoId.toString())

        return videoId
    }

    private fun playVideo() {
        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                youTubePlayer.cueVideo(videoId!!, 0f)
                youTubePlayer.play()
                binding.youtubePlayer.addFullScreenListener(object :
                    YouTubePlayerFullScreenListener {
                    override fun onYouTubePlayerEnterFullScreen() {
                        Timber.e("enterFullScreen")
                        enterFullScreen()
                    }

                    override fun onYouTubePlayerExitFullScreen() {
                        Timber.e("exitFullScreen")
                        exitFullScreen()
                    }
                })
            }

            override fun onError(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                Timber.tag("Error").e(error.toString())
            }

            override fun onCurrentSecond(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                second: Float
            ) {
                currentSecond = second
            }

            override fun onVideoDuration(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                duration: Float
            ) {
                videoDuration = duration
            }

        })

    }

    private fun enterFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun exitFullScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /*private fun playVideo() {
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
                    *//*Toast.makeText(AppClass.appContext, p1.toString(), Toast.LENGTH_SHORT)
                        .show()*//*

                    Timber.tag("p1").e(p1.toString())
                }
            })
    }*/

}
