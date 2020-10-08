package com.wms.base.sdk.system

import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.wms.base.common.BaseActivity


fun BaseActivity.handlerTouch(event: MotionEvent?, inputManager: InputMethodManager?) {
    if (event == null || inputManager == null) {
        return
    }
    if (event.action == android.view.MotionEvent.ACTION_DOWN) {
        if (currentFocus != null && currentFocus.windowToken != null) {
            inputManager.hideSoftInputFromWindow(currentFocus
                    .windowToken, android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun BaseActivity.handlerDisTouch(ev: MotionEvent?, inputManager: InputMethodManager?) {
    if (ev == null || inputManager == null || currentFocus == null) {
        return
    }
    if (ev.action == android.view.MotionEvent.ACTION_DOWN) {
        if (isRange(currentFocus, ev)) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}

private fun isRange(v: View?, ev: MotionEvent): Boolean {
    if (v != null && v is EditText) {
        val leftTop = intArrayOf(0, 0)
        v.getLocationInWindow(leftTop)

        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + v.getHeight()
        val right = left + v.getWidth()

        if (ev.x > left && ev.x < right
                && ev.y > top && ev.y < bottom) {
            return false
        }
    }
    return true
}
