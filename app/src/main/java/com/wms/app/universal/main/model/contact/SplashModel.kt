package com.wms.app.universal.main.model.contact

class SplashModel {

    interface Model {

        fun setTimer(time: String)

        fun jumpMain()

    }

    interface Presenter {

        fun startCountTime()

        fun completeTimer()

        fun clear()
    }


}