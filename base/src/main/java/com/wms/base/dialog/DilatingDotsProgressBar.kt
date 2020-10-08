package com.wms.base.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.wms.base.R
import java.util.ArrayList

class DilatingDotsProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mDotColor: Int = 0
    var dotGrowthSpeed: Int = 0
        private set
    private var mWidthBetweenDotCenters: Int = 0
    private var mNumberDots: Int = 0
    private var mDotRadius: Float = 0.toFloat()
    var dotScaleMultiplier: Float = 0.toFloat()
        private set
    private var mDotMaxRadius: Float = 0.toFloat()
    var horizontalSpacing: Float = 0.toFloat()
        private set
    private var mStartTime: Long = -1
    private var mShouldAnimate: Boolean = false
    private var mDismissed = false
    private val mDrawables = ArrayList<DilatingDotDrawable>()
    private val mAnimations = ArrayList<Animator>()

    /** delayed runnable to stop the progress  */
    private val mDelayedHide = Runnable {
        mStartTime = -1
        visibility = View.GONE
        stopAnimations()
    }
    /** delayed runnable to start the progress  */
    private val mDelayedShow = Runnable {
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis()
            visibility = View.VISIBLE
            startAnimations()
        }
    }

    // -------------------------------
    // ------ Getters & Setters ------
    // -------------------------------

    var dotRadius: Float
        get() = mDotRadius
        set(radius) {
            reset()
            mDotRadius = radius
            calculateMaxRadius()
            calculateWidthBetweenDotCenters()
            setupDots()
        }

    var numberOfDots: Int
        get() = mNumberDots
        set(numDots) {
            reset()
            mNumberDots = numDots
            setupDots()
        }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DilatingDotsProgressBar)
        mNumberDots = a.getInt(R.styleable.DilatingDotsProgressBar_dd_numDots, 3)
        mDotRadius = a.getDimension(R.styleable.DilatingDotsProgressBar_android_radius, 8f)
        mDotColor = a.getColor(R.styleable.DilatingDotsProgressBar_android_color, -0x63d850)
        dotScaleMultiplier = a.getFloat(
                R.styleable.DilatingDotsProgressBar_dd_scaleMultiplier,
                DEFAULT_GROWTH_MULTIPLIER)
        dotGrowthSpeed = a.getInt(R.styleable.DilatingDotsProgressBar_dd_animationDuration, 300)
        horizontalSpacing = a.getDimension(R.styleable.DilatingDotsProgressBar_dd_horizontalSpacing, 12f)
        a.recycle()

        mShouldAnimate = false
        calculateMaxRadius()
        calculateWidthBetweenDotCenters()

        initDots()
        updateDots()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (computeMaxHeight() != h.toFloat() || w.toFloat() != computeMaxWidth()) {
            updateDots()
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(mDelayedHide)
        removeCallbacks(mDelayedShow)
    }

    fun reset() {
        hideNow()
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    fun hide() {
        hide(MIN_SHOW_TIME)
    }

    fun hide(delay: Int) {
        mDismissed = true
        removeCallbacks(mDelayedShow)
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= delay || mStartTime == -1L) {
            mDelayedHide.run()
        } else {
            if (delay - diff <= 0) {
                mDelayedHide.run()
            } else {
                postDelayed(mDelayedHide, delay - diff)
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    fun show() {
        show(MIN_DELAY)
    }

    fun showNow() {
        show(0)
    }

    fun hideNow() {
        hide(0)
    }

    fun show(delay: Int) {
        mStartTime = -1
        mDismissed = false
        removeCallbacks(mDelayedHide)

        if (delay == 0) {
            mDelayedShow.run()
        } else {
            postDelayed(mDelayedShow, delay.toLong())
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (shouldAnimate()) {
            for (dot in mDrawables) {
                dot.draw(canvas)
            }
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return if (shouldAnimate()) {
            mDrawables.contains(who)
        } else super.verifyDrawable(who)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(computeMaxWidth().toInt(), computeMaxHeight().toInt())
    }

    private fun computeMaxHeight(): Float {
        return mDotMaxRadius * 2
    }

    private fun computeMaxWidth(): Float {
        return computeWidth() + (mDotMaxRadius - mDotRadius) * 2
    }

    private fun computeWidth(): Float {
        return (mDotRadius * 2 + horizontalSpacing) * mDrawables.size - horizontalSpacing
    }

    private fun calculateMaxRadius() {
        mDotMaxRadius = mDotRadius * dotScaleMultiplier
    }

    private fun calculateWidthBetweenDotCenters() {
        mWidthBetweenDotCenters = (mDotRadius * 2).toInt() + horizontalSpacing.toInt()
    }

    private fun initDots() {
        mDrawables.clear()
        mAnimations.clear()
        for (i in 1..mNumberDots) {
            val dot = DilatingDotDrawable(mDotColor, mDotRadius, mDotMaxRadius)
            dot.callback = this
            mDrawables.add(dot)

            val growAnimator = ObjectAnimator.ofFloat(dot, "radius", mDotRadius, mDotMaxRadius, mDotRadius)
            growAnimator.duration = dotGrowthSpeed.toLong()
            growAnimator.interpolator = AccelerateDecelerateInterpolator()

            if (i == mNumberDots) {
                growAnimator.addListener(
                        object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                if (shouldAnimate()) {
                                    startAnimations()
                                }
                            }
                        })
            }

            growAnimator.startDelay = ((i - 1) * (START_DELAY_FACTOR * dotGrowthSpeed).toInt()).toLong()
            mAnimations.add(growAnimator)
        }
    }

    private fun updateDots() {
        if (mDotRadius <= 0) {
            mDotRadius = height.toFloat() / 2f / dotScaleMultiplier
        }

        var left = (mDotMaxRadius - mDotRadius).toInt()
        var right = (left + mDotRadius * 2).toInt() + 2
        val top = 0
        val bottom = (mDotMaxRadius * 2).toInt() + 2

        for (i in mDrawables.indices) {
            val dot = mDrawables[i]
            dot.setRadius(mDotRadius)
            dot.setBounds(left, top, right, bottom)
            val growAnimator = mAnimations[i] as ValueAnimator
            growAnimator.setFloatValues(mDotRadius, mDotRadius * dotScaleMultiplier, mDotRadius)

            left += mWidthBetweenDotCenters
            right += mWidthBetweenDotCenters
        }
    }

    protected fun startAnimations() {
        mShouldAnimate = true
        for (anim in mAnimations) {
            anim.start()
        }
    }

    protected fun stopAnimations() {
        mShouldAnimate = false
        removeCallbacks()
        for (anim in mAnimations) {
            anim.cancel()
        }
    }

    protected fun shouldAnimate(): Boolean {
        return mShouldAnimate
    }

    fun setDotSpacing(horizontalSpacing: Float) {
        reset()
        this.horizontalSpacing = horizontalSpacing
        calculateWidthBetweenDotCenters()
        setupDots()
    }

    fun setGrowthSpeed(growthSpeed: Int) {
        reset()
        dotGrowthSpeed = growthSpeed
        setupDots()
    }

    fun setDotScaleMultpiplier(multplier: Float) {
        reset()
        dotScaleMultiplier = multplier
        calculateMaxRadius()
        setupDots()
    }

    fun setDotColor(color: Int) {
        mDotColor = color
        for (dot in mDrawables) {
            dot.setColor(mDotColor)
        }
    }

    private fun setupDots() {
        initDots()
        updateDots()
        showNow()
    }

    companion object {
        val TAG = DilatingDotsProgressBar::class.java.simpleName
        val START_DELAY_FACTOR = 0.35
        private val DEFAULT_GROWTH_MULTIPLIER = 1.75f
        private val MIN_SHOW_TIME = 500 // ms
        private val MIN_DELAY = 500 // ms
    }
}
