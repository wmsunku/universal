package com.wms.app.universal.main.model

import android.app.Activity
import android.support.design.widget.BottomNavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.View
import com.wms.app.universal.R
import com.wms.base.common.BaseViewHolder
import com.wms.base.sdk.initMenuWithDrawer
import com.wms.base.sdk.setMenuLayout
import com.wms.base.sdk.system.bindToolBar
import com.wms.base.sdk.system.setBadgeCount

class HomeViewHolder(view: View): BaseViewHolder(view) {

    val toolBar = getView<Toolbar>(R.id.toolBar)!!
    val drawer = getView<DrawerLayout>(R.id.drawer)!!
    val bottomMenu = getView<BottomNavigationView>(R.id.bottomMenu)!!

    fun init(ac: Activity) {
        setBadgeCount(ac, 8)
        initMenuWithDrawer(ac, drawer, toolBar)
        bindToolBar(toolBar)
        setMenuLayout(toolBar, R.menu.home_menu)
        setTitle("首页")
    }

    fun setTitle(title: String) {
        initToolBarTitle(toolBar, false, title)
    }
}