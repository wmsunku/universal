package com.wms.base.dialog

import android.graphics.*
import android.graphics.drawable.Drawable

class DilatingDotDrawable(color: Int, private var radius: Float, maxRadius: Float) : Drawable() {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var maxRadius: Float = 0.toFloat()
    private val mDirtyBounds = Rect(0, 0, 0, 0)

    init {
        mPaint.color = color
        mPaint.style = Paint.Style.FILL
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        setMaxRadius(maxRadius)
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        canvas.drawCircle(bounds.centerX().toFloat(), bounds.centerY().toFloat(), radius - 1, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        if (alpha != mPaint.alpha) {
            mPaint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setColor(color: Int) {
        mPaint.color = color
        invalidateSelf()
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        invalidateSelf()
    }

    fun getRadius(): Float {
        return radius
    }

    override fun getIntrinsicWidth(): Int {
        return (maxRadius * 2).toInt() + 2
    }

    override fun getIntrinsicHeight(): Int {
        return (maxRadius * 2).toInt() + 2
    }

    fun setMaxRadius(maxRadius: Float) {
        this.maxRadius = maxRadius
        mDirtyBounds.bottom = (maxRadius * 2).toInt() + 2
        mDirtyBounds.right = (maxRadius * 2).toInt() + 2
    }

    override fun getDirtyBounds(): Rect {
        return mDirtyBounds
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mDirtyBounds.offsetTo(bounds.left, bounds.top)
    }

    companion object {
        private val TAG = DilatingDotDrawable::class.java.simpleName
    }
}
