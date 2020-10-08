package com.wms.base.swipback

import android.os.Build

class RelateSlider(curActivity: SwipeBackPage) : SwipeListener {
    private var mOffset = 500
    private var currPage = curActivity

    fun setOffset(offset: Int) {
        mOffset = offset
    }

    fun setEnable(enable: Boolean) {
        if(enable) {
            currPage.addListener(this)
        }else {
            currPage.removeListener(this)
        }
    }

    override fun onScroll(percent: Float, px: Int) {
        if (Build.VERSION.SDK_INT > 11) {
            val page = SwipeBackHelper.getPrePage(currPage)
            if(page != null) {
                page.swipeBackLayout!!.x = -mOffset * (Math.max(1-percent, 0f))
                if(percent == 0f) {
                    page.swipeBackLayout!!.x = 0f
                }
            }

        }
    }

    override fun onEdgeTouch() {
    }

    override fun onScrollToClose() {
        if (Build.VERSION.SDK_INT > 11) {
            val page = SwipeBackHelper.getPrePage(currPage)
            if(page != null) {
                page.swipeBackLayout!!.x = 0f
            }
        }
    }


}