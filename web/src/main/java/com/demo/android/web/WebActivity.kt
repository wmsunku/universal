package com.demo.android.web

import android.content.Context
import android.content.Intent
import android.view.View
import com.demo.android.web.model.WebViewHolder
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.createView

class WebActivity: BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_web)
    }

    override fun slidEnable(): Boolean {
        return true
    }

    override fun initView() {
        val holder = WebViewHolder(getContentView())

        holder.init("相册")
    }

}