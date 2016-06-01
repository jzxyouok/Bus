package com.scrat.app.bus.common;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.scrat.app.core.common.BaseView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yixuanxuan on 16/5/12.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onNoNetworkError() {
        Snackbar.make(getWindow().getDecorView(), "当前没有可用网络", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onTokenError() {

    }

    public void showToask(String content) {
        Snackbar.make(getWindow().getDecorView(), content, Snackbar.LENGTH_LONG).show();
    }
}
