package com.scrat.app.bus.module.yct;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseActivity;
import com.scrat.app.core.utils.ActivityUtils;

/**
 * Created by yixuanxuan on 16/6/1.
 */
public class YctCardBalanceActivity extends BaseActivity implements View.OnClickListener {
    private static final String sUrlFormat = "http://weixin.gzyct.com/busiqry/user-card-balance!otherBalanceQry.action?openid=oKYOJjqvCJWcqNJDVLJF9lZIYVNY&cardno=%s";
    private static final String sExtraKey = "card_id";
    public static void startActivity(Context ctx, String cardId) {
        Intent i = new Intent(ctx, YctCardBalanceActivity.class);
        i.putExtra(sExtraKey, cardId);
        ctx.startActivity(i);
    }

    private EditText mSearchEt;
    private ImageView mSearchIv;
    private ImageView mBackIv;
    private WebView mWebView;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_yct_balance);

        mWebView = (WebView) findViewById(R.id.wv_content);
        mSearchIv = (ImageView) findViewById(R.id.iv_search);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
        mSearchEt = (EditText) findViewById(R.id.et_search);
        mSearchIv.setOnClickListener(this);

        mProgressBar = ProgressDialog.show(YctCardBalanceActivity.this, "羊城通余额", "正在查询...");

        String cardId = getIntent().getStringExtra(sExtraKey);
        if (!TextUtils.isEmpty(cardId)) {
            mSearchEt.setText(cardId);
            showContent(cardId);
        } else {
            mProgressBar.dismiss();
        }
    }

    private void showContent(String cardId) {
        ActivityUtils.hideKeyboard(this);
        String url = String.format(sUrlFormat, cardId);
        mProgressBar.show();
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (mProgressBar.isShowing()) {
                    mProgressBar.dismiss();
                }
            }

        });

        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        if (v == mSearchIv) {
            String content = mSearchEt.getText().toString();
            if (TextUtils.isEmpty(content)) {
                showToask("请输入正确的羊城通卡号");
                return;
            }

            showContent(content);
        } else if (v == mBackIv) {
            finish();
        }
    }
}
