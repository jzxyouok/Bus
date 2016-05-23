package com.scrat.app.core;

import android.content.Context;

/**
 * Created by yixuanxuan on 16/5/12.
 */
public class CommonContext {
    private static Context mContext;

    public static Context getContext() {

        if (mContext == null) throw new RuntimeException("need to init context in the application");

        return mContext;
    }

    public static void initContext(Context context) {
        if (mContext == null) {
            mContext = context;
        }
    }

    public static void releaseContext() {
        mContext = null;
    }

}
