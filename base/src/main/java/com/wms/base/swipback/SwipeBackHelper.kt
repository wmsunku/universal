package com.wms.base.swipback

import android.app.Activity

import java.util.Stack

/**
 * 滑动的全局管理类
 */
object SwipeBackHelper {
    private var page: SwipeBackPage? = null
    private val mPageStack = Stack<SwipeBackPage>()

    private fun findHelperByActivity(activity: Activity): SwipeBackPage? {
        for (swipeBackPage in mPageStack) {
            if (swipeBackPage.mActivity === activity) return swipeBackPage
        }
        return null
    }

    fun getCurrentPage(activity: Activity): SwipeBackPage {
        page = findHelperByActivity(activity)
        if (page == null) {
            throw RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first")
        }
        return page!!
    }

    fun onCreate(activity: Activity) {
         page = findHelperByActivity(activity)
        if (page == null) {
            page = mPageStack.push(SwipeBackPage(activity))
        }
        page!!.onCreate()
    }

    fun onPostCreate(activity: Activity) {
        page = findHelperByActivity(activity)
        if (page == null) {
            throw RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first")
        }
        page!!.onPostCreate()
    }

    fun onDestroy(activity: Activity) {
        page = findHelperByActivity(activity)
        if (page == null) {
            throw RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first")
        }
        mPageStack.remove(page)
        page!!.mActivity = null
    }

    fun finish(activity: Activity) {
        page = findHelperByActivity(activity)

        if (page == null) {
            throw RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first")
        }
        page!!.scrollToFinishActivity()
    }

    internal fun getPrePage(activity: SwipeBackPage): SwipeBackPage? {
        val index = mPageStack.indexOf(activity)
        return if (index > 0)
            mPageStack[index - 1]
        else
            null
    }

}
