package com.scrat.app.core.common;

/**
 * Created by yixuanxuan on 16/4/29.
 */
public interface BaseView {
    void showLoading();
    void hideLoading();
    void onNoNetworkError();
    void onTokenError();
}
