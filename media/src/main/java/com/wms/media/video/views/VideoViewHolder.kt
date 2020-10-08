package com.wms.media.video.views

import android.support.v7.widget.Toolbar
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.wms.base.common.BaseViewHolder
import com.wms.base.dialog.DilatingDotsProgressBar
import com.wms.base.sdk.AcManager
import com.wms.base.sdk.setBackDefaultListener
import com.wms.base.sdk.system.bindToolBar
import com.wms.media.R

class VideoViewHolder(view: View): BaseViewHolder(view) {

    val toolBar = getView<Toolbar>(R.id.toolBar)!!

    val viewPlayer = getView<ViewGroup>(R.id.viewPlayer)!!

    val tvCurrentTime = getView<TextView>(R.id.tvCurrentTime)!!

    val sbProgress = getView<SeekBar>(R.id.sbProgress)!!

    val tvTotalTime = getView<TextView>(R.id.tvTotalTime)!!

    val ivEnter = getView<ImageView>(R.id.ivEnter)!!

    val ivPlay = getView<ImageView>(R.id.ivPlay)!!

    val viewLoading = getView<View>(R.id.viewLoading)!!

    val pbLoading = getView<DilatingDotsProgressBar>(R.id.pbLoading)!!

    val viewControl = getView<View>(R.id.viewControl)!!

    fun init(title: String) {
        initToolBar(toolBar, true, title)
        toolBar.setNavigationOnClickListener({
            AcManager.Instance().getCurrActivity().onBackPressed()
        })
    }

}