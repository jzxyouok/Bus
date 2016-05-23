package com.scrat.app.core.common;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by yixuanxuan on 16/5/14.
 */
public abstract class BaseRecyclerViewAdapter<Item, Holder extends BaseRecyclerViewHolder> extends RecyclerView.Adapter<Holder> {

    protected abstract void initView(Holder holder, int position, Item item);

    private List<Item> mList;

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Item item = getItem(position);
        if (item == null)
            return;

        initView(holder, position, item);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setList(List<Item> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        mList.add(item);
        notifyDataSetChanged();
    }

    public void addItem(Item item, int location) {
        mList.add(location, item);
        notifyDataSetChanged();
    }

    public void addList(List<Item> list) {
        if (list == null || list.size() == 0)
            return;

        if (mList == null) {
            setList(list);
            return;
        }

        mList.addAll(list);
        notifyDataSetChanged();
    }

    private Item getItem(int position) {
        if (position + 1 > getItemCount())
            return null;

        return mList.get(position);
    }
}
