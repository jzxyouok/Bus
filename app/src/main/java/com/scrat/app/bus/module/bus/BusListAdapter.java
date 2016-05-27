package com.scrat.app.bus.module.bus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scrat.app.bus.R;
import com.scrat.app.bus.model.BusStopInfo;
import com.scrat.app.core.common.BaseRecyclerViewAdapter;
import com.scrat.app.core.common.BaseRecyclerViewHolder;

/**
 * Created by yixuanxuan on 16/5/14.
 */
public class BusListAdapter extends BaseRecyclerViewAdapter<BusStopInfo, BaseRecyclerViewHolder> {

    @Override
    protected void initView(BaseRecyclerViewHolder holder, int position, BusStopInfo busStopInfo) {
        holder.setText(R.id.tv_name, busStopInfo.getBusStopName())
                .setVisibility(R.id.ic_curr_stop,
                        busStopInfo.isArrivaled() ? View.VISIBLE : View.INVISIBLE)
                .setVisibility(R.id.ic_leaving_stop,
                        busStopInfo.isLeaving() ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bus, parent, false);
        return new BaseRecyclerViewHolder(view);
    }

}
