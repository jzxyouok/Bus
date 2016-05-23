package com.scrat.app.core.common;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yixuanxuan on 16/5/14.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();

    }

    protected <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseRecyclerViewHolder setText(int viewId, CharSequence content) {
        TextView view = getView(viewId);
        view.setText(content);
        return this;
    }

    public BaseRecyclerViewHolder setVisibility(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
}
