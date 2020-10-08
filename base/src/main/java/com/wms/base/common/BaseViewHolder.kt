package com.wms.base.common

import android.support.v7.widget.Toolbar
import android.view.View
import com.wms.base.R
import com.wms.base.R.id.toolBar
import com.wms.base.sdk.*


open class BaseViewHolder(v: View) {
    protected var view = v

    protected fun <T : View> getView(id: Int): T {
        return view.findViewById(id)
    }

    protected fun initToolBar(toolBar: Toolbar, isDark: Boolean, title: String) {
        setTitle(toolBar, title)
        showToolBar(toolBar, true)
        setBackDefaultListener(AcManager.Instance().getCurrActivity(), toolBar)

        if (isDark) {
            setTitleColor(toolBar, R.color.white)
            setBackIcon(toolBar, R.mipmap.ic_back_white)
        } else {
            setTitleColor(toolBar, R.color.c_333333)
            setBackIcon(toolBar, R.mipmap.ic_back_black)
        }
    }

    protected fun initToolBarTitle(toolBar: Toolbar, isDark: Boolean, title: String) {
        setTitle(toolBar, title)
        showToolBar(toolBar, true)
        if (isDark) {
            setTitleColor(toolBar, R.color.white)
        } else {
            setTitleColor(toolBar, R.color.c_333333)
        }
    }


}