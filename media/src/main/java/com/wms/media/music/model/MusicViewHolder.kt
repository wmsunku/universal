package com.wms.media.music.model

import android.support.v7.widget.Toolbar
import com.wms.base.common.BaseViewHolder
import android.view.View
import com.wms.base.sdk.system.bindToolBar
import com.wms.media.R
import com.wms.media.music.views.AlbumCoverView
import com.wms.media.music.views.AudioPlayer


class MusicViewHolder(view: View): BaseViewHolder(view) {
    val toolBar = getView<Toolbar>(R.id.toolBar)!!
    val albumCover = getView<AlbumCoverView>(R.id.albumCover)!!
    val player = getView<AudioPlayer>(R.id.player)!!

    fun init(title: String) {
        bindToolBar(toolBar)
        initToolBar(toolBar, true, title)
    }

    fun bindAlbum(isPlaying: Boolean) {
        player.bindAlbum(albumCover, isPlaying)
    }
}