package com.wms.media.sdk

fun getTime(time: Int): String {
    var temp = time
    val hour = 60*60*1000
    val min = 60*1000
    val sec = 1000

    val hourNum = time/hour
    temp %= hour

    val minNum = temp/min
    temp %= min

    val secNum = temp/sec

    val sb = StringBuilder()
    if(hourNum>0) {
        sb.append(getNum(hourNum)).append(":").append(getNum(minNum)).append(":").append(getNum(secNum))
    }else {
        sb.append(getNum(minNum)).append(":").append(getNum(secNum))
    }
    return sb.toString()

}

fun getNum(num: Int): String {
    if(num>=10) {
        return "$num"
    }
    return "0$num"
}