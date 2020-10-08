package com.wms.app.universal.sdk

import android.support.v4.app.Fragment
import com.wms.app.universal.R
import com.wms.app.universal.main.ui.activity.HomeActivity
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.addFragment
import com.wms.base.sdk.showFragment
import com.wms.base.sdk.hideFragment

fun addFragment(ac: BaseActivity, fragment: Fragment) {
    addFragment(ac.supportFragmentManager, R.id.fragment_main, fragment)
}

fun showFragment(ac: BaseActivity, fragment: Fragment) {
    showFragment(ac.supportFragmentManager, fragment)
}

fun hideFragment(ac: BaseActivity, fragment: Fragment) {
    hideFragment(ac.supportFragmentManager, fragment)
}

fun HomeActivity.getIndex(id: Int): Int {
    return when(id) {
        R.id.tab_one -> 0
        R.id.tab_two -> 1
        R.id.tab_three -> 2
        R.id.tab_four -> 3
        else -> 0
    }
}