package com.wms.bookreader.views.readview

import android.content.Context
import com.wms.bookreader.views.readview.bean.BookMixAToc

class NoAimWidget(context: Context, bookId: String, chaptersList: List<BookMixAToc.MixToc.Chapters>, listener: OnReadStateChangeListener) : OverlappedWidget(context, bookId, chaptersList, listener) {

    override fun startAnimation() {
        startAnimation(700)
    }

    fun startAnimation(duration: Int) {
        val dx: Int
        if (actiondownX > mScreenWidth / 2) {
            dx = (-(mScreenWidth + touch_down)).toInt()
            mScroller.startScroll((mScreenWidth + touch_down).toInt(), mTouch.y.toInt(), dx, 0, duration)
        } else {
            dx = (mScreenWidth - touch_down).toInt()
            mScroller.startScroll(touch_down.toInt(), mTouch.y.toInt(), dx, 0, duration)
        }
    }

}
