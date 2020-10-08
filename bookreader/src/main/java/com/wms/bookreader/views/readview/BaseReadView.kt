/**
 * Copyright 2016 JustWayward Team
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wms.bookreader.views.readview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.wms.base.sdk.getScreenHeight
import com.wms.base.sdk.getScreenWidth
import com.wms.base.sdk.showTost

import com.wms.bookreader.views.readview.bean.BookMixAToc
import com.wms.bookreader.views.readview.manager.SettingManager
import com.wms.bookreader.views.readview.manager.ThemeManager

/**
 * @author yuyh.
 * @date 2016/10/18.
 */
abstract class BaseReadView(context: Context,  var bookId: String, chaptersList: List<BookMixAToc.MixToc.Chapters>,
                             var listener: OnReadStateChangeListener) : View(context) {

    protected var mScreenWidth: Int = 0
    protected var mScreenHeight: Int = 0

    protected var mTouch = PointF()
    protected var actiondownX: Float = 0.toFloat()
    protected var actiondownY: Float = 0.toFloat()
    protected var touch_down = 0f // 当前触摸点与按下时的点的差值

    protected var mCurPageBitmap: Bitmap? = null
    protected var mNextPageBitmap: Bitmap? = null
    protected var mCurrentPageCanvas: Canvas
    protected var mNextPageCanvas: Canvas
    protected var pagefactory: PageFactory? = null
    var isPrepared = false

    internal var mScroller: Scroller

    private var dx: Int = 0
    private var dy: Int = 0
    private var et: Long = 0
    private var cancel = false
    private var center = false

    val readPos: IntArray
        get() = pagefactory!!.position

    val headLine: String
        get() = pagefactory!!.headLineStr.replace("@".toRegex(), "")

    init {

        mScreenWidth = getScreenWidth()
        mScreenHeight = getScreenHeight()

        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888)
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888)
        mCurrentPageCanvas = Canvas(mCurPageBitmap!!)
        mNextPageCanvas = Canvas(mNextPageBitmap!!)

        mScroller = Scroller(getContext())


        pagefactory = PageFactory(getContext(), bookId, chaptersList)
         pagefactory!!.setOnReadStateChangeListener(listener)
    }

    @Synchronized
    fun init(theme: Int) {
        if (!isPrepared) {
            try {
                pagefactory!!.setBgBitmap(ThemeManager.getThemeDrawable(theme))
                // 自动跳转到上次阅读位置
                val pos = SettingManager.Instance().getReadProgress(bookId)
                val ret = pagefactory!!.openBook(pos[0], intArrayOf(pos[1], pos[2]))
//                LogUtils.i("上次阅读位置：chapter=" + pos[0] + " startPos=" + pos[1] + " endPos=" + pos[2])
                if (ret == 0) {
                    listener.onLoadChapterFailure(pos[0])
                    return
                }
                pagefactory!!.onDraw(mCurrentPageCanvas)
                postInvalidate()
            } catch (e: Exception) {
            }

            isPrepared = true
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                et = System.currentTimeMillis()
                dx = e.x.toInt()
                dy = e.y.toInt()
                mTouch.x = dx.toFloat()
                mTouch.y = dy.toFloat()
                actiondownX = dx.toFloat()
                actiondownY = dy.toFloat()
                touch_down = 0f
                pagefactory!!.onDraw(mCurrentPageCanvas)
                if (actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3) {
                    center = true
                } else {
                    center = false
                    calcCornerXY(actiondownX, actiondownY)
                    if (actiondownX < mScreenWidth / 2) {// 从左翻
                        val status = pagefactory!!.prePage()
                        if (status == BookConfig.NO_PRE_PAGE) {
                            showTost("没有上一页啦")
                            return false
                        } else if (status == BookConfig.LOAD_SUCCESS) {
                            abortAnimation()
                            pagefactory!!.onDraw(mNextPageCanvas)
                        } else {
                            return false
                        }
                    } else if (actiondownX >= mScreenWidth / 2) {// 从右翻
                        val status = pagefactory!!.nextPage()
                        if (status == BookConfig.NO_NEXT_PAGE) {
                            showTost("没有下一页啦")
                            return false
                        } else if (status == BookConfig.LOAD_SUCCESS) {
                            abortAnimation()
                            pagefactory!!.onDraw(mNextPageCanvas)
                        } else {
                            return false
                        }
                    }
                    listener.onFlip()
                    setBitmaps(mCurPageBitmap, mNextPageBitmap)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val mx = e.x.toInt()
                val my = e.y.toInt()
                cancel = actiondownX < mScreenWidth / 2 && mx < mTouch.x || actiondownX > mScreenWidth / 2 && mx > mTouch.x
                mTouch.x = mx.toFloat()
                mTouch.y = my.toFloat()
                touch_down = mTouch.x - actiondownX
                this.postInvalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                val t = System.currentTimeMillis()
                val ux = e.x.toInt()
                val uy = e.y.toInt()

                if (center) { // ACTION_DOWN的位置在中间，则不响应滑动事件
                    resetTouchPoint()
                    if (Math.abs(ux - actiondownX) < 5 && Math.abs(uy - actiondownY) < 5) {
                        listener.onCenterClick()
                        return false
                    }
                }

                if (Math.abs(ux - dx) < 10 && Math.abs(uy - dy) < 10) {
                    if (t - et < 1000) { // 单击
                        if (this is NoAimWidget) {
                            this.startAnimation(0)
                        } else {
                            startAnimation()
                        }
                    } else { // 长按
                        pagefactory!!.cancelPage()
                        restoreAnimation()
                    }
                    postInvalidate()
                    return true
                }
                if (cancel) {
                    pagefactory!!.cancelPage()
                    restoreAnimation()
                    postInvalidate()
                } else {
                    startAnimation()
                    postInvalidate()
                }
                cancel = false
                center = false
            }
            else -> {
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        calcPoints()
        drawCurrentPageArea(canvas)
        drawNextPageAreaAndShadow(canvas)
        drawCurrentPageShadow(canvas)
        drawCurrentBackArea(canvas)
    }

    protected abstract fun drawNextPageAreaAndShadow(canvas: Canvas)

    protected abstract fun drawCurrentPageShadow(canvas: Canvas)

    protected abstract fun drawCurrentBackArea(canvas: Canvas)

    protected abstract fun drawCurrentPageArea(canvas: Canvas)

    protected abstract fun calcPoints()

    protected abstract fun calcCornerXY(x: Float, y: Float)

    /**
     * 开启翻页
     */
    protected abstract fun startAnimation()

    /**
     * 停止翻页动画（滑到一半调用停止的话  翻页效果会卡住 可调用#{restoreAnimation} 还原效果）
     */
    protected abstract fun abortAnimation()

    /**
     * 还原翻页
     */
    protected abstract fun restoreAnimation()

    protected abstract fun setBitmaps(mCurPageBitmap: Bitmap?, mNextPageBitmap: Bitmap?)

    abstract fun setTheme(theme: Int)

    /**
     * 复位触摸点位
     */
    protected fun resetTouchPoint() {
        mTouch.x = 0.1f
        mTouch.y = 0.1f
        touch_down = 0f
        calcCornerXY(mTouch.x, mTouch.y)
    }

    open fun jumpToChapter(chapter: Int) {
        resetTouchPoint()
        pagefactory!!.openBook(chapter, intArrayOf(0, 0))
        pagefactory!!.onDraw(mCurrentPageCanvas)
        pagefactory!!.onDraw(mNextPageCanvas)
        postInvalidate()
    }

    fun nextPage() {
        val status = pagefactory!!.nextPage()
        if (status == BookConfig.NO_NEXT_PAGE) {
            showTost("没有下一页啦")
            return
        } else if (status == BookConfig.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory!!.onDraw(mCurrentPageCanvas)
                pagefactory!!.onDraw(mNextPageCanvas)
                postInvalidate()
            }
        } else {
            return
        }

    }

    fun prePage() {
        val status = pagefactory!!.prePage()
        if (status == BookConfig.NO_PRE_PAGE) {
            showTost("没有上一页啦")
            return
        } else if (status == BookConfig.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory!!.onDraw(mCurrentPageCanvas)
                pagefactory!!.onDraw(mNextPageCanvas)
                postInvalidate()
            }
        } else {
            return
        }
    }

    @Synchronized
    fun setFontSize(fontSizePx: Int) {
        resetTouchPoint()
        pagefactory!!.textFont = fontSizePx
        if (isPrepared) {
            pagefactory!!.onDraw(mCurrentPageCanvas)
            pagefactory!!.onDraw(mNextPageCanvas)
            //SettingManager.getInstance().saveFontSize(bookId, fontSizePx);
            SettingManager.Instance().saveFontSize(fontSizePx)
            postInvalidate()
        }
    }

    @Synchronized
    fun setTextColor(textColor: Int, titleColor: Int) {
        resetTouchPoint()
        pagefactory!!.setTextColor(textColor, titleColor)
        if (isPrepared) {
            pagefactory!!.onDraw(mCurrentPageCanvas)
            pagefactory!!.onDraw(mNextPageCanvas)
            postInvalidate()
        }
    }

    fun setBattery(battery: Int) {
        pagefactory!!.setBattery(battery)
        if (isPrepared) {
            pagefactory!!.onDraw(mCurrentPageCanvas)
            postInvalidate()
        }
    }

    fun setTime(time: String) {
        pagefactory!!.setTime(time)
    }

    fun setPosition(pos: IntArray) {
        val ret = pagefactory!!.openBook(pos[0], intArrayOf(pos[1], pos[2]))
        if (ret == 0) {
            listener.onLoadChapterFailure(pos[0])
            return
        }
        pagefactory!!.onDraw(mCurrentPageCanvas)
        postInvalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (pagefactory != null) {
            pagefactory!!.recycle()
        }

        if (mCurPageBitmap != null && !mCurPageBitmap!!.isRecycled) {
            mCurPageBitmap!!.recycle()
            mCurPageBitmap = null
//            LogUtils.d("mCurPageBitmap recycle")
        }

        if (mNextPageBitmap != null && !mNextPageBitmap!!.isRecycled) {
            mNextPageBitmap!!.recycle()
            mNextPageBitmap = null
//            LogUtils.d("mNextPageBitmap recycle")
        }
    }
}
