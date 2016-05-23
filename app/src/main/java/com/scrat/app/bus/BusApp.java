package com.scrat.app.bus;

import android.app.Application;
import android.content.Context;

import com.scrat.app.core.CommonContext;

/**
 * Created by yixuanxuan on 16/4/19.
 */
public class BusApp extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        CommonContext.initContext(mContext);
    }
}
