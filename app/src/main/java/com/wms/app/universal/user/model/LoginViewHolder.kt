package com.wms.app.universal.user.model

import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.wms.app.universal.R
import com.wms.base.common.BaseViewHolder
import com.wms.base.sdk.system.bindToolBar

class LoginViewHolder(view: View): BaseViewHolder(view) {
    val toolBar = getView<Toolbar>(R.id.toolBar)!!
    val email = getView<AutoCompleteTextView>(R.id.email)!!
    val password = getView<TextView>(R.id.password)!!
    val login = getView<View>(R.id.login)!!


    fun init(title: String) {
        bindToolBar(toolBar)
        initToolBar(toolBar, false, title)
    }
}