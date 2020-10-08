package com.demo.android.web.model

import android.support.v7.widget.Toolbar
import com.wms.base.common.BaseViewHolder
import android.view.View
import android.webkit.WebView
import com.demo.android.web.R
import android.webkit.WebSettings
import com.wms.base.sdk.system.bindToolBar


class WebViewHolder(v: View): BaseViewHolder(v) {

    val toolBar: Toolbar = getView(R.id.toolBar)
    val webView: WebView = getView(R.id.webView)

    fun init(title: String) {
        bindToolBar(toolBar)
        initToolBar(toolBar, false, title)


        val webSettings = webView.settings

        webSettings.javaScriptEnabled = true

//        webSettings.setPluginsEnabled(true)

        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件

        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式


        webView.loadUrl("file:///android_asset/index.html")
    }


}