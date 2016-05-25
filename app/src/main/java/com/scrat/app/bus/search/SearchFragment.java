package com.scrat.app.bus.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
    private MyAdapter mAdapter;
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
        mSearchIv = (ImageView) root.findViewById(R.id.iv_search);
        mSearchContentEt = (EditText) root.findViewById(R.id.et_search);
        RecyclerView resultListRv = (RecyclerView) root.findViewById(R.id.rv_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        resultListRv.setLayoutManager(manager);
        final Activity activity = getActivity();
        mAdapter = new MyAdapter(new OnItemClickListener() {
            @Override
            public void onItemSelected(String busId, String busName) {
                BusListActivity.show(activity, busId, busName);
            }
        });
        resultListRv.setAdapter(mAdapter);

        mBackIv.setOnClickListener(this);
        mSearchIv.setOnClickListener(this);
        mPresenter = new SearchPresenter(this);
        mSearchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    handled = true;
                }
                return handled;
            }
        });

        return root;
    }

    private void search() {
        ActivityUtils.hideKeyboard(getActivity());
        String content = mSearchContentEt.getText().toString();
        mPresenter.search(content);
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
            search();
        }
    }

    private void setResultText(String content) {
        mResultTv.setVisibility(View.VISIBLE);
        mResultTv.setText(content);
    }

    @Override
    public void showNoResult() {
        setResultText(getString(R.string.no_result));
    }

    @Override
    public void showResult(List<BusInfo> busInfos) {
        int totalResult = busInfos.size();
        String searchResultFormat = getString(R.string.search_result);
        setResultText(String.format(searchResultFormat, totalResult));
        mAdapter.setList(busInfos);
    }

    @Override
    public void onSearchError() {
        showMsg(getString(R.string.server_error));
    }

    interface OnItemClickListener {
        void onItemSelected(String busId, String busName);
    }

    private static class MyAdapter extends BaseRecyclerViewAdapter<BusInfo, BaseRecyclerViewHolder> {
        private OnItemClickListener mListener;
        public MyAdapter(OnItemClickListener listener) {
            mListener = listener;
        }

        @Override
        protected void initView(final BaseRecyclerViewHolder holder, int position, BusInfo busInfo) {
            holder.setText(R.id.tv_name, busInfo.getBusName());
            final String busId = busInfo.getBusId();
            final String busName = busInfo.getBusName();
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null)
                        return;

                    mListener.onItemSelected(busId, busName);
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
