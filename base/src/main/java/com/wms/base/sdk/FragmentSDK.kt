package com.wms.base.sdk

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun addFragment(manager: FragmentManager, fragmentId: Int,  fragment: Fragment) {
    val transaction = manager.beginTransaction()
    transaction.add(fragmentId, fragment)
    transaction.commit()
}

fun showFragment(manager: FragmentManager, fragment: Fragment) {
    val transaction = manager.beginTransaction()
    transaction.show(fragment)
    transaction.commit()
}

fun hideFragment(manager: FragmentManager, fragment: Fragment) {
    val transaction = manager.beginTransaction()
    transaction.hide(fragment)
    transaction.commit()
}

fun replaceFragment(manager: FragmentManager, fragmentId: Int,  fragment: Fragment) {
    val transaction = manager.beginTransaction()
    transaction.replace(fragmentId, fragment)
    transaction.commit()
}

