package com.wms.media.sdk

interface OnBasePlayerActionListener {

    fun loadUrl(path: String)

    fun start()

    fun pause()

    fun stop()
}