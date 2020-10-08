package com.wms.base.swipback

interface SwipeListener {
    fun onScroll(percent: Float, px: Int)

    fun onEdgeTouch()

    fun onScrollToClose()
}