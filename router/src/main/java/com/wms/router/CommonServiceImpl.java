package com.wms.router;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.wms.router.contact.CommonService;

@Route(path = RouterConfig.COMMON_GROUP_ACTION)
public class CommonServiceImpl implements IProvider {
    private Context mContext;


    public void showTost(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
