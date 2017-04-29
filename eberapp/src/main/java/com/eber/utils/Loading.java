package com.eber.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.eber.R;
import com.eber.base.ActivityManager;

/**
 * Created by WangLibo on 2017/4/23.
 */

public class Loading {
    private static Dialog loading;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void show() {
        if (loading == null) {
            Activity activity = ActivityManager.getInstance().getTopActivity();
            loading = new Dialog(activity, R.style.loding_dialog);
            // 触摸不消失
            loading.setCanceledOnTouchOutside(false);
            Window w = loading.getWindow();
            loading.show();
            View loadingView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_process_dialog, null);
            w.setContentView(loadingView);
            //            w.setContentView(R.layout.loading_process_dialog_color);
            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Loading.dismiss();
//                    NetUtils.cancelCurHandler();
                }
            });
        } else {
            try {
                loading.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭对话框
     */

    public static void dismiss() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
            loading = null;
        }
    }

    /**
     * 对话框是否显示
     *
     * @return
     */
    public static boolean isShowing() {
        if (loading != null) {
            return loading.isShowing();
        }
        return false;
    }

    /**
     * 释放资源
     */
    public static void destory() {
        dismiss();
        if (loading != null) {
            loading = null;
            mContext = null;
        }

    }
}
