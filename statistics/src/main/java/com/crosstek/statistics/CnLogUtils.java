package com.crosstek.statistics;

import android.util.Log;

/**
 * @author yemint
 * Log统一管理类
 */
public class CnLogUtils {

    private CnLogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("CnLogUtils cannot be instantiated");
    }

    public static boolean isDebug = AppConstants.isDebug;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "CnLogUtils:";

//    // 下面四个是默认tag的函数
//    public static void i(String msg) {
//        if (isDebug)
//            Log.i(TAG, msg);
//    }
//
//    public static void d(String msg) {
//        if (isDebug)
//            Log.d(TAG, msg);
//    }
//
//    public static void e(String msg) {
//        if (isDebug)
//            Log.e(TAG, msg);
//    }
//
//    public static void v(String msg) {
//        if (isDebug)
//            Log.v(TAG, msg);
//    }

    static void i(String methodName, String msg) {
        if (isDebug)
            Log.i(TAG, canvs(methodName, msg));
    }

    static void d(String methodName, String msg) {
        if (isDebug)
            Log.d(TAG, canvs(methodName, msg));
    }

    static void e(String methodName, String msg) {
        if (isDebug)
            Log.e(TAG, canvs(methodName, msg));
    }

    private static String canvs(String name, String msg) {
        String b = "start\n|————————————————————————————————————————————————————————————————————————————————————————————————————|\n|";
        String c = "\n|————————————————————————————————————————————————————————————————————————————————————————————————————|\n|";
        String d = "\n|————————————————————————————————————————————————————————————————————————————————————————————————————|\nend";
        String last = b + "--------->" + name + c + "--------->" + msg + d;
        return last;
    }


}
