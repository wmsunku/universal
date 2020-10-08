package com.wms.media.sdk

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build

class MediaManager {
    private var player: MediaPlayer? = null
    private var isPrepared = false

    companion object {
        private object VH {
            var instance = MediaManager()
        }

        @Synchronized
        fun newInstance(): MediaManager {
            return VH.instance
        }
    }

    fun getPlayer(): MediaPlayer {
        if(player != null) {
            if(player!!.isPlaying) {
                player!!.stop()
                player!!.reset()
                player!!.release()
            }
        }

        if(player == null) {
            player = MediaPlayer()
        }
        return player!!
    }

    @Throws
    fun init(listener: OnBindPlayerListener) {
        val media = getPlayer()
        media.setOnPreparedListener { _ ->
            isPrepared = true
            listener.loadReady()
        }

        media.setOnErrorListener { _, _, _ ->
            listener.loadError()
            false
        }


        media.setOnCompletionListener {
            listener.completePlayer()
        }

        media.setOnInfoListener { _, what, extra ->
            listener.mediaInfo(what, extra)
            true
        }

        media.setOnBufferingUpdateListener { _, percent -> listener.buffer(percent) }

        media.setOnVideoSizeChangedListener { _, width, height -> listener.changeSize(width, height) }
    }

    fun loadUrl(path: String) {
        val media = getPlayer()
        isPrepared = false
        media.reset()
        media.setDataSource(path)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attrBuilder =  AudioAttributes.Builder()
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
        }else {
            media.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        media.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        media.prepareAsync()
    }

    fun start() {
        if (isPrepared) {
            player!!.start()
        }
    }

    fun pause() {
        if (player != null) {
            player!!.pause()
        }
    }

    fun stop() {
        if (player != null) {
            player!!.stop()
        }
    }

    fun seekProgress(progress: Int) {
        if (player != null) {
            player!!.seekTo(progress)
        }
    }

    fun isPrepared(): Boolean {
        return isPrepared
    }

    fun getTotalProgress(): Int {
        if (player != null && isPrepared) {
            return player!!.duration
        }
        return 0
    }

    fun getCurrProgress(): Int {
        if (player != null && isPrepared) {
            return player!!.currentPosition
        }
        return 0
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