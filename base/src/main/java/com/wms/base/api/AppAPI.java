package com.wms.base.api;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Process;

final public class AppAPI {

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageName(Context context) {
        PackageInfo info = getPackageInfo(context);
        if(info != null) {
            return info.packageName;
        }
        return null;
    }

    public static String getAppProcessName(Context context) {
        try {
            int pid = Process.myPid();
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
                if (info.pid == pid) {
                    return info.processName;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getApplicationInfo(packageName, 0).loadLogo(pm);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppVersion(Context context, String packageName) {
        PackageInfo info = getPackageInfo(context);
        if(info != null) {
            return info.versionName;
        }
        return null;
    }

    public static int getAppVersionNum(Context context, String packageName) {
        PackageInfo info = getPackageInfo(context);
        if(info != null) {
            return info.versionCode;
        }
        return 1;
    }

    public static String getAppName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return info.loadLabel(pm).toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }

    public static void exitApp() {
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    public static String getFirstActivityName(Activity activity) {
        try {
            ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
            return info.topActivity.getClassName();
        }catch (Exception e) {
            return null;
        }
    }

    public static String getCurrActivityName(Activity activity) {
        return activity.getClass().getName();
    }

    public static boolean isFristActivity(Activity activity) {
        return DataAPI.isEqual(getCurrActivityName(activity), getFirstActivityName(activity));
    }


}
