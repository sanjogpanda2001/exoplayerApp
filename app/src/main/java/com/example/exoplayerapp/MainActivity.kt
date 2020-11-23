package com.example.exoplayerapp

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.util.*
import kotlin.properties.Delegates

private lateinit var simpleExoPlayer: SimpleExoPlayer

class MainActivity : AppCompatActivity() {

   // val prev=findViewById<ImageView>(R.id.previous)

    var currentTrackIndex=0
    val url1=Uri.parse("https://i.imgur.com/7bMqysJ.mp4")
    val uri2=Uri.parse("https://i.imgur.com/7bMqysJ.mp4")
    val video_url= arrayOf(url1,uri2)
    val len=video_url.size
    //variables

    //lateinit   var simpleExoPlayer=SimpleExoPlayer
//val quality: ImageView =findViewById<ImageView>(R.id.quality)
    //private lateinit var simpleExoPlayer: SimpleExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        excecute()


    }
    fun excecute(){
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val playerView: PlayerView = findViewById<PlayerView>(R.id.player_view)
        val btFullScreen = playerView?.findViewById<ImageView>(R.id.bt_fullscreen)

        var flag = false
        //fullscreen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//
//        val retro= Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("https://i.imgur.com/7bMqysJ.mp4/").build()
//
//        val jsonholderapi=retro.create(jsonapiholder::class.java)
//
//        val myImg=jsonholderapi.getUsers()
        //  myImg.enqueue(object : Callback<List<User>> {
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                val user = response.body()
//                val vdoView = findViewById<PlayerView>(R.id.player_view)
//                val us = user?.get(0)


        //            if (us != null) {
        //Picasso.get().load(us.url).into(imageView)

        //video url
        val videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4")
        //  val url=Uri.parse(us.url)


        //initiative load control
        val loadControl = DefaultLoadControl()
        //initialize bandwidth meter

        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

        //initialize track selector
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()

        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        //initialize simple exo player
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                //this,trackSelector,loadControl,
                this@MainActivity, trackSelector, loadControl
        )

        //datasource factory
        val factory = DefaultHttpDataSourceFactory("exoplayer_video")
        //initialise extractor factory
        val extractorFactory = DefaultExtractorsFactory()
        //initialise media store
        val mediasource = ExtractorMediaSource(video_url[currentTrackIndex], factory, extractorFactory, null, null)


        val loop = findViewById<ImageView>(R.id.loop)
        //setPlayer
        if (playerView != null) {
            playerView.player = simpleExoPlayer
        }
        //screen on
        playerView?.keepScreenOn = true
        //prepare media
        simpleExoPlayer.prepare(mediasource)

        //play when rdy
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.visibility = View.GONE
                    //  quality.isEnabled=true

                }
            }


            //override fun g
            // override fun setPlaybackSpeed
        })
        loop.setOnClickListener {
            val loopingSource =  LoopingMediaSource(mediasource);
            simpleExoPlayer.prepare(mediasource);

        }

        val next=findViewById<ImageView>(R.id.next)
        next.setOnClickListener {
            simpleExoPlayer.release()
            currentTrackIndex++
            if (currentTrackIndex > len - 1) {
                Toast.makeText(this, "no more videos", Toast.LENGTH_SHORT)
            } else {
                excecute()
            }
        }
//        val quality=findViewById<ImageView>(R.id.quality)
//        quality.setOnClickListener {
//            var popup=PopupMenu(this,quality)
//            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {  })
//
//        }



        if (btFullScreen != null) {
            btFullScreen.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (flag) {
                        btFullScreen?.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen))
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        flag = false
                    } else {
                        if (btFullScreen != null) {
                            btFullScreen?.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen_exit))
                        }
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        flag = true
                    }
                }
            })
        }
    }


//            override fun onFailure(call: Call<List<User>>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "failure", Toast.LENGTH_SHORT).show()
//            }


    override fun onPause() {
        super.onPause()
        simpleExoPlayer.playWhenReady = false
        simpleExoPlayer.getPlaybackState()
    }

    override fun onRestart() {
        super.onRestart()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.getPlaybackState()
    }

  

    override fun onBackPressed() {
        super.onBackPressed()

    }
    fun kill(){
        if (simpleExoPlayer!=null){
            simpleExoPlayer.release()
        }
    }

}

