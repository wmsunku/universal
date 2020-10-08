package com.wms.media.music.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import com.wms.base.sdk.createView
import com.wms.base.sdk.registerReceiver
import com.wms.base.sdk.unregisterReceiver
import com.wms.media.R
import com.wms.media.sdk.OnCountTimeListener
import com.wms.media.sdk.getTime
import com.wms.media.service.AudioConfig

class AudioPlayer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
        OnAudioPlayerListener, OnCountTimeListener, OnAudioLifeListener {

    private val IS_PLAYING = "is_playing"
    private var mCoverView: AlbumCoverView? = null
    private var isPlaying = false
    private var holder: AudioViewHolder? = null
    private var hasPlaying = false
    private var receiver = AudioUIReceiver()

    init {
        val contentView = createView(context, R.layout.layout_audio_player)
        addView(contentView)
        initView(contentView)
    }

    private fun initView(view: View) {
        isPlaying = isPlaying()
        registerReceiver(receiver, getIntentFilter())
        holder = AudioViewHolder(view)
        initListener()
        startCount(this)
        if(isPlaying) {
            getHolder().tvTotalTime.text = getTime(getTotalProgress())
            getHolder().tvCurrentTime.text = getTime(getCurrProgress())
            getHolder().sbProgress.max = getTotalProgress()
            getHolder().ivPlay.isSelected = true
            getHolder().sbProgress.isEnabled = true
        }
    }

    private fun initListener() {
        val vh = getHolder()
        vh.ivMode.setOnClickListener {}
        vh.ivNext.setOnClickListener {
            nextSong()
        }
        vh.ivPrev.setOnClickListener {
            lastSong()
        }
        vh.ivPlay.setOnClickListener {
            if (isPlaying) {
                pause()
            } else {
                start()
            }
        }
        hasPlaying = isPlaying
        vh.sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                vh.tvCurrentTime.text = getTime(progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                hasPlaying = isPlaying
                pause()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                val progress = seek.progress
                vh.sbProgress.progress = progress
                handlerSeek(progress)
                if (hasPlaying) {
                    start()
                }
            }
        })
    }

    private fun getHolder(): AudioViewHolder {
        return holder!!
    }

    fun bindAlbum(cover: AlbumCoverView, isPlaying: Boolean) {
        mCoverView = cover
        if(isPlaying) {
            mCoverView!!.initNeedle(true)
            onPlayCover()
        }else {
            mCoverView!!.initNeedle(false)

        }
    }

    private fun onPlayCover() {
        if (mCoverView != null) {
            mCoverView!!.start()
        }
    }

    private fun onPauseCover() {
        if (mCoverView != null) {
            mCoverView!!.pause()
        }
    }



    /***
     *
     * 音频播放器实现
     *
     ***/

    override fun loadUrl(path: String) {
        startCount(this)
        getHolder().ivPlay.isSelected = false
        getHolder().sbProgress.isEnabled = false
        handlerLoadUrl(path)
    }

    override fun start() {
        if (!isPrepared()) {
            Toast.makeText(context, "还未加载好", Toast.LENGTH_SHORT).show()
            return
        }
        val vh = getHolder()
        vh.ivPlay.isSelected = true
        vh.sbProgress.isEnabled = true
        isPlaying = true
        onPlayCover()
        handlerStart()
    }

    override fun pause() {
        val vh = getHolder()
        vh.ivPlay.isSelected = false
        isPlaying = false
        onPauseCover()
        handlerPause()
    }

    override fun stop() {
        isPlaying = false
        getHolder().sbProgress.progress = 0
        onPauseCover()
        handlerStop()
    }

    override fun nextSong() {

    }

    override fun lastSong() {

    }


    /***
     *
     * 资源加载监听
     *
     ***/

    private fun loadReady() {
        Log.e("ddddddd", "准备完成12")
        val vh = getHolder()
        val totalProgress = getTotalProgress()
        val currProgress = getCurrProgress()

        vh.tvCurrentTime.text = getTime(currProgress)
        vh.tvTotalTime.text = getTime(totalProgress)
        vh.sbProgress.max = totalProgress
        vh.sbProgress.progress = currProgress

    }

    private fun completePlayer() {
        val vh = getHolder()
        vh.ivPlay.isSelected = false
        vh.sbProgress.progress = 0
        vh.sbProgress.secondaryProgress = 0
        onPauseCover()
    }


    /***
     *
     * 计时更新进度
     *
     ***/

    override fun countTime() {
        if (isPlaying) {
            val vh = getHolder()
            val currProgress = getCurrProgress()
            vh.sbProgress.progress = currProgress
            vh.tvCurrentTime.text = getTime(currProgress)
            vh.sbProgress.secondaryProgress = getCurrBufferPercent()
        }
    }


    /***
     *
     * 音频的生命周期
     *
     ***/

    override fun onResume() {
        if(isPlaying()) {
            onPlayCover()
        }
    }

    override fun onPause() {
        onPauseCover()
    }

    override fun onDestroy() {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterReceiver(receiver)
        stopCount()
    }

    private fun getIntentFilter(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(AudioConfig.READY)
        filter.addAction(AudioConfig.COMPLETE)
//        filter.addAction(AudioConfig.START)
//        filter.addAction(AudioConfig.PAUSE)
//        filter.addAction(AudioConfig.STOP)
        return filter
    }


    inner class AudioUIReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            handlerReceiver(intent)
        }

    }

    private fun handlerReceiver(intent: Intent) {
        val action = intent.action
        when (action) {
            AudioConfig.READY -> loadReady()
            AudioConfig.COMPLETE -> completePlayer()
//            AudioConfig.START -> onPlayCover()
//            else -> onPauseCover()
        }
    }


}