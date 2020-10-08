package com.wms.media.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.wms.media.music.views.AudioPlayerManager

class MusicService: Service() {

    private var musicReceiver = MusicReceiver()

    override fun onCreate() {
        super.onCreate()
        registerReceiver(musicReceiver, getIntentFilter())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicReceiver)
        AudioPlayerManager.newInstance().onDestroy()
    }

    private fun getIntentFilter(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(AudioConfig.LOAD_URL)
        filter.addAction(AudioConfig.ERROR)
        filter.addAction(AudioConfig.STOP)
        filter.addAction(AudioConfig.START)
        filter.addAction(AudioConfig.PAUSE)
        filter.addAction(AudioConfig.SEEK_PROGRESS)


        filter.addAction(AudioConfig.NEXT)
        filter.addAction(AudioConfig.LAST)
        filter.addAction(AudioConfig.INFO)
        filter.addAction(AudioConfig.BUFFER)
        return filter
    }

    inner class MusicReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            handlerReceive(intent)
        }
    }

    fun handlerReceive(intent: Intent) {
        val action = intent.action
        when(intent.action) {
            AudioConfig.LOAD_URL -> loadUrl(intent)
            AudioConfig.ERROR -> 1
            AudioConfig.SEEK_PROGRESS -> seekProgress(intent)
            else -> handlerPlayer(action)
        }
    }

    private fun loadUrl(intent: Intent) {
        val path = intent.getStringExtra(AudioConfig.EXTRA_URL)
        Log.e("加载地址", ""+path)
        AudioPlayerManager.newInstance().loadUrl(path)
    }

    private fun seekProgress(intent: Intent) {
        val progress = intent.getIntExtra(AudioConfig.EXTRA_SEEK_PROGRESS, 0)
        AudioPlayerManager.newInstance().seekProgress(progress)
    }

    private fun handlerPlayer(action: String) {
        when(action) {
            AudioConfig.START -> AudioPlayerManager.newInstance().start()
            AudioConfig.PAUSE -> AudioPlayerManager.newInstance().pause()
            AudioConfig.STOP -> AudioPlayerManager.newInstance().stop()
        }
    }







}