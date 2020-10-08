package com.wms.media.video.views

interface OnVideoLifeListener {

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()
}