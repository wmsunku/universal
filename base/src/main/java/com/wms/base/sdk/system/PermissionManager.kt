package com.wms.base.sdk.system

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.wms.base.api.AppAPI
import com.wms.base.api.DataAPI
import com.wms.base.sdk.constact.OnApplyPermissionsNextListener
import com.wms.base.sdk.constant.SystemConfig

private var applyPermissionsNextListener: OnApplyPermissionsNextListener? = null

fun checkPermission(ac: Activity, permissions: Array<String>?, listenerApplyPermissions: OnApplyPermissionsNextListener?) {
    if (DataAPI.isEmpty(permissions)) {
        return
    }
    applyPermissionsNextListener = listenerApplyPermissions
    if (noPermission(ac, permissions!!)) {
        applyPermissions(ac, permissions)
    }else {
        handlerNext(ac, true)
    }
}

fun noPermission(ac: Activity, permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(ac,
                        permission) != PackageManager.PERMISSION_GRANTED) {
            return true
        }
    }
    return false
}

fun applyPermissions(ac: Activity, permissions: Array<out String>) {
    ActivityCompat.requestPermissions(ac,
            permissions, SystemConfig.PERMISSION_CODE)
}

fun handlerNext(ac: Activity, isAccess: Boolean) {
    if(isAccess) {
        applyPermissionsNextListener!!.nextAction()
    }else {
        jumpSetting(ac)
    }
}

fun jumpSetting(ac: Activity) {
    val localIntent = Intent()
    val packageName = AppAPI.getPackageName(ac)
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (Build.VERSION.SDK_INT >= 9) {
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", packageName, null)
    } else if (Build.VERSION.SDK_INT <= 8) {
        localIntent.action = Intent.ACTION_VIEW
        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
        localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName)
    }
    ac.startActivity(localIntent)

}

