package com.eber.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by WangLibo on 2017/5/3.
 */

public class ShareUtil {

    private Activity mContext;

    public ShareUtil(Activity context) {
        this.mContext = context;
    }

    public static final String WEIXIN_PACKAGE_NAME = "";
    public static final String QQ_PACKAGE_NAME = "";
    //  public static final String ;  

    /**
     * 分享图片
     */
    public void shareImg() {
        if (getScreenImage() == null) {
            Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = getScreenImage();
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent it = new Intent(Intent.ACTION_SEND);
            it.setType("text/plain");
            List<ResolveInfo> resInfo = mContext.getPackageManager().queryIntentActivities(it,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (!resInfo.isEmpty()) {
                List<Intent> targetedShareIntents = new ArrayList<Intent>();
                for (ResolveInfo info : resInfo) {
                    Intent targeted = new Intent(Intent.ACTION_SEND);
                    targeted.setType("image/*");
                    targeted.putExtra(Intent.EXTRA_STREAM, uri);
                    ActivityInfo activityInfo = info.activityInfo;

                    // judgments : activityInfo.packageName, activityInfo.name, etc.  
                    if (activityInfo.packageName.contains("com.tencent.mm") || activityInfo.packageName.contains("com.tencent.mobileqq") || activityInfo.packageName.contains("com.sina.weibo")) {

                        targeted.setPackage(activityInfo.packageName);
                        targeted.setClassName(activityInfo.packageName, activityInfo.name);
                        targetedShareIntents.add(targeted);
                    }
                }
                try {
                    if (targetedShareIntents.size() == 0) {
                        Toast.makeText(mContext, "不存在分享组件", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),
                            "分享到");
                    if (chooserIntent == null) {
                        return;
                    }
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

                    mContext.startActivity(chooserIntent);
                } catch (Exception ex) {
                    Toast.makeText(mContext, "不存在分享组件", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "不存在分享组件", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
        }
        Loading.dismiss();
    }

    /**
     * 获取包名
     *
     * @return
     */
    private String getPackageName() {
        try {
            String pkName = mContext.getPackageName();
            String versionName = mContext.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = mContext.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 获取和保存当前屏幕的截图
     */
    public File getScreenImage() {
        //1.构建Bitmap    
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();

        Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //2.获取屏幕    
        View decorview = mContext.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();

        String SavePath = getSDCardPath() + "/eber/ScreenImage";

        //3.保存Bitmap     
        String filepath = "";
        File file = null;
        try {
            File path = new File(SavePath);
            //文件    
            filepath = SavePath + File.separator + System.currentTimeMillis() + ".png";
            file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取SDCard的目录路径功能
     *
     * @return
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在  
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    private void deleteImageFile() {

    }
}
