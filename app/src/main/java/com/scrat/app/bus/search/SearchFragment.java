package com.scrat.app.bus.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scrat.app.bus.R;
import com.scrat.app.bus.common.BaseFragment;
import com.scrat.app.bus.home.BusListActivity;
import com.scrat.app.bus.model.BusInfo;
import com.scrat.app.core.common.BaseRecyclerViewAdapter;
import com.scrat.app.core.common.BaseRecyclerViewHolder;
import com.scrat.app.core.utils.ActivityUtils;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/20.
 */
public class SearchFragment extends BaseFragment
        implements View.OnClickListener, SearchContract.View {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private SearchContract.Presenter mPresenter;

    private ImageView mBackIv;
    private ImageView mSearchIv;
    private EditText mSearchContentEt;
    private RecyclerView mResultListRv;
    private BaseRecyclerViewAdapter mAdapter;
    private TextView mResultTv;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frg_search, container, false);
        mResultTv = (TextView) root.findViewById(R.id.tv_result);
        mBackIv = (ImageView) root.findViewById(R.id.iv_back);
        mSearchContentEt = (EditText) root.findViewById(R.id.et_search);
        mSearchIv = (ImageView) root.findViewById(R.id.iv_search);
        mResultListRv = (RecyclerView) root.findViewById(R.id.rv_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mResultListRv.setLayoutManager(manager);
        mAdapter = new MyAdapter(getActivity());
        mResultListRv.setAdapter(mAdapter);


        mBackIv.setOnClickListener(this);
        mSearchIv.setOnClickListener(this);
        mPresenter = new SearchPresenter(this);

        return root;
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mBackIv) {
            getActivity().finish();
            return;
        }

        if (v == mSearchIv) {
            ActivityUtils.hideKeyboard(getActivity());
            String content = mSearchContentEt.getText().toString();
            mPresenter.search(content);
            return;
        }
    }

    private void setResultText(String content) {
        mResultTv.setVisibility(View.VISIBLE);
        mResultTv.setText(content);
    }

    @Override
    public void showNoResult() {
        setResultText("没有找到数据");
    }

    @Override
    public void showResult(List<BusInfo> busInfos) {
        int totalResult = busInfos.size();
        setResultText(String.format("共搜索出 %d 条结果", totalResult));
        mAdapter.setList(busInfos);
    }

    @Override
    public void onSearchError() {
        showMsg("服务器有点抽风");
    }

    private static class MyAdapter extends BaseRecyclerViewAdapter<BusInfo, BaseRecyclerViewHolder> {
        private Context mContext;
        public MyAdapter(Context context) {
            mContext = context;
        }

        @Override
        protected void initView(final BaseRecyclerViewHolder holder, int position, BusInfo busInfo) {
            holder.setText(R.id.tv_name, busInfo.getBusName());
            final String busId = busInfo.getBusId();
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusListActivity.show((SearchActivity)mContext, busId);
                }
            });
        }

        @Override
        public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_search_result, parent, false);
            return new BaseRecyclerViewHolder(view);
        }
    }


}
