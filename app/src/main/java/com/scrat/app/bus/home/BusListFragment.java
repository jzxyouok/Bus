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
    private TextView mBusName;
    private ImageView mOrderIv;
    private ImageView mBackIv;

    private static final String sKey = "bus_id";

    public static BusListFragment newInstance(String busId) {
        BusListFragment fragment = new BusListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(sKey, busId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String busId = getArguments().getString(sKey);
        mPresenter = new BusListPresenter(this, busId);
        mAdapter = new BusListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frg_home, container, false);
        mBusName = (TextView) root.findViewById(R.id.tv_bus_name);
        mOrderIv = (ImageView) root.findViewById(R.id.iv_order);
        mOrderIv.setOnClickListener(this);
        mBackIv = (ImageView) root.findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.activity_bus_list_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public void showBusName(String busName) {
        mBusName.setText(busName);
    }

    @Override
    public void showBusStop(List<BusStopInfo> list) {
        mAdapter.setList(list);
    }

    @Override
    public void onLoadDataError() {
        showMsg("获取数据失败");
    }

    public void refreshLocation() {
        mPresenter.init();
    }

    @Override
    public void onClick(View v) {
        if (v == mBackIv) {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return;
        }

        if (v == mOrderIv) {
            mPresenter.changeOrder();
        }
    }
}
