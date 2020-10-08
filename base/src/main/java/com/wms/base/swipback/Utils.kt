package com.wms.base.swipback

import android.app.Activity

const val fun1Name = "convertFromTranslucent"
const val fun2Name = "TranslucentConversionListener"
const val fun3Name = "convertToTranslucent"


@Throws(Exception::class)
fun convertActivityFromTranslucent(activity: Activity) {

    val method = Activity::class.java.getDeclaredMethod(fun1Name)
    method.isAccessible = true
    method.invoke(activity)

}

@Throws(Exception::class)
fun convertActivityToTranslucent(activity: Activity) {

    val classes = Activity::class.java.declaredClasses
    var translucentConversionListenerClazz: Class<*>? = null
    for (clazz in classes) {
        if (clazz.simpleName.contains(fun2Name)) {
            translucentConversionListenerClazz = clazz
        }
    }
    val method = Activity::class.java.getDeclaredMethod(fun3Name,
            translucentConversionListenerClazz)
    method.isAccessible = true
    method.invoke(activity, null)


}