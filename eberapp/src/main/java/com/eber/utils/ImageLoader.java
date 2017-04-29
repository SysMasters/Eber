package com.eber.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by WangLibo on 2017/4/23.
 */

public class ImageLoader {

    /**
     * 说明
     *
     *
     * 不加载缓存
     * Glide.with(this).load(active.getSIZE_URL()).diskCacheStrategy(DiskCacheStrategy.NONE).into(img);
     */

    /**
     * load SD卡资源：load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg")
     * load assets资源：load("file:///android_asset/f003.gif")
     * load raw资源：load("Android.resource://com.frank.glide/raw/raw_1")或load("android.resource://com.frank.glide/raw/"+R.raw.raw_1)
     * load drawable资源：load("android.resource://com.frank.glide/drawable/news")或load("android.resource://com.frank.glide/drawable/"+R.drawable.news)
     * load ContentProvider资源：load("content://media/external/images/media/139469")
     * load http资源：load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg")
     * load https资源：load("https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp")
     */

    /**
     * 不限类型
     * load(Uri uri)，load(File file)，load(Integer resourceId)，load(URL url)，load(byte[] model)，load(T model)，loadFromMediaStore(Uri uri)。
     */


    private static DrawableRequestBuilder<String> loadImage(DrawableTypeRequest<String> typeRequest) {
        return typeRequest
//                .placeholder(R.mipmap.icon)// 占位图
//                .error(R.mipmap.icon)
                .dontAnimate();
    }
    private static DrawableRequestBuilder<String> loadImage(DrawableTypeRequest<String> typeRequest,int defaultResId) {
        return typeRequest
                .placeholder(defaultResId)// 占位图
                .error(defaultResId)
                .dontAnimate();
    }


    public static void displayImage(Context context, String uri, ImageView imageView) {
        loadImage(Glide.with(context).load(uri)).into(imageView);
    }



    public static void displayImage(Context context, String uri, final OnLoadBitmapListener listener) {
        Glide.with(context).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (null != listener) {
                    listener.loadBitmap(resource);
                }
            }

        });
    }


    public interface OnLoadBitmapListener {
        void loadBitmap(Bitmap bitmap);
    }

}
