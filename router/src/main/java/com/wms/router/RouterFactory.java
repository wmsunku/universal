package com.wms.router;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wms.router.contact.CommonService;

public class RouterFactory {

    private RouterFactory(){

    }

    private static class VH {
        static RouterFactory instance = new RouterFactory();
    }

    public static RouterFactory Instance() {
        return VH.instance;
    }

//    public BookReaderService getBookReaderService() {
//        return (BookReaderService) ARouter.getInstance().build(RouterConfig.BOOK_READER_GROUP_ACTION).navigation();
//    }
//
//    public MediaService getMediaService() {
//        return (MediaService) ARouter.getInstance().build(RouterConfig.MEDIA_GROUP_ACTION).navigation();
//    }
//
//    public UserService getUserService() {
//        return (UserService) ARouter.getInstance().build(RouterConfig.USER_GROUP_ACTION).navigation();
//    }

    public CommonServiceImpl getCommonService() {

        return ARouter.getInstance().navigation(CommonServiceImpl.class);

//        return  (CommonService) ARouter.getInstance().build(RouterConfig.COMMON_GROUP_ACTION).navigation();
    }


}
