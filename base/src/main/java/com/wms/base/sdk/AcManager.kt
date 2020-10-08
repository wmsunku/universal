package com.wms.base.sdk

import android.app.Activity
import com.wms.base.api.DataAPI
import java.util.*

class AcManager private constructor() {

    private var currActivity: Activity? = null
    private val activityStack = Stack<Activity>()

    companion object {
        private object VH {
            val instance = AcManager()
        }

        fun Instance(): AcManager {
            return VH.instance
        }
    }

    fun addActivity(ac: Activity) {
        currActivity = ac
        activityStack.add(ac)
    }

    fun finishCurrActivity(ac: Activity, isMain: Boolean) {
        if(isMain) {
            ac.finish()
            return
        }
        DataAPI.removeElement(activityStack, ac)
        currActivity = DataAPI.getTopElement(activityStack)
        ac.finish()
    }

    fun setCurrActivity(ac: Activity) {
        currActivity = ac
    }

    fun getCurrActivity(): Activity {
        return currActivity!!
    }

    fun cleanStack() {
        for(ac in activityStack) {
            ac.finish()
        }
        currActivity = null
        activityStack.clear()
    }

}