package com.scrat.app.bus.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseFragment;
import com.scrat.app.bus.model.BusStopInfo;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/15.
 */
public class BusListFragment extends BaseFragment implements BusListContract.View, View.OnClickListener {
    private BusListContract.Presenter mPresenter;
    private BusListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mBusName;
    private ImageView mOrderIv;
    private ImageView mBackIv;
    private ImageView mSearchIv;
    private ImageView mRefreshIv;

    private static final String sKeyId = "bus_id";
    private static final String sKeyBusName = "bus_name";

    public static BusListFragment newInstance(String busId, String busName) {
        BusListFragment fragment = new BusListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(sKeyId, busId);
        bundle.putString(sKeyBusName, busName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String busId = getArguments().getString(sKeyId);
        mBusName = getArguments().getString(sKeyBusName);
        mPresenter = new BusListPresenter(this, busId);
        mAdapter = new BusListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frg_home, container, false);
        TextView busNameTv = (TextView) root.findViewById(R.id.tv_bus_name);
        busNameTv.setText(mBusName);
        mOrderIv = (ImageView) root.findViewById(R.id.iv_order);
        mOrderIv.setOnClickListener(this);
        mBackIv = (ImageView) root.findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
        mSearchIv = (ImageView) root.findViewById(R.id.iv_search);
        mSearchIv.setOnClickListener(this);
        mRefreshIv = (ImageView) root.findViewById(R.id.iv_refresh);
        mRefreshIv.getBackground().setAlpha(100);
        mRefreshIv.setOnClickListener(this);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.init();
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void showBusStop(List<BusStopInfo> list) {
        mAdapter.setList(list);
    }

    @Override
    public void onLoadDataError() {
        showMsg("获取数据失败");
    }

    @Override
    public void onClick(View v) {
        if (v == mBackIv || v == mSearchIv) {
            getActivity().finish();
            return;
        }

        if (v == mOrderIv) {
            mPresenter.changeOrder();
            return;
        }

        if (v == mRefreshIv) {
            mPresenter.init();
        }

    }
}
