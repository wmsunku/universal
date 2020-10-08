package com.wms.app.universal.main.ui.fragment

import android.graphics.Bitmap
import android.support.design.widget.NavigationView
import android.view.MenuItem
import com.demo.android.web.WebActivity
import com.wms.app.universal.R
import com.wms.app.universal.main.model.LeftMenuViewHolder
import com.wms.app.universal.user.ui.activity.LoginActivity
import com.wms.app.universal.user.ui.activity.UserInfoActivity
import com.wms.base.common.BaseFragment
import com.wms.base.sdk.constact.OnCallBackPhotoListener
import com.wms.base.sdk.constant.SystemConfig
import com.wms.base.sdk.selectPhoto

class LeftMenuFragment: BaseFragment(), NavigationView.OnNavigationItemSelectedListener, OnCallBackPhotoListener {
    private var holder: LeftMenuViewHolder? = null


    override fun getLayout(): Int {
        return R.layout.fragment_left_menu
    }

    override fun initView() {
        holder = LeftMenuViewHolder(getContentView())
        holder!!.leftMenu.setNavigationItemSelectedListener(this)
        holder!!.ivHead.setOnClickListener({
//            selectPhoto(SystemConfig.CAMERA_CODE, true, this)
            UserInfoActivity.start(activity!!)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_camera -> 1
            R.id.nav_gallery -> 2
            R.id.nav_slideshow -> 1
            R.id.nav_manage -> 1
            R.id.nav_share -> WebActivity.start(activity!!)
            R.id.nav_send -> LoginActivity.start(activity!!)
        }
        return true
    }

    override fun resultBitmap(bitmap: Bitmap?) {

    }


}