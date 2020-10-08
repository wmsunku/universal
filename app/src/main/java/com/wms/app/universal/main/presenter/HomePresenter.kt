package com.wms.app.universal.main.presenter

import android.support.v4.app.Fragment
import com.wms.app.universal.main.ui.activity.HomeActivity
import com.wms.app.universal.main.ui.fragment.BookFragment
import com.wms.app.universal.main.ui.fragment.HomeFragment
import com.wms.app.universal.main.ui.fragment.MusicFragment
import com.wms.app.universal.main.ui.fragment.VideoFragment
import com.wms.app.universal.sdk.addFragment
import com.wms.app.universal.sdk.hideFragment
import com.wms.app.universal.sdk.showFragment

class HomePresenter(home: HomeActivity) {
    private val ac: HomeActivity = home
    private var fragmentMap = HashMap<Int, Fragment>()


    fun init() {
        fragmentMap[0] = HomeFragment()
        fragmentMap[1] = BookFragment()
        fragmentMap[2] = VideoFragment()
        fragmentMap[3] = MusicFragment()

        for(f: Fragment in fragmentMap.values) {
            addFragment(ac, f)
        }
        selectItem(0)
    }

    fun selectItem(index: Int) {
        for(key in fragmentMap.keys) {
            if(key == index) {
                showFragment(ac, fragmentMap[key]!!)
            }else {
                hideFragment(ac, fragmentMap[key]!!)
            }
        }
    }

    fun cleanData() {
        fragmentMap.clear()
    }

}