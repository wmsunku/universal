package com.wms.app.universal.main.ui.activity

import android.os.Bundle
import android.view.View
import com.wms.app.universal.R
import com.wms.app.universal.main.model.SplashViewHolder
import com.wms.app.universal.main.model.contact.SplashModel
import com.wms.app.universal.main.presenter.SplashPresenter
import com.wms.base.api.AppAPI
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.createView
import com.wms.base.sdk.system.fullScreen

class SplashActivity: BaseActivity(), SplashModel.Model {
    private var presenter: SplashPresenter? = null
    private var holder: SplashViewHolder? = null

    override fun beforeCreate(savedInstanceState: Bundle?) {
        fullScreen()
        if(!AppAPI.isFristActivity(this)) {
            jumpMain()
            return
        }
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_splash)
    }

    override fun initView() {
        holder = SplashViewHolder(getContentView())
        presenter = SplashPresenter(this, this)
        presenter!!.startCountTime()
    }

    fun skip(v: View) {
        presenter!!.completeTimer()
    }

    override fun setTimer(time: String) {
        holder!!.tvTimer.text = time
    }

    override fun jumpMain() {
        HomeActivity.start(this)
        finish()
    }


}