package com.wms.media.music.views

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log

class AudioPlayerManager {
    private var player: MediaPlayer? = null
    private var isPrepared = false
    private var playState = 0
    private var mPercent = 0

    companion object {
        private object VH {
            var instance = AudioPlayerManager()
        }

        fun newInstance(): AudioPlayerManager {
            return VH.instance
        }
    }

    private fun getPlayer(): MediaPlayer {
        if(isInit()) {
            return player!!
        }
        player = MediaPlayer()
        return player!!
    }

    fun getCurrPlayer(): MediaPlayer? {
        return player
    }

    @Throws
    fun init() {
        if(isInit()) {
            Log.e("ddddddd", "已经初始化")
            return
        }
        Log.e("ddddddd", "初始化成功")
        val media = getPlayer()
        media.setOnPreparedListener { _ ->
            isPrepared = true
            handlerReady()
        }

        media.setOnErrorListener { _, _, _ ->
            handlerError()
            false
        }


        media.setOnCompletionListener {
            handlerComplete()
        }

        media.setOnInfoListener { _, what, _ ->
            playState = what
            true
        }
        media.setOnBufferingUpdateListener { _, percent -> mPercent = percent }

    }

    @Throws
    fun loadUrl(path: String) {
        if(isInit()) {
            val media = getCurrPlayer()!!
            isPrepared = false
            playState = 0
            mPercent = 0
            media.reset()
            media.setDataSource(path)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val attrBuilder = AudioAttributes.Builder()
                attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
            } else {
                media.setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
            media.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            media.prepareAsync()
        }
    }

    fun start() {
        if (isInit() && isPrepared) {
            getCurrPlayer()!!.start()
        }
    }

    fun pause() {
        if (isInit()) {
            getCurrPlayer()!!.pause()
        }
    }

    fun stop() {
        if (isInit()) {
            getCurrPlayer()!!.stop()
        }
    }

    fun seekProgress(progress: Int) {
        if (isInit()) {
            getCurrPlayer()!!.seekTo(progress)
        }
    }

    fun isPrepared(): Boolean {
        return isPrepared
    }

    fun getTotalProgress(): Int {
        if (isInit() && isPrepared) {
            return getCurrPlayer()!!.duration
        }
        return 0
    }

    fun getCurrProgress(): Int {
        if (isInit() && isPrepared) {
            return getCurrPlayer()!!.currentPosition
        }
        return 0
    }

    fun getCurrBufferPercent(): Int {
        return mPercent
    }

    fun getPlayState(): Int {
        return playState
    }

    private fun isInit(): Boolean {
        return player != null
    }

    fun onDestroy() {
        if(player != null) {
            player!!.stop()
            player!!.reset()
            player!!.release()
        }
        player = null
    }

}