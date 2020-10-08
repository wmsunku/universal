package com.wms.base.sdk

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.wms.base.api.DataAPI
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.constant.SystemConfig
import com.wms.base.sdk.system.handlerNext
import com.wms.base.sdk.system.setLightStatusBar
import com.wms.base.sdk.system.setStatusBarColor
import com.wms.base.sdk.system.transparencyBar
import com.wms.base.swipback.SwipeBackHelper
import android.view.WindowManager


fun BaseActivity.handlerPermissionsResult(requestCode: Int, grantResults: IntArray?, permissions: Array<out String>) {
    if (requestCode == SystemConfig.PERMISSION_CODE) {
        handlerNext(this, DataAPI.getSize(permissions) == DataAPI.getSize(grantResults) &&
                DataAPI.getInt(grantResults, 0) == PackageManager.PERMISSION_GRANTED)
    }
}

fun BaseActivity.transparencyBar(boolean: Boolean) {
    transparencyBar(this)
    setLightStatusBar(this, boolean)
}

fun BaseActivity.colorStatusBar(boolean: Boolean, colorId: Int) {
    setStatusBarColor(this, colorId)
    setLightStatusBar(this, boolean)
}

fun BaseActivity.createView(layout: Int): View {
    return createView(this, layout)
}

fun BaseActivity.getRootView(): View {
    return window.decorView
}



fun BaseActivity.slidBackEnable(bool: Boolean) {
    SwipeBackHelper.getCurrentPage(this)
            .setSwipeBackEnable(bool)
}

fun BaseActivity.initAfterCreate(){
    initView()
    initData()
    if(slidEnable()) {
        slidBackEnable(true)
    }
}

fun BaseActivity.initBeforeCreate(savedInstanceState: Bundle?) {
    beforeCreate(savedInstanceState)
    AcManager.Instance().addActivity(this)
    if(slidEnable()) {
        SwipeBackHelper.onCreate(this)
    }
}

fun BaseActivity.handlerPostCreate() {
    if(slidEnable()) {
        SwipeBackHelper.onPostCreate(this)
    }
}

fun BaseActivity.freeSwipe() {
    if(slidEnable()) {
        SwipeBackHelper.onDestroy(this)
    }
}

fun BaseActivity.getOrientation(): Int {
    val mgr = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return mgr.defaultDisplay.rotation
}






