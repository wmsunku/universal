package com.wms.app.universal.user.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.design.widget.Snackbar
import android.view.View
import com.wms.app.universal.R
import com.wms.app.universal.user.model.UserInfoViewHolder
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.constact.OnCallBackPhotoListener
import com.wms.base.sdk.constant.SystemConfig
import com.wms.base.sdk.createView
import com.wms.base.sdk.selectPhoto

class UserInfoActivity: BaseActivity() {
    private var holder: UserInfoViewHolder? = null

    companion object {
        fun start(ac: Activity) {
            val intent = Intent(ac, UserInfoActivity::class.java)
            ac.startActivity(intent)
        }
    }

    override fun slidEnable(): Boolean {
        return true
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_user)
    }

    override fun initView() {
        holder = UserInfoViewHolder(getContentView())
        holder!!.init("")

        holder!!.fab.setOnClickListener({
            Snackbar.make(getContentView(), "Replace with your own action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        })

        holder!!.ivHead.setOnClickListener({
            selectPhoto(SystemConfig.ROLL_CODE, true, object : OnCallBackPhotoListener {
                override fun resultBitmap(bitmap: Bitmap?) {
                    holder!!.ivHead.setImageBitmap(bitmap)
                }
            })
        })

    }

}