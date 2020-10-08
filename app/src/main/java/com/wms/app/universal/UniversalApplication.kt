package com.wms.app.universal

import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.SharedPreferences
import com.alibaba.android.arouter.launcher.ARouter
import com.wms.base.api.SpAPI
import com.wms.base.common.BaseApplication
import com.wms.base.sdk.system.CrashManager
import com.wms.base.sdk.constact.OnAppCarshHandlerListener

class UniversalApplication : BaseApplication(), OnAppCarshHandlerListener {

    companion object {
        object VH {
            val instance = UniversalApplication()
        }

        fun Instance(): UniversalApplication {
            return VH.instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        initConfig()
    }

    private fun initConfig() {
        ARouter.openLog()
        ARouter.openDebug()

        ARouter.init(this)
        SpAPI.init(this, packageName + "_preference")
        CrashManager.newInstance().init(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        CrashManager.newInstance().freeApp()
    }

    override fun handlderCarsh(throwable: Throwable) {

    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }


}