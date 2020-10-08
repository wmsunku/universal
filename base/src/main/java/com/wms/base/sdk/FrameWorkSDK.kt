package com.wms.base.sdk

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.content.res.Resources
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.wms.base.common.BaseApplication


fun getScreenWidth(): Int {
    return getResources().displayMetrics.widthPixels
}


fun getScreenHeight(): Int {
    return getResources().displayMetrics.heightPixels
}


fun isLandscape(context: Context): Boolean {
    return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun isPortrait(context: Context): Boolean {
    return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}


fun dpToPx(dp: Float): Float {
    return dp * getResources().displayMetrics.density
}

fun dpToPxInt(dp: Float): Int {
    return (dpToPx(dp) + 0.5f).toInt()
}


fun pxToDp(px: Float): Float {
    return px / getResources().displayMetrics.density
}

fun pxToDpInt(px: Float): Int {
    return (pxToDp(px) + 0.5f).toInt()
}


fun pxToSp(pxValue: Float): Float {
    return pxValue / getResources().displayMetrics.scaledDensity
}


fun spToPx(spValue: Float): Float {
    return spValue * getResources().displayMetrics.scaledDensity
}

fun getResources(): Resources {
    return getApplication().resources
}

fun getApplication(): Application {
//    val common = RouterFactory.Instance().commonService
//    return common!!.application
    val app = BaseApplication.newInstance().getApplication()
    Log.e("ss", ""+(app==null))
    return app
}

fun getScreenBrightness(): Int {
    return (getScreenBrightnessInt255() / 255.0f * 100).toInt()
}


fun getScreenBrightnessInt255(): Int {
    var screenBrightness = 255
    try {
        screenBrightness = Settings.System.getInt(getApplication().contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return screenBrightness
}

fun showTost(text: String) {
//    RouterFactory.Instance().commonService.showTost(text)
}

fun showTost(res: Int) {
//    RouterFactory.Instance().commonService.showTost(res)

}

fun createView(context: Context, layout: Int): View {
    return View.inflate(context, layout, null)
}

fun getColor(color: Int): Int {
    return ActivityCompat.getColor(getApplication(), color)
}

fun sendBroadcast(intent: Intent) {
    val app = getApplication()
    app.sendBroadcast(intent)
}

fun View.registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
    context.registerReceiver(receiver, filter)
}

fun View.unregisterReceiver(receiver: BroadcastReceiver) {
    context.unregisterReceiver(receiver)
}
