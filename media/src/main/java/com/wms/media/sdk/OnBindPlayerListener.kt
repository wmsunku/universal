package com.wms.media.sdk

interface OnBindPlayerListener {

    fun loadReady()

    fun loadError()

    fun completePlayer()

    fun mediaInfo(what: Int, extra: Int)

    fun buffer(percent: Int)

    fun changeSize(width: Int, height: Int)

}