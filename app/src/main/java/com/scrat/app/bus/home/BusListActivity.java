package com.scrat.app.bus.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.scrat.app.bus.R;
import com.scrat.app.core.utils.ActivityUtils;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class BusListActivity extends AppCompatActivity {
    private BusListFragment mFragment;

    private static final String sExtraKeyBusId = "bus_id";

    public static void show(Activity ctx, String busId) {
        Intent i = new Intent(ctx, BusListActivity.class);
        i.putExtra(sExtraKeyBusId, busId);
        ctx.startActivity(i);
        ctx.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_base);

        String busId = getIntent().getStringExtra(sExtraKeyBusId);

        if (mFragment == null) {
            mFragment = BusListFragment.newInstance(busId);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
    }
}
