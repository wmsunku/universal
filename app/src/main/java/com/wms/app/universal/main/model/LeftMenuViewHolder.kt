package com.wms.app.universal.main.model

import android.support.design.widget.NavigationView
import android.view.View
import android.widget.ImageView
import com.wms.app.universal.R

class LeftMenuViewHolder(view: View) {
    val leftMenu: NavigationView = view as NavigationView
    val ivHead: ImageView = leftMenu.getHeaderView(0)!!.findViewById(R.id.ivHead)
}