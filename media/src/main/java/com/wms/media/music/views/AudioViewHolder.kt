package com.wms.media.music.views

import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.wms.base.common.BaseViewHolder
import com.wms.media.R

class AudioViewHolder(view: View): BaseViewHolder(view) {

    val tvCurrentTime = getView<TextView>(R.id.tvCurrentTime)!!

    val sbProgress = getView<SeekBar>(R.id.sbProgress)!!

    val tvTotalTime = getView<TextView>(R.id.tvTotalTime)!!

    val ivMode = getView<ImageView>(R.id.ivMode)!!

    val ivPrev = getView<ImageView>(R.id.ivPrev)!!

    val ivPlay = getView<ImageView>(R.id.ivPlay)!!

    val ivNext = getView<ImageView>(R.id.ivNext)!!

}