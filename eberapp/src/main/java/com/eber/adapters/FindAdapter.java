package com.eber.adapters;

import android.content.Context;

import com.eber.R;
import com.eber.bean.Artivcle;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by wxd on 2017/4/29.
 */

public class FindAdapter extends CommonAdapter<Artivcle> {
    public FindAdapter(Context context, int layoutId, List<Artivcle> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Artivcle item, int position) {
        viewHolder.setText(R.id.find_item_title, item.title);
    }
}
