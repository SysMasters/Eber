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

}
