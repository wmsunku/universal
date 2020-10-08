package com.wms.app.universal.main.presenter

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.wms.app.universal.main.model.contact.SplashModel
import com.wms.app.universal.main.ui.activity.HomeActivity
import java.util.*

class SplashPresenter(ac: Activity, model: SplashModel.Model): SplashModel.Presenter {
    private var activity = ac
    private var listener = model
    private var timer: Timer? = null
    private var num = 6
    private var handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            listener.setTimer(msg.what.toString() + "秒")
        }
    }

    override fun startCountTime() {
        if (timer == null) {
            timer = Timer()
        }
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                if (num >= 0) {
                    handler.sendEmptyMessage(num)
                }else {
                    completeTimer()
                }
                num--
            }
        }, 0, 1000)
    }

    override fun completeTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        handler.removeMessages(0)
        listener.jumpMain()
    }


    override fun clear() {

    }
}