package com.zhy.adapter.abslistview;

import android.content.Context;

import com.zhy.adapter.abslistview.base.ItemViewDelegate;

import java.util.List;

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected int selectedPosition = -1;// 选中的位置  

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T item, int position);

    public void refresh(List<T> datas) {
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void refresh2(List<T> datas) {
        this.mDatas =datas;
        this.notifyDataSetChanged();
    }

}
