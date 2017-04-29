package com.eber.views.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.eber.R;
import com.eber.utils.ImageLoader;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

    /**
     * 获取ImageView视图的同时加载显示url
     *
     * @return
     */
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        ImageLoader.displayImage(context, url, imageView);
        return imageView;
    }
}
