package com.scrat.app.core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * Created by yixuanxuan on 16/4/12.
 */
public class Utils {
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static DateFormat sShortDateFormat =
            java.text.DateFormat.getDateInstance(DateFormat.SHORT);

    public static String getUpdateString(long timestamp) {
        if (timestamp == 0L)
            return "未知";

        long now = System.currentTimeMillis();
        long distance = now - timestamp;
        distance /= 1000L;
        if(distance < 60L)
            return "刚刚";

        if(distance >= 60L && distance < 3600L)
            return (distance / 60L) + "分钟前";

        if(distance >= 3600L && distance < 24L * 3600L)
            return (distance / 3600L) + "小时前";

        return sShortDateFormat.format(timestamp);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean phoneNumValid(String mobiles) {
        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    public static void saveImage(Bitmap photo, String path) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(path, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(bos);
        }
    }

    public static boolean isX86CPU() {
        return "x86".equals(android.os.Build.CPU_ABI);
    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getAndroidID(Context context) {
        try {
            ContentResolver cr = context.getContentResolver();
            return Settings.Secure.getString(cr, Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }
}
