package com.wms.app.universal.main.ui.fragment

import com.wms.app.universal.R
import com.wms.base.common.BaseFragment
import com.wms.media.music.ui.activity.MusicActivity
import kotlinx.android.synthetic.main.fragment_music.*

class MusicFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_music
    }

    override fun initView() {
        music.setOnClickListener({
            MusicActivity.start(activity!!)
        })
    }
}