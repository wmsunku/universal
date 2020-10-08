package com.wms.base.swipback

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wms.base.R
import java.util.ArrayList

class SwipeBackLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs) {

    /**
     * Threshold of scroll, we will close the activity, when scrollPercent over
     * this value;
     */
    private var mScrollThreshold = DEFAULT_SCROLL_THRESHOLD

    private var mActivity: Activity? = null

    private var mEnable = true

    private var mDisallowIntercept = false

    private var mContentView: View? = null

    private val mDragHelper: ViewDragHelper

    private var mScrollPercent: Float = 0f

    private var mContentLeft: Int = 0

    /**
     * The set of listeners to be sent events through.
     */
    private var mListeners: MutableList<SwipeListener>? = null

    internal var mShadowLeft: Drawable? = null

    private var mScrimOpacity: Float = 0.toFloat()

    private var mScrimColor = DEFAULT_SCRIM_COLOR

    private var mInLayout: Boolean = false

    private val mTmpRect = Rect()

    /**
     * Edge being dragged
     */
    private var mTrackingEdge: Int = 0

    init {
        mDragHelper = ViewDragHelper.create(this, ViewDragCallback())

        setShadow(R.drawable.shadow_left)

        val density = resources.displayMetrics.density
        val minVel = MIN_FLING_VELOCITY * density
        setEdgeSize(resources.displayMetrics.widthPixels)
        mDragHelper.minVelocity = minVel
        mDragHelper.setMaxVelocity(minVel * 2f)
        mDragHelper.setSensitivity(context, 0.3f)
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
    }

    /**
     * Sets the sensitivity of the NavigationLayout.
     *
     * @param context     The application context.
     * @param sensitivity value between 0 and 1, the final value for touchSlop =
     * ViewConfiguration.getScaledTouchSlop * (1 / s);
     */
    fun setSensitivity(context: Context, sensitivity: Float) {
        mDragHelper.setSensitivity(context, sensitivity)
    }

    /**
     * Set up contentView which will be moved by user gesture
     *
     * @param view
     */
    private fun setContentView(view: View) {
        mContentView = view
    }

    fun setEnableGesture(enable: Boolean) {
        mEnable = enable
    }

    /**
     * Set a color to use for the scrim that obscures primary content while a
     * drawer is open.
     *
     * @param color Color to use in 0xAARRGGBB format.
     */
    fun setScrimColor(color: Int) {
        mScrimColor = color
        invalidate()
    }

    /**
     * Set the size of an edge. This is the range in pixels along the edges of
     * this view that will actively detect edge touches or drags if edge
     * tracking is enabled.
     *
     * @param size The size of an edge in pixels
     */
    fun setEdgeSize(size: Int) {
        mTrackingEdge = size
        mDragHelper.edgeSize = mTrackingEdge
    }


    fun setEdgeSizePercent(size: Float) {
        mTrackingEdge = (resources.displayMetrics.widthPixels * size).toInt()
        mDragHelper.edgeSize = mTrackingEdge
    }

    /**
     * Register a callback to be invoked when a swipe event is sent to this
     * view.
     *
     * @param listener the swipe listener to attach to this view
     */
    @Deprecated("use {@link #addSwipeListener} instead")
    fun setSwipeListener(listener: SwipeListener) {
        addSwipeListener(listener)
    }

    /**
     * Add a callback to be invoked when a swipe event is sent to this view.
     *
     * @param listener the swipe listener to attach to this view
     */
    fun addSwipeListener(listener: SwipeListener) {
        if (mListeners == null) {
            mListeners = ArrayList()
        }
        mListeners!!.add(listener)
    }

    /**
     * Removes a listener from the set of listeners
     *
     * @param listener
     */
    fun removeSwipeListener(listener: SwipeListener) {
        if (mListeners == null) {
            return
        }
        mListeners!!.remove(listener)
    }


    /**
     * Set scroll threshold, we will close the activity, when scrollPercent over
     * this value
     *
     * @param threshold
     */
    fun setScrollThreshold(threshold: Float) {
        if (threshold >= 1.0f || threshold <= 0) {
            throw IllegalArgumentException("Threshold value should be between 0 and 1.0")
        }
        mScrollThreshold = threshold
    }


    fun setShadow(shadow: Drawable) {
        mShadowLeft = shadow
        invalidate()
    }


    fun setShadow(resId: Int) {
        setShadow(resources.getDrawable(resId))
    }

    /**
     * Scroll out contentView and finish the activity
     */
    fun scrollToFinishActivity() {
        val childWidth = mContentView!!.width
        var left = 0
        val top = 0
        left = childWidth + mShadowLeft!!.intrinsicWidth + OVERSCROLL_DISTANCE
        mDragHelper.smoothSlideViewTo(mContentView!!, left, top)
        invalidate()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!mEnable || mDisallowIntercept) {
            return false
        }
        try {
            return mDragHelper.shouldInterceptTouchEvent(event)
        } catch (e: Exception) {
            //            e.printStackTrace();
            return false
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mEnable) {
            return false
        }
        try {
            mDragHelper.processTouchEvent(event)
        } catch (e: Exception) {
            //            e.printStackTrace();
            return false
        }

        return true
    }

    fun setDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        mDisallowIntercept = disallowIntercept
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mInLayout = true
        if (mContentView != null)
            mContentView!!.layout(mContentLeft, 0,
                    mContentLeft + mContentView!!.measuredWidth,
                    mContentView!!.measuredHeight)
        mInLayout = false
    }

    override fun requestLayout() {
        if (!mInLayout) {
            super.requestLayout()
        }
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val drawContent = child === mContentView

        val ret = super.drawChild(canvas, child, drawingTime)
        if (mScrimOpacity > 0 && drawContent
                && mDragHelper.viewDragState != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child)
            drawScrim(canvas, child)
        }
        return ret
    }

    private fun drawScrim(canvas: Canvas, child: View) {
        val baseAlpha = (mScrimColor and -0x1000000).ushr(24)
        val alpha = (baseAlpha * mScrimOpacity).toInt()
        val color = alpha shl 24 or (mScrimColor and 0xffffff)
        canvas.clipRect(0, 0, child.left, height)
        canvas.drawColor(color)
    }

    private fun drawShadow(canvas: Canvas, child: View) {
        val childRect = mTmpRect
        child.getHitRect(childRect)

        mShadowLeft!!.setBounds(childRect.left - mShadowLeft!!.intrinsicWidth, childRect.top,
                childRect.left, childRect.bottom)
        mShadowLeft!!.alpha = (mScrimOpacity * FULL_ALPHA).toInt()
        mShadowLeft!!.draw(canvas)
    }

    fun attachToActivity(activity: Activity) {
        if (parent != null) {
            return
        }
        mActivity = activity
        val a = activity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()

        val decor = activity.window.decorView as ViewGroup
        var decorChild = decor.findViewById<View>(android.R.id.content)
        while (decorChild.parent !== decor) {
            decorChild = decorChild.parent as View
        }
        decorChild.setBackgroundResource(background)
        decor.removeView(decorChild)
        addView(decorChild)
        setContentView(decorChild)
        decor.addView(this)
    }

    fun removeFromActivity(activity: Activity) {
        if (parent == null) return
        val decorChild = getChildAt(0) as ViewGroup
        val decor = activity.window.decorView as ViewGroup
        decor.removeView(this)
        removeView(decorChild)
        decor.addView(decorChild)
    }

    override fun computeScroll() {
        mScrimOpacity = 1 - mScrollPercent
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private inner class ViewDragCallback : ViewDragHelper.Callback() {
        private var mIsScrollOverValid: Boolean = false

        override fun tryCaptureView(view: View, i: Int): Boolean {
            val ret = mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT, i)
            if (ret) {
                if (mListeners != null && !mListeners!!.isEmpty()) {
                    for (listener in mListeners!!) {
                        listener.onEdgeTouch()
                    }
                }
                mIsScrollOverValid = true
            }
            return ret
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return mTrackingEdge
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 0
        }

        override fun onViewPositionChanged(changedView: View?, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            mScrollPercent = Math.abs(left.toFloat() / mContentView!!.width)
            mContentLeft = left
            invalidate()
            if (mScrollPercent < mScrollThreshold && !mIsScrollOverValid) {
                mIsScrollOverValid = true
            }

            if (mListeners != null && !mListeners!!.isEmpty()) {
                for (listener in mListeners!!) {
                    listener.onScroll(mScrollPercent, mContentLeft)
                }
            }
            if (mScrollPercent >= 1) {
                if (!mActivity!!.isFinishing) {
                    if (mListeners != null && !mListeners!!.isEmpty()
                            && mScrollPercent >= mScrollThreshold && mIsScrollOverValid) {
                        mIsScrollOverValid = false
                        for (listener in mListeners!!) {
                            listener.onScrollToClose()
                        }
                    }
                    mActivity!!.finish()
                }
            }


        }

        override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
            val childWidth = releasedChild!!.width
            val top = 0
            val left = if(xvel >= 0 && mScrollPercent > mScrollThreshold){
                childWidth + mShadowLeft!!.intrinsicWidth + OVERSCROLL_DISTANCE
            }else {
                0
            }

            mDragHelper.settleCapturedViewAt(left, top)
            invalidate()
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return Math.min(child.width, Math.max(left, 0))
        }

    }

    companion object {
        private val TAG = "ViewDragHelper"

        /**
         * Minimum velocity that will be detected as a fling
         */
        private val MIN_FLING_VELOCITY = 400 // dips per second

        private val DEFAULT_SCRIM_COLOR = -0x67000000

        private val FULL_ALPHA = 255


        /**
         * A view is currently being dragged. The position is currently changing as
         * a result of user input or simulated user input.
         */
        val STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING

        /**
         * A view is currently settling into place as a result of a fling or
         * predefined non-interactive motion.
         */
        val STATE_SETTLING = ViewDragHelper.STATE_SETTLING

        /**
         * Default threshold of scroll
         */
        private val DEFAULT_SCROLL_THRESHOLD = 0.3f

        private const val OVERSCROLL_DISTANCE = 10
    }
}
