package com.scrat.app.bus.search;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseActivity;
import com.scrat.app.core.utils.ActivityUtils;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class SearchActivity extends BaseActivity {
    private SearchFragment mFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        if (mFragment == null) {
            mFragment = SearchFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }
}
