package com.wms.media.music.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.createView
import com.wms.media.R
import com.wms.media.music.model.MusicViewHolder
import com.wms.media.music.views.isPlaying
import com.wms.media.music.views.startMusicService

class MusicActivity : BaseActivity() {
    private var holder: MusicViewHolder? = null
    private var isPlaying = false

    companion object {
        fun start(ac: Activity) {
            val intent = Intent(ac, MusicActivity::class.java)
            ac.startActivity(intent)
        }
    }

    override fun slidEnable(): Boolean {
        return true
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_music)
    }

    override fun initView() {
        isPlaying = isPlaying()
        holder = MusicViewHolder(getContentView())
        holder!!.init("音乐")

        if(!isPlaying) {
            Log.e("dddddddd", "加载歌曲")
            val path = "http://qukufile2.qianqian.com/data2/video/ee0df9b95100b183493af0e3fc104235/607962659/607962659.mp4"
            holder!!.player.loadUrl(path)
        }
        holder!!.bindAlbum(isPlaying)
    }

    override fun onResume() {
        super.onResume()
        holder!!.player.onResume()
    }

    override fun onPause() {
        super.onPause()
        holder!!.player.onPause()
    }
}