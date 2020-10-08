package com.wms.media.video.views

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getVideoTaskNum(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
    val date = sdf.format(Date())
    val sb = StringBuilder()
    val num = Random().nextInt(100)
    sb.append("video").append(date).append(num).append("num")
    return sb.toString()
}