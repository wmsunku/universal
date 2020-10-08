package com.wms.base.sdk.system

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.view.Window
import com.readystatesoftware.systembartint.SystemBarTintManager
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.getResources

fun BaseActivity.fullScreen() {
    hideNavigAction()
    hideState()
}

fun BaseActivity.hideNavigAction() {
    val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
    window.decorView.systemUiVisibility = uiOptions
}

fun BaseActivity.showNavigAction() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    window.decorView.fitsSystemWindows = true
}

fun BaseActivity.hideState() {
    window.setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN)
}

fun BaseActivity.transparentState() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.statusBarColor = Color.TRANSPARENT
}

/***
 * 状态栏透明
 */
@TargetApi(19)
fun transparencyBar(activity: Activity) {
    val window = activity.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

/***
 * 设置状态栏颜色
 */
@TargetApi(19)
fun setStatusBarColor(activity: Activity, colorId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = activity.resources.getColor(colorId)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        transparencyBar(activity)
        val tintManager = SystemBarTintManager(activity)
        tintManager.isStatusBarTintEnabled = true
        tintManager.setStatusBarTintResource(colorId)
    }
}


/**
 * 修改状态栏文字颜色，这里小米，魅族区别对待。
 */
@TargetApi(19)
fun setLightStatusBar(activity: Activity, dark: Boolean) {
    when (getLightStatusBarAvailableRomType()) {
        MIUI -> MIUISetStatusBarLightMode(activity, dark)
        FLYME -> setFlymeLightStatusBar(activity, dark)
        ANDROID_NATIVE -> setAndroidNativeLightStatusBar(activity, dark)
    }

}

@TargetApi(23)
private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
    val decor = activity.window.decorView
    if (dark) {
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}

private fun setFlymeLightStatusBar(activity: Activity?, dark: Boolean): Boolean {
    var result = false
    if (activity != null) {
        try {
            val lp = activity.window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            result = true
        } catch (e: Exception) {
        }

    }
    return result
}

fun MIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
    var result = false
    val window = activity.window
    if (window != null) {
        val clazz = window::class.java
        try {
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (dark) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            result = true

            if (isMiUIV7OrAbove()) {
                setAndroidNativeLightStatusBar(activity, dark)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun getStatusBarHeight(): Int {
    var result = 0
    val resId = getResources().getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        result = getResources().getDimensionPixelOffset(resId)
    }
    return result
}

fun bindToolBar(toolBar: Toolbar?) {
    toolBar!!.setPadding(0, getStatusBarHeight(), 0, 0)
}
