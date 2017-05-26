package com.eber.bfs.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.eber.bfs.R;
import com.eber.bfs.bean.QAT;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;


/**
 * Created by wxd on 2017/5/9.
 */

public class QatAdapter extends CommonAdapter<QAT> {
    public QatAdapter(Context context, int layoutId, List datas) {
        super(context, R.layout.layout_qat_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QAT item, int position) {
        if (null == item.typeName || item.typeName.equals("")) {
            holder.setText(R.id.qat_item_name, item.title);
        }else{
            holder.setText(R.id.qat_item_name, item.typeName+":");
            TextView tv = holder.getView(R.id.qat_item_name);
            holder.setBackgroundColor(R.id.root, Color.LTGRAY);
            tv.setTextSize(14);
        }
    }
}
