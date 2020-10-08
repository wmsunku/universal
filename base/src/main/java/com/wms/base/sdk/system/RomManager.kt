package com.wms.base.sdk.system

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.util.*

const val MIUI = 1
const val FLYME = 2
const val ANDROID_NATIVE = 3
const val NA = 4
private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"


fun getLightStatusBarAvailableRomType(): Int {
    if(isMiUIV7OrAbove()) {
        return ANDROID_NATIVE
    }
    if(isMiUIV6OrAbove()) {
        return MIUI
    }
    if (isFlymeV4OrAbove()) {
        return FLYME
    }
    if (isAndroidMOrAbove()) {
        return ANDROID_NATIVE
    }
    return NA
}

private fun isFlymeV4OrAbove(): Boolean {
    val displayId = Build.DISPLAY
    if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
        val displayIdArray = displayId.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (temp in displayIdArray) {
            //版本号4以上，形如4.x.
            if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*".toRegex())) {
                return true
            }
        }
    }
    return false
}

//Android Api 23以上
private fun isAndroidMOrAbove(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
}


private fun isMiUIV6OrAbove(): Boolean {
    return isMiUIVrAbove(4)
}

fun isMiUIV7OrAbove(): Boolean {
   return isMiUIVrAbove(5)
}

private fun isMiUIVrAbove(codeNum: Int): Boolean {
    return try {
        val properties = Properties()
        properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
        val uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null)
        if (uiCode != null) {
            val code = Integer.parseInt(uiCode!!)
            code >= codeNum
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}




