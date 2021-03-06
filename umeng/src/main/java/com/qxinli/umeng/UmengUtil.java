package com.qxinli.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.qxinli.umeng.login.AuthCallback;
import com.qxinli.umeng.login.BaseInfo;
import com.qxinli.umeng.login.QQInfo;
import com.qxinli.umeng.login.SinaInfo;
import com.qxinli.umeng.login.WeixinInfo;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class UmengUtil {
    private static ScreenShot screenShot = new ScreenShot();
    private static Context context;
    private static UMShareAPI umShareAPI;
    private static SHARE_MEDIA[] shareMedias;//SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE

    public static void init(Context context1, boolean degbug, SHARE_MEDIA... shareMediaList) {
        context = context1;
        shareMedias = shareMediaList;
        Config.isJumptoAppStore = true;
        Config.DEBUG = degbug;
        umShareAPI = UMShareAPI.get(context1);
        //对应平台没有安装的时候跳转转到应用商店下载,其中qq 微信会跳转到下载界面进行下载，其他应用会跳到应用商店进行下载
        //友盟统计
        //        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType. E_UM_NORMAL);
    }

    public static void setKeySecretWeixin(String key, String secret) {
        PlatformConfig.setWeixin(key, secret);
    }

    public static void setKeySecretQQ(String key, String secret) {
        PlatformConfig.setQQZone(key, secret);
    }

    public static void setKeySecretSina(String key, String secret, String auth) {
        PlatformConfig.setSinaWeibo(key, secret, auth);
    }


    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    public static void onDestroy(Activity activity) {
        UMShareAPI.get(activity).release();
    }


    public static void shareTxt(Activity activity, final String uid,
                                String title, String desc, String thumbUrl,
                                String targetUrl, final ShareCallback callback) {
        share(0, activity, uid, title, desc, "", thumbUrl, targetUrl, callback);
    }

    public static void bind(Activity activity, SHARE_MEDIA platfrom, AuthCallback callback) {
        deleteAuth(activity, platfrom, callback);
    }

    public static void bindQQ(Activity activity, AuthCallback callback) {
        deleteAuth(activity, SHARE_MEDIA.QQ, callback);
    }

    public static void bindWeChat(Activity activity, AuthCallback callback) {
        deleteAuth(activity, SHARE_MEDIA.WEIXIN, callback);
    }

    public static void bindSina(Activity activity, AuthCallback callback) {
        deleteAuth(activity, SHARE_MEDIA.SINA, callback);
    }

    private static void deleteAuth(final Activity activity, final SHARE_MEDIA platfrom, final AuthCallback callback) {
        login(activity, platfrom, callback);
    }


    /**
     * 自带分享的统计,其中uid是app本身的账户系统的id
     *
     * @param type
     * @param activity
     * @param uid
     * @param title
     * @param desc
     * @param thumbUrl
     * @param mediaUrl
     * @param targetUrl
     * @param callback
     */

    private static void share(int type, Activity activity, final String uid,
                              String title, String desc, String thumbUrl, String mediaUrl,
                              String targetUrl, final ShareCallback callback) {

        ShareAction action = new ShareAction(activity);

        if (type == 0) {
            action.withMedia(new UMImage(activity, mediaUrl));
        } else if (type == 1) {
            UMusic music = new UMusic(mediaUrl);
            music.setTitle(title);//音乐的标题
            music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
            music.setDescription(desc);//音乐的描述
            action.withMedia(music);
        } else if (type == 2) {
            UMVideo music = new UMVideo(mediaUrl);
            music.setTitle(title);//音乐的标题
            music.setThumb(new UMImage(activity, thumbUrl));//音乐的缩略图
            music.setDescription(desc);//音乐的描述
            action.withMedia(music);
        }
        action
                .withText(desc)
                .setDisplayList(shareMedias)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        UMPlatformData.UMedia media = UMPlatformData.UMedia.SINA_WEIBO;
                        if (share_media == SHARE_MEDIA.QQ) {
                            media = UMPlatformData.UMedia.TENCENT_QQ;
                        } else if (share_media == SHARE_MEDIA.SINA) {
                            media = UMPlatformData.UMedia.SINA_WEIBO;
                        } else if (share_media == SHARE_MEDIA.WEIXIN) {
                            media = UMPlatformData.UMedia.WEIXIN_FRIENDS;
                        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            media = UMPlatformData.UMedia.WEIXIN_CIRCLE;
                        } else if (share_media == SHARE_MEDIA.QZONE) {
                            media = UMPlatformData.UMedia.TENCENT_QZONE;
                        }


                        UMPlatformData platform = new UMPlatformData(media, uid);

                        callback.onResult(share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        callback.onError(share_media, throwable);

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        callback.onCancel(share_media);

                    }
                })
                .open();
    }

    public static void shareImage(Activity activity, File imageFile) {
        shareImage(activity, imageFile, new ShareCallback() {
            @Override
            public void onResult(SHARE_MEDIA var1) {

            }

            @Override
            public void onError(SHARE_MEDIA var1, Throwable var2) {

            }

            @Override
            public void onCancel(SHARE_MEDIA var1) {

            }
        });
    }

    public static void shareImage(Activity activity) {
        try {
            File file = getScreenImage(activity);
            shareImage(activity, file, new ShareCallback() {
                @Override
                public void onResult(SHARE_MEDIA var1) {

                }

                @Override
                public void onError(SHARE_MEDIA var1, Throwable var2) {

                }

                @Override
                public void onCancel(SHARE_MEDIA var1) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享图片
     *
     * @param activity
     * @param imageFile
     * @param callback
     */
    public static void shareImage(Activity activity, File imageFile, final ShareCallback callback) {
        //        new ShareAction(activity).withMedia(new UMImage(activity, imageFile) )
        //                .setCallback(shareListener).share();
        ShareAction action = new ShareAction(activity);
        UMImage umImage = new UMImage(activity, imageFile);
        action.withMedia(umImage)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        UMPlatformData.UMedia media = UMPlatformData.UMedia.SINA_WEIBO;
                        if (share_media == SHARE_MEDIA.QQ) {
                            media = UMPlatformData.UMedia.TENCENT_QQ;
                        } else if (share_media == SHARE_MEDIA.SINA) {
                            media = UMPlatformData.UMedia.SINA_WEIBO;
                        } else if (share_media == SHARE_MEDIA.WEIXIN) {
                            media = UMPlatformData.UMedia.WEIXIN_FRIENDS;
                        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            media = UMPlatformData.UMedia.WEIXIN_CIRCLE;
                        } else if (share_media == SHARE_MEDIA.QZONE) {
                            media = UMPlatformData.UMedia.TENCENT_QZONE;
                        }

                        callback.onResult(share_media);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        callback.onError(share_media, throwable);

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        callback.onCancel(share_media);

                    }
                })
                .open();
    }


    public static void shareMusic(Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback) {
        share(1, activity, uid, title, desc, thumbUrl, musicUrl, targetUrl, callback);
    }

    public static void shareVideo(Activity activity, final String uid,
                                  String title, String desc, String thumbUrl, String musicUrl,
                                  String targetUrl, final ShareCallback callback) {
        share(2, activity, uid, title, desc, thumbUrl, musicUrl, targetUrl, callback);
    }

    public static void loginBySina(Activity activity, AuthCallback callback) {
        login(activity, SHARE_MEDIA.SINA, callback);
    }

    public static void loginByWeixin(Activity activity, AuthCallback callback) {
        login(activity, SHARE_MEDIA.WEIXIN, callback);
    }

    public static void loginByQQ(Activity activity, AuthCallback callback) {
        login(activity, SHARE_MEDIA.QQ, callback);
    }

    private static void login(final Activity activity, final SHARE_MEDIA platform, final AuthCallback callback) {
        umShareAPI.deleteOauth(activity, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                umShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        BaseInfo info = null;
                        if (share_media == SHARE_MEDIA.SINA) {
                            SinaInfo weixinInfo = new SinaInfo();
                            weixinInfo.followers_count = map.get("followers_count");
                            weixinInfo.friends_count = map.get("friends_count");
                            info = weixinInfo;

                        } else if (share_media == SHARE_MEDIA.QQ) {
                            QQInfo weixinInfo = new QQInfo();
                            weixinInfo.is_yellow_year_vip = map.get("is_yellow_year_vip");
                            info = weixinInfo;
                        } else if (share_media == SHARE_MEDIA.WEIXIN) {
                            WeixinInfo weixinInfo = new WeixinInfo();
                            weixinInfo.openid = map.get("openid");
                            weixinInfo.country = map.get("country");
                            info = weixinInfo;

                        }
                        info.accessToken = map.get("accessToken");
                        info.city = map.get("city");
                        info.expiration = map.get("expiration");
                        info.gender = map.get("gender");
                        info.iconurl = map.get("iconurl");
                        info.name = map.get("name");
                        info.province = map.get("province");
                        info.refreshtoken = map.get("refreshtoken");
                        info.uid = map.get("uid");


                        callback.onComplete(i, info);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        callback.onError(i, throwable);

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        callback.onCancel(i);

                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                callback.onError(i, throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                callback.onCancel(i);
            }
        });
        
    }


    //以下的是统计的api

    //    public static void analysisOnResume(Activity activity){
    //        MobclickAgent.onResume(activity);
    //    }
    //    public static void analysisOnPause(Activity activity){
    //        MobclickAgent.onPause(activity);
    //    }
    //    public static void onKillProcess(){
    //        MobclickAgent.onKillProcess(context);
    //    }
    //
    //    public static void analysisOnPageStart(String  pageName){
    //        MobclickAgent.onPageStart(pageName);
    //    }
    //    public static void analysisOnPageEnd(String  pageName){
    //        MobclickAgent.onPageEnd(pageName);
    //    }
    //
    //    public static void onProfileSignIn(String ID) {
    //        MobclickAgent.onProfileSignIn(ID);
    //    }
    //    public static void onProfileSignIn(String provider, String ID) {
    //        MobclickAgent.onProfileSignIn(provider,ID);
    //    }
    //    public static void onProfileSignOff(){
    //        MobclickAgent.onProfileSignOff();
    //    }

    /**
     * 获取和保存当前屏幕的截图
     */
    public static File getScreenImage(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度  
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高  
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉状态栏，如果需要的话  
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);  
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();

        String SavePath = getSDCardPath() + "/eber/ScreenImage";

        //3.保存Bitmap     
        String filepath = "";
        File file = null;
        try {
            File path = new File(SavePath);
            //文件    
            filepath = SavePath + File.separator + "screen_share_image.png";
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
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
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
    private static String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在  
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }
}
