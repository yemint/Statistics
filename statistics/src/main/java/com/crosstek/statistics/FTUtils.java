package com.crosstek.statistics;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;

import java.util.Map;

public class FTUtils {
    public static long START_TIME = 0L;
    public static long END_TIME = -1;
    public static Map<String, Object> map;

    protected static final String TAG = "FTUtils";

    private FTUtils() {
        throw new UnsupportedOperationException(" FTUtils cannot be instantiated");
    }

    /**
     * 初始化SDK
     *
     * @param context
     */
    public static void init(Context context) {
        CrashHandlerUtil.getInstance().init(context);

        START_TIME = System.currentTimeMillis();

        Location location = CnLocationUtils.getBestLocation(context);

        map = new ArrayMap<>();
        map.put("packageName", CnDeviceUtils.getAppPackageName(context));
        map.put("versionCode", CnDeviceUtils.getVersionCode(context));
        map.put("startTime", START_TIME);
        map.put("deviceName", CnDeviceUtils.getPhoneModel());
        map.put("systemCode", "Android:" + CnDeviceUtils.getBuildVersion());
        map.put("ip", CnDeviceUtils.getLocalIpAddress());
        map.put("deviceId", CnDeviceUtils.getDeviceId(context));

        if (location != null) {
            map.put("longitude", location.getLongitude());
            map.put("latitude", location.getLatitude());
            map.put("area", CnLocationUtils.convertAddress(context, location));
        } else {
            map.put("longitude", 0);
            map.put("latitude", 0);
        }
        CnLogUtils.i(TAG, "初始化成功...");
        if ((boolean) CnSPUtils.get(context, AppConstants.SP_VALUE, false)) {
            FTUtils.runError();
            CnSPUtils.remove(context, AppConstants.SP_VALUE);
        }
    }

    /**
     * App正常启动
     */
    public static void runNormal() {
        map.put("runType", 1);

        CnHttpUtils.getInstance().post(AppConstants.IP_URL, new ArrayMap<String, Object>(), new CnHttpUtils.CallBacks() {
            @Override
            public void onSuccess(Object o) {

                String str = (String) o;

                String query = str.substring(str.indexOf("query\":\"") + 8, str.indexOf("\",\"region"));
                map.put("ip", query);
                CnLogUtils.d("ip地址拿下", query);
                upData();
            }

            @Override
            public void onFailure(Exception e) {
                CnLogUtils.d("ip地址未获取到", e.getMessage());

                upData();

            }
        });

    }

    /**
     * 监测到App异常
     */
    public static void runError() {

        map.put("runType", 0);
        CnLogUtils.d("有问题", "");
        upData();
    }

    /**
     * 上传统计数据
     */
    public static void upData() {
        END_TIME = System.currentTimeMillis();
        long time = END_TIME - START_TIME;

//        String data = CnDeviceUtils.formatDuration(time);//有格式的时间

        map.put("startTime", time);

//        CnLogUtils.i(TAG + "-upData", map.toString());


        CnHttpUtils.getInstance().post(AppConstants.UPDATA_URL, map, new CnHttpUtils.CallBacks() {
            @Override
            public void onSuccess(Object o) {
                CnLogUtils.d("上传统计结果", o.toString());
            }

            @Override
            public void onFailure(Exception e) {
                CnLogUtils.d("上传统计结果", e.getMessage());

            }
        });
    }

    /**
     * 是否Debug
     *
     * @param isDebug
     */
    public static void isDebug(boolean isDebug) {
        AppConstants.isDebug = isDebug;
    }

    /**
     * 检查权限
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Activity activity, String permissions, int requestCode) {
        boolean isHasPer = true;

        if (ActivityCompat.checkSelfPermission(activity, permissions) != 0) {
            isHasPer = false;
        }

        if (!isHasPer) {
            ActivityCompat.requestPermissions(activity, new String[]{permissions}, requestCode);
        }

        return isHasPer;
    }

}
