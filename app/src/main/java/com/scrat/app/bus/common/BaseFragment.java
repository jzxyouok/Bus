package com.scrat.app.bus.common;

import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.scrat.app.bus.R;
import com.scrat.app.core.common.BaseView;

/**
 * Created by yixuanxuan on 16/5/15.
 */
public abstract class BaseFragment extends Fragment implements BaseView {
    protected void showMsg(String content) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, content, Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onNoNetworkError() {
        showMsg(getString(R.string.no_net_error));
    }

    @Override
    public void onTokenError() {

    }
}
