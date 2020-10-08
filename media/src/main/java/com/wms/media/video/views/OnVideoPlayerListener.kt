package com.wms.media.video.views

import com.wms.media.sdk.OnBasePlayerActionListener

interface OnVideoPlayerListener: OnBasePlayerActionListener {

    fun enterFull()

    fun exitFull()

    fun showLoading()

    fun hideLoading()

}