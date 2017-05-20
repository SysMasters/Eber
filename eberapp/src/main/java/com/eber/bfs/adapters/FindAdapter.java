package com.eber.bfs.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.eber.bfs.R;
import com.eber.bfs.bean.FindArticle;
import com.eber.bfs.utils.ImageLoader;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by wxd on 2017/4/29.
 */

public class FindAdapter extends CommonAdapter<FindArticle> {

    private Context mContext;
    private SimpleDateFormat sdf;
    public FindAdapter(Context context,  List<FindArticle> datas) {
        super(context, R.layout.layout_find_article_item, datas);
        mContext= context;
        sdf = new SimpleDateFormat("MM月dd日");
    }

    @Override
    protected void convert(ViewHolder holder, FindArticle item, int position) {
        holder.setText(R.id.find_item_title, item.title);
        try {
            holder.setText(R.id.find_item_time,sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.createTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setText(R.id.find_subTitle,item.subTitle);
        holder.setText(R.id.find_type_name,item.articleTypeName);
        ImageView imageView = holder.getView(R.id.find_item_img);
        ImageLoader.displayImage(mContext,item.imgurl,imageView);
    }

}
