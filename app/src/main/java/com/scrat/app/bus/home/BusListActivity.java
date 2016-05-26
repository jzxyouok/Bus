package com.scrat.app.bus.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseActivity;
import com.scrat.app.core.utils.ActivityUtils;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class BusListActivity extends BaseActivity {
    private BusListFragment mFragment;

    private static final String sExtraKeyBusId = "bus_id";
    private static final String sExtraKeyBusName = "bus_name";

    public static void show(Activity ctx, String busId, String busName) {
        Intent i = new Intent(ctx, BusListActivity.class);
        i.putExtra(sExtraKeyBusId, busId);
        i.putExtra(sExtraKeyBusName, busName);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_base);

        if (mFragment == null) {
            String busId = getIntent().getStringExtra(sExtraKeyBusId);
            String busName = getIntent().getStringExtra(sExtraKeyBusName);
            mFragment = BusListFragment.newInstance(busId, busName);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }
}
