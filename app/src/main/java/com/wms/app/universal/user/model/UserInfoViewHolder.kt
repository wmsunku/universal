package com.wms.app.universal.user.model

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.View
import com.wms.app.universal.R
import com.wms.base.common.BaseViewHolder
import com.wms.base.sdk.setMenuLayout

class UserInfoViewHolder(view: View): BaseViewHolder(view) {
    val toolBar = view.findViewById<Toolbar>(R.id.toolBar)!!
    val fab = view.findViewById<FloatingActionButton>(R.id.fab)!!
    val ivHead = view.findViewById<FloatingActionButton>(R.id.ivHead)!!

    fun init(title: String) {
        initToolBar(toolBar, true, title)
        setMenuLayout(toolBar, R.menu.user_info)
    }
}