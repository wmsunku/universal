package com.wms.base.sdk.system

import com.wms.base.api.AppAPI
import com.wms.base.sdk.AcManager
import com.wms.base.sdk.constact.OnAppCarshHandlerListener

class CrashManager : Thread.UncaughtExceptionHandler {
    private var mListener: OnAppCarshHandlerListener? = null

    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    companion object {
        private object VH {
            val instance = CrashManager()
        }

        fun newInstance(): CrashManager {
            return VH.instance
        }
    }


    fun init(listener: OnAppCarshHandlerListener) {
        mListener = listener
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(thread: Thread?, throwable: Throwable?) {
        if (mListener == null) {
            if (mDefaultHandler != null) {
                mDefaultHandler!!.uncaughtException(thread, throwable)
            }
            freeApp()
        } else {
            mListener!!.handlderCarsh(throwable!!)
        }
    }

    fun freeApp() {
        AcManager.Instance().cleanStack()
        mListener = null
        mDefaultHandler = null
        System.gc()
        System.runFinalization()
        System.gc()
        AppAPI.exitApp()
    }

}