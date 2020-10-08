package com.wms.media.sdk

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.*

class CountManager {
    private var timer: Timer? = null
    private var countListener: OnCountTimeListener? = null

    companion object {
        private object VH {
            val instance = CountManager()
        }

        @Synchronized
        fun newInstance(): CountManager {
            return VH.instance
        }
    }

    private var handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (countListener != null) {
                countListener!!.countTime()
            }
        }
    }

    fun startCount(delay: Long, period: Long, listener: OnCountTimeListener) {
        stopCount()
        countListener = listener
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(0x123)

            }
        }, delay, period)
    }

    fun stopCount() {
        handler.removeMessages(0)
        if (timer != null) {
            timer!!.cancel()
        }
    }

    fun onDestroy() {
        stopCount()
        timer = null
    }

}