package com.wms.app.universal.main.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.wms.app.universal.R
import com.wms.app.universal.main.model.HomeViewHolder
import com.wms.app.universal.main.presenter.HomePresenter
import com.wms.app.universal.sdk.getIndex
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.*
import com.wms.base.sdk.system.setBadgeCount
import com.wms.media.music.views.startMusicService
import com.wms.router.RouterFactory

class HomeActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var holder: HomeViewHolder? = null
    private var presenter: HomePresenter? = null

    companion object {
        fun start(ac: Activity) {
            val intent = Intent(ac, HomeActivity::class.java)
            ac.startActivity(intent)
        }
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {
        transparencyBar(true)
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_home)
    }

    override fun initView() {
        startMusicService()
        holder = HomeViewHolder(getContentView())
        presenter = HomePresenter(this)
        presenter!!.init()
        holder!!.bottomMenu.setOnNavigationItemSelectedListener(this)

        setBadgeCount(this, 10)
        holder!!.init(this)
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        val index = getIndex(menu.itemId)
        presenter!!.selectItem(index)
        return true
    }


}