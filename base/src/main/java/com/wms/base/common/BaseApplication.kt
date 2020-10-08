package com.wms.base.common

import android.app.Application
import android.support.multidex.MultiDexApplication

open class BaseApplication: MultiDexApplication() {

    companion object {
        object VH {
            val instance = BaseApplication()
        }

        fun newInstance(): BaseApplication {
            return VH.instance
        }

        var mApp: Application? = null

    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
    }

    fun getApplication(): Application {
        return mApp!!
    }

}