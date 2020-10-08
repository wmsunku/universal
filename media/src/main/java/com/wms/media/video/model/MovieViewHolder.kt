package com.wms.media.video.model

import android.view.View
import android.view.ViewGroup
import com.wms.base.common.BaseViewHolder
import com.wms.media.R
import com.wms.media.video.views.VideoPlayer
import com.wms.media.video.views.VideoViewHolder

class MovieViewHolder(view: View): BaseViewHolder(view) {

    val player = getView<VideoPlayer>(R.id.player)!!

    val viewPlay = getView<ViewGroup>(R.id.viewPlay)!!

    val viewBody = getView<ViewGroup>(R.id.viewBody)!!

    fun bindView() {
        player.bindView(viewPlay, viewBody)

    }

    fun getHolder(): VideoViewHolder {
        return player.getViewHolder()
    }

}