package com.wms.app.universal.main.ui.fragment

import com.wms.app.universal.R
import com.wms.base.common.BaseFragment
import com.wms.router.RouterFactory
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }


    override fun initView() {

        button.setOnClickListener{
            ff()
        }

    }

    fun ff() {
        val common = RouterFactory.Instance().commonService
        if(common!= null) {
            common.showTost("saveReadBrightnessErr CheckRefs")
        }

    }
}