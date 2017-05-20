package com.eber.bfs.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eber.bfs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 设备管理
 */

public class EquipmentManagementAdapter extends BaseAdapter {
    Context context;
    private List<com.eber.bfs.bean.Equipment> list;

    public EquipmentManagementAdapter(Context context, List<com.eber.bfs.bean.Equipment> equipmentss) {
        this.context = context;
        if (equipmentss == null) {
            list = new ArrayList<>();
        } else {
            this.list = equipmentss;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public com.eber.bfs.bean.Equipment getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.equipment_management_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.equipment_name);
            holder.xinghao = (TextView) convertView
                    .findViewById(R.id.equipment_mark);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        com.eber.bfs.bean.Equipment test = getItem(position);
        holder.name.setText(test.typename);
        holder.xinghao.setText(test.mac);
        com.eber.bfs.utils.ImageLoader.displayImage(context, test.imgurl, holder.image);
        return convertView;
    }

    public List<com.eber.bfs.bean.Equipment> getData() {
        return list;
    }

    class ViewHolder {
        public TextView name;
        public TextView xinghao;
        public ImageView image;
    }

}
