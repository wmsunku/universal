package com.wms.media.music.views

import android.content.Intent
import android.util.Log
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.sendBroadcast
import com.wms.media.sdk.CountManager
import com.wms.media.sdk.OnCountTimeListener
import com.wms.media.service.AudioConfig
import com.wms.media.service.MusicService

fun BaseActivity.startMusicService() {
    AudioPlayerManager.newInstance().init()
    Log.e("ddddddd", "开启服务")
    val intent = Intent(this, MusicService::class.java)
    startService(intent)
}

fun handlerError() {
    val intent = Intent()
    intent.action = AudioConfig.ERROR
    sendBroadcast(intent)
}

fun handlerReady() {
    Log.e("ddddddd", "准备完成")
    val intent = Intent()
    intent.action = AudioConfig.READY
    sendBroadcast(intent)
}

fun handlerComplete() {
    val intent = Intent()
    intent.action = AudioConfig.COMPLETE
    sendBroadcast(intent)
}

fun handlerSeek(progress: Int) {
    val intent = Intent()
    intent.action = AudioConfig.SEEK_PROGRESS
    intent.putExtra(AudioConfig.EXTRA_SEEK_PROGRESS, progress)
    sendBroadcast(intent)
}

fun handlerNext() {
    val intent = Intent()
    intent.action = AudioConfig.NEXT
    sendBroadcast(intent)
}

fun handlerLast() {
    val intent = Intent()
    intent.action = AudioConfig.LAST
    sendBroadcast(intent)
}

fun handlerLoadUrl(url: String) {
    Log.e("dddddddd", "加载歌曲" + url)
    val intent = Intent()
    intent.action = AudioConfig.LOAD_URL
    intent.putExtra(AudioConfig.EXTRA_URL, url)
    sendBroadcast(intent)
}

fun handlerStart() {
    val intent = Intent()
    intent.action = AudioConfig.START
    sendBroadcast(intent)
}

fun handlerPause() {
    val intent = Intent()
    intent.action = AudioConfig.PAUSE
    sendBroadcast(intent)
}

fun handlerStop() {
    val intent = Intent()
    intent.action = AudioConfig.STOP
    sendBroadcast(intent)
}


fun AudioPlayer.isPrepared(): Boolean {
    return AudioPlayerManager.newInstance().isPrepared()
}

fun AudioPlayer.getCurrProgress(): Int {
    return AudioPlayerManager.newInstance().getCurrProgress()
}

fun AudioPlayer.getTotalProgress(): Int {
    return AudioPlayerManager.newInstance().getTotalProgress()
}

fun AudioPlayer.getCurrBufferPercent(): Int {
    val total = getTotalProgress()
    val bufferPercent = AudioPlayerManager.newInstance().getCurrBufferPercent()
    return (total * bufferPercent / 100)
}

fun isPlaying(): Boolean {
    val player = AudioPlayerManager.newInstance().getCurrPlayer()
    return if(player == null) false else player.isPlaying
}

fun startCount(listener: OnCountTimeListener) {
    val delay = 0L
    val period = 1000 / 24L
    CountManager.newInstance().startCount(delay, period, listener)
}

fun stopCount() {
    CountManager.newInstance().stopCount()
}


