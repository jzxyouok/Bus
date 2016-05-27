package com.scrat.app.bus.module.bus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.init();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });

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
        String from;
        String to;
        if (list.size() == 0) {
            String unknown = getString(R.string.unknown);
            from = unknown;
            to = unknown;
        } else {
            from = list.get(0).getBusStopName();
            to = list.get(list.size()-1).getBusStopName();
        }
        showBusLine(from, to);
    }

    @Override
    public void onLoadDataError() {
        showMsg(getString(R.string.server_error));
    }

    private void showBusLine(String from, String to) {
        if (getView() == null)
            return;

        TextView busLineTv = (TextView) getView().findViewById(R.id.tv_bus_line);;
        String line = String.format("%s ---> %s", from, to);
        busLineTv.setText(line);
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

    @Override
    public void showLoading() {
        super.showLoading();
        loading(true);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        loading(false);
    }

    private void loading(final boolean show) {
        if (getView() == null)
            return;

        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(show);
            }
        });
    }
}
