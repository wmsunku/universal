package com.wms.media.video.views

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import com.wms.base.api.DataAPI
import com.wms.media.sdk.OnBindPlayerListener

class VideoManager {

    private var playerMap = HashMap<String, MediaPlayer>()
    private var currPlayer: MediaPlayer? = null
    private var currTaskNum: String? = null
    private var isOnly = true
    private var isPrepared = false

    companion object {
        private object VH {
            val instance = VideoManager()
        }

        fun newInstance(): VideoManager {
            return VH.instance
        }
    }

    @Throws
    fun getCurrPlayer(taskNum: String): MediaPlayer {
        if (DataAPI.isEmpty(taskNum) || DataAPI.isEmpty(playerMap)) {
            return createPlayer(taskNum, true)
        }
        return createPlayer(taskNum, playerMap[taskNum] == null)
    }

    private fun getCurrPlayer(): MediaPlayer {
        return getCurrPlayer(currTaskNum!!)
    }

    fun isPrepared(): Boolean {
        return isPrepared
    }

    private fun createPlayer(taskNum: String, isNew: Boolean): MediaPlayer {
        currPlayer = if (isNew) MediaPlayer() else playerMap[taskNum]
        playerMap[taskNum] = currPlayer!!
        return currPlayer!!
    }

    @Throws
    fun init(taskNum: String, listener: OnBindPlayerListener) {
        currTaskNum = taskNum
        val media = getCurrPlayer(taskNum)

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

    @Throws
    fun loadUrl(path: String) {
        isPrepared = false
        val media = getCurrPlayer(currTaskNum!!)
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

    fun start() {
        if (isPrepared && currPlayer != null) {
            getCurrPlayer().start()
        }
    }

    fun pause() {
        if (currPlayer != null) {
            getCurrPlayer().pause()
        }
    }

    fun stop() {
        if (currPlayer != null) {
            getCurrPlayer().stop()
        }
    }

    fun seekProgress(progress: Int) {
        if (currPlayer != null) {
            getCurrPlayer().seekTo(progress)
        }
    }

    fun getTotalProgress(): Int {
        if (isPrepared && currPlayer != null) {
            return getCurrPlayer().duration
        }
        return 0
    }

    fun getCurrProgress(): Int {
        if (isPrepared && currPlayer != null) {
            return getCurrPlayer().currentPosition
        }
        return 0
    }

    private fun freeAll() {
        if (DataAPI.isEmpty(playerMap)) {
            return
        }
        for (m in playerMap.values) {
            m.stop()
            m.reset()
            m.release()
        }
        playerMap.clear()
    }

    fun onDestroy() {
        freeAll()
        currPlayer = null
        currTaskNum = null
    }

}