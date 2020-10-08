package com.wms.base.swipback

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup

class SwipeBackPage internal constructor(var activity: Activity?) {
    var swipeBackLayout: SwipeBackLayout? = null
    var mActivity = activity
    private var slider: RelateSlider? = null

    internal fun onCreate() {
        mActivity!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mActivity!!.window.decorView.setBackgroundColor(Color.TRANSPARENT)
        swipeBackLayout = SwipeBackLayout(mActivity!!)
        swipeBackLayout!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        slider = RelateSlider(this)
    }

    internal fun onPostCreate() {
        swipeBackLayout!!.attachToActivity(mActivity!!)
    }


    @TargetApi(11)
    fun setSwipeRelateEnable(enable: Boolean): SwipeBackPage {
        slider!!.setEnable(enable)
        return this
    }

    fun setSwipeRelateOffset(offset: Int): SwipeBackPage {
        slider!!.setOffset(offset)
        return this
    }

    fun setSwipeBackEnable(enable: Boolean): SwipeBackPage {
        if (enable) {
            swipeBackLayout!!.attachToActivity(mActivity!!)
        } else {
            swipeBackLayout!!.removeFromActivity(mActivity!!)
        }
        swipeBackLayout!!.setEnableGesture(enable)
        return this
    }

    fun setSwipeEdge(swipeEdge: Int): SwipeBackPage {
        swipeBackLayout!!.setEdgeSize(swipeEdge)
        return this
    }

    fun setSwipeEdgePercent(swipeEdgePercent: Float): SwipeBackPage {
        swipeBackLayout!!.setEdgeSizePercent(swipeEdgePercent)
        return this
    }

    fun setSwipeSensitivity(sensitivity: Float): SwipeBackPage {
        swipeBackLayout!!.setSensitivity(mActivity!!, sensitivity)
        return this
    }

    fun setScrimColor(color: Int): SwipeBackPage {
        swipeBackLayout!!.setScrimColor(color)
        return this
    }

    fun setClosePercent(percent: Float): SwipeBackPage {
        swipeBackLayout!!.setScrollThreshold(percent)
        return this
    }

    fun setDisallowInterceptTouchEvent(disallowIntercept: Boolean): SwipeBackPage {
        swipeBackLayout!!.setDisallowInterceptTouchEvent(disallowIntercept)
        return this
    }

    fun addListener(listener: SwipeListener): SwipeBackPage {
        swipeBackLayout!!.addSwipeListener(listener)
        return this
    }

    fun removeListener(listener: SwipeListener): SwipeBackPage {
        swipeBackLayout!!.removeSwipeListener(listener)
        return this
    }

    fun scrollToFinishActivity() {
        swipeBackLayout!!.scrollToFinishActivity()
    }

}