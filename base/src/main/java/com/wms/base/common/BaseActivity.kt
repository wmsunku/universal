package com.wms.base.common

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.wms.base.sdk.*

abstract class BaseActivity : AppCompatActivity() {
    private var layoutView:View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initBeforeCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        initAfterCreate()
    }

    open fun beforeCreate(savedInstanceState: Bundle?) {
        transparencyBar(true)
    }

    open fun slidEnable(): Boolean {
        return false
    }

    private fun initLayout(): View {
        layoutView = getLayout()
        return layoutView!!
    }

    fun getContentView() :View {
        return layoutView!!
    }

    abstract fun getLayout(): View

    abstract fun initView()

    open fun initData() {

    }

    override fun onResume() {
        super.onResume()
        AcManager.Instance().setCurrActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handlerPhotoResult(this, requestCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlerPermissionsResult(requestCode, grantResults, permissions)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        handlerPostCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(layoutView!=null) {
            layoutView = null
        }
        freeSwipe()
    }
}