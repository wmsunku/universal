package com.wms.base.sdk

import android.app.Activity
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.wms.base.R
import com.wms.base.R.id.toolBar
import com.wms.base.common.BaseActivity

fun setTitle(toolBar: Toolbar, title: String) {
    val tvTitle = toolBar.findViewById<TextView>(R.id.tvTitle)
    if(tvTitle != null) {
        tvTitle.text = title
        return
    }
    toolBar.title = title
}

fun setTitle(toolBar: Toolbar, res: Int) {
    val tvTitle = toolBar.findViewById<TextView>(R.id.tvTitle)
    if(tvTitle != null) {
        tvTitle.setText(res)
        return
    }
    toolBar.setTitle(res)
}

fun setTitleColor(toolBar: Toolbar, color: Int) {
    val tvTitle = toolBar.findViewById<TextView>(R.id.tvTitle)
    if(tvTitle != null) {
        tvTitle.setTextColor(getColor(color))
        return
    }
    toolBar.setTitleTextColor(getColor(color))
}

fun setMenuLayout(toolBar: Toolbar, menu: Int) {
    toolBar.inflateMenu(menu)
}

fun setLogo(toolBar: Toolbar, icon: Int) {
    toolBar.setLogo(icon)
}

fun setBackIcon(toolBar: Toolbar, icon: Int) {
    toolBar.setNavigationIcon(icon)
}

fun setBackDefaultListener(ac: Activity, toolBar: Toolbar) {
    toolBar.setNavigationOnClickListener({
        ac.finish()
    })
}

fun setBackListener(toolBar: Toolbar, listener: View.OnClickListener) {
    toolBar.setNavigationOnClickListener(listener)
}

fun setMenuSelectListener(toolBar: Toolbar, listener: Toolbar.OnMenuItemClickListener) {
    toolBar.setOnMenuItemClickListener(listener)
}

fun initMenuWithDrawer(ac: Activity, drawer: DrawerLayout, toolBar: Toolbar) {
    val toggle = ActionBarDrawerToggle(
            ac, drawer, toolBar, R.string.app_name, R.string.app_name)
    drawer.addDrawerListener(toggle)
    toggle.syncState()
}

fun showToolBar(toolBar: Toolbar, enable: Boolean) {
    toolBar.visibility = if(enable) View.VISIBLE else View.INVISIBLE
}







