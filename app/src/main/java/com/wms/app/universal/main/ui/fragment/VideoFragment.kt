package com.wms.app.universal.main.ui.fragment

import com.wms.app.universal.R
import com.wms.base.common.BaseFragment
import com.wms.media.video.ui.activity.MovieActivity
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_video
    }

    override fun initView() {
        video.setOnClickListener({
            MovieActivity.start(activity!!)
        })
    }
}