package com.scrat.app.bus.common;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.scrat.app.core.common.BaseView;

/**
 * Created by yixuanxuan on 16/5/12.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onNoNetworkError() {
        Snackbar.make(getWindow().getDecorView(), "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onTokenError() {

    }
}
