package com.eber.http;

/**
 * 接口地址
 * Created by WangLibo on 2017/4/23.
 */

public class HttpUrls {

    public final static String IP = "http://112.74.62.116:8080/";
    public final static String BASEURL = IP + "ieber/";


    public final static String FINDEQUIPARRAY = BASEURL + "equipTypeAPP/findEquipArray.shtml";
    public final static String GETVCODE = BASEURL + "memberLoginAPP/getVCode.shtml";
    public final static String REGISTERBYCELLPHONE = BASEURL + "memberLoginAPP/registerByCellphone.shtml";
    public final static String LOGIN = BASEURL + "memberLoginAPP/login.shtml";
    public final static String CHECKSESSIONID = BASEURL + "memberLoginAPP/checkSessionId.shtml";
    public final static String MODIFYMEMBERINFO = BASEURL + "memberAPP/modifyMemberInfo.shtml";
    public final static String FINDLASTRECORDDETAIL = BASEURL + "memberRecordAPP/findLastRecordDetail.shtml";
    /**
     * 三方注册
     */
    public final static String REGISTERTHIRDMEMBER = BASEURL + "memberLoginAPP/registerThirdMember.shtml";
    /**三方登录**/
    public final static String MEMBERLOGINAPP = BASEURL + "memberLoginAPP/login.shtml";
    // 发现 加载图片和文章
    public final static String FINDARTICLEALL = BASEURL + "articleAPP/findArticleAll.shtml";
    // 根据文章ID显示文章内容
    public final static String FINDARTICLEBYID = BASEURL + "articleAPP/findArticleById.shtml?articleId=%s";
    // 进入我的页 获取数据
    public final static String TOMYPAGE = BASEURL + "memberLoginAPP/toMyPage.shtml";
    // 签到加分
    public final static String ADDSCORE = BASEURL + "memberScoreAPP/addScore.shtml";
    // 进入修改资料
    public final static String GETMEMBERINFO = BASEURL + "memberAPP/getMemberInfo.shtml";
    // 退出登录
    public final static String LOGOUT = BASEURL + "memberLoginAPP/logout.shtml";
    // 常见问题列表
    public final static String FINDQAALL = BASEURL + "QAAPP/findQAAll.shtml";
    // 获取微信公众号信息
    public final static String GETPUBLICMUMBER = BASEURL + "dictionaryAPP/getPublicMumber.shtml";

}
