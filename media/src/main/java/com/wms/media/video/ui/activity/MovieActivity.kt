package com.wms.media.video.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.createView
import com.wms.base.sdk.system.hideNavigAction
import com.wms.base.sdk.system.hideState
import com.wms.base.sdk.system.showNavigAction
import com.wms.base.sdk.transparencyBar
import com.wms.media.R
import com.wms.media.video.model.MovieViewHolder
import com.wms.media.video.views.ScreenConfig
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MovieActivity : BaseActivity() {

    private var holder: MovieViewHolder? = null
    private var orientation = 1

    companion object {
        fun start(ac: Activity) {
            val intent = Intent(ac, MovieActivity::class.java)
            ac.startActivity(intent)
        }
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        transparencyBar(false)
        hideState()
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_video)
    }

    override fun slidEnable(): Boolean {
        return true
    }

    override fun initView() {
        holder = MovieViewHolder(getContentView())
        holder!!.getHolder().init("视频")
        holder!!.bindView()

        val path = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-03_13-02-41.mp4"

//        val path = "https://ketang.homestyler.com/cn/wp-content/uploads/2018/07/定制橱柜整体设计流程.mp4"
        holder!!.player.loadUrl(path)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeScreenOrientation(screen: ScreenConfig) {
        requestedOrientation = if (screen.isLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        orientation = if(screen.isLandscape){
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        if(screen.isLandscape) {
            transparencyBar(false)
            hideNavigAction()
        }else {
            transparencyBar(false)
            showNavigAction()
        }
    }

    override fun onBackPressed() {
        if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            holder!!.player.exitFull()
        }else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        holder!!.player.onResume()
    }

    override fun onPause() {
        super.onPause()
        holder!!.player.onPause()
    }

    override fun onStop() {
        super.onStop()
        holder!!.player.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        holder!!.player.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}