package com.scrat.app.core.net;

import com.scrat.app.est.utils.ActivityUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yixuanxuan on 16/4/29.
 */
public abstract class ResponseCallback<T> implements Callback {
    protected abstract void onRequestFailure(IOException e);
    protected abstract void onRequestSuccess(T t);
    protected abstract void onHttpError();

    protected abstract T parseResponse(Response response);

    @Override
    public void onFailure(final Call call, final IOException e) {
        ActivityUtils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onRequestFailure(e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            final T t = parseResponse(response);
            ActivityUtils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onRequestSuccess(t);
                }
            });
        } else {
            ActivityUtils.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onHttpError();
                }
            });
        }

    }

}
