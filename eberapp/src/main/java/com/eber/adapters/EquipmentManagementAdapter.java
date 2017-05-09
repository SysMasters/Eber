package com.eber.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eber.R;
import com.eber.bean.Equipment;
import com.eber.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 设备管理
 */

public class EquipmentManagementAdapter extends BaseAdapter {
    Context context;
    private List<Equipment> list;

    public EquipmentManagementAdapter(Context context, List<Equipment> equipmentss) {
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
    public Equipment getItem(int position) {
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
        Equipment test = getItem(position);
        holder.name.setText(test.typename);
        holder.xinghao.setText(test.mac);
        ImageLoader.displayImage(context, test.imgurl, holder.image);
        return convertView;
    }

    public List<Equipment> getData() {
        return list;
    }

    class ViewHolder {
        public TextView name;
        public TextView xinghao;
        public ImageView image;
    }

}
