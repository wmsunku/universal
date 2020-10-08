package com.wms.base.sdk.system

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.wms.base.api.AppAPI

/*****
 * 管理应用小红点消息数
 */

fun setBadgeCount(context: Context, num: Int) {
    var count = num
    count = if (count <= 0) {
        0
    } else {
        Math.max(0, Math.min(count, 99))
    }
    when {
        Build.MANUFACTURER.toLowerCase().contains("xiaomi") -> sendToXiaoMi(context, count)
        Build.MANUFACTURER.toLowerCase().contains("sony") -> sendToSony(context, count)
        Build.MANUFACTURER.toLowerCase().contains("samsung") -> sendToSamsumg(context, count)
        Build.MANUFACTURER.toLowerCase().contains("huawei") -> sendToHuawei(context, count)
    }
}

private fun sendToHuawei(context: Context, count: Int) {
    val launcherClassName = AppAPI.getLauncherClassName(context) ?: return
    val localBundle = Bundle()
    localBundle.putString("package", AppAPI.getPackageName(context))
    localBundle.putString("class", launcherClassName)
    localBundle.putInt("badgenumber", count)
    context.contentResolver.call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, localBundle)
}


private fun sendToXiaoMi(context: Context, count: Int) {
    try {
        val notification = Notification.Builder(context).build()
        val field = notification::class.java.getDeclaredField("extraNotification")
        val extraNotification = field.get(notification)
        val method = extraNotification.javaClass.getDeclaredMethod("setMessageCount", Int::class.javaPrimitiveType)
        method.invoke(extraNotification, count)
    } catch (e: Exception) {
        e.printStackTrace()
        val localIntent = Intent(
                "android.intent.action.APPLICATION_MESSAGE_UPDATE")
        localIntent.putExtra(
                "android.intent.extra.update_application_component_name",
                context.packageName + "/" + AppAPI.getLauncherClassName(context))
        localIntent.putExtra(
                "android.intent.extra.update_application_message_text", (if (count == 0) "" else count).toString())
        context.sendBroadcast(localIntent)
    }

}


private fun sendToSony(context: Context, count: Int) {
    val launcherClassName = AppAPI.getLauncherClassName(context) ?: return
    var isShow = true
    if (count == 0) {
        isShow = false
    }
    val localIntent = Intent()
    localIntent.action = "com.sonyericsson.home.action.UPDATE_BADGE"
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow)//是否显示
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName)//启动页
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", count.toString())//数字
    localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.packageName)//包名
    context.sendBroadcast(localIntent)
}


private fun sendToSamsumg(context: Context, count: Int) {
    val launcherClassName = AppAPI.getLauncherClassName(context) ?: return
    val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
    intent.putExtra("badge_count", count)
    intent.putExtra("badge_count_package_name", context.packageName)
    intent.putExtra("badge_count_class_name", launcherClassName)
    context.sendBroadcast(intent)
}



