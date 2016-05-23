package com.scrat.app.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.scrat.app.core.CommonContext;

/**
 * Created by yixuanxuan on 16/4/12.
 */
public class NetUtil {
    private NetUtil() {
    }

    public static int getNetworkType(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.getType();
        } catch (Throwable e) {
            return -1/*ConnectivityManager.TYPE_NONE*/;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isAvailable() && info.isConnected();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable(CommonContext.getContext());
    }

}
