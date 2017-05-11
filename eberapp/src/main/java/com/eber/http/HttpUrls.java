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
    /**提交称重记录**/
    public final static String ADDRECORD = BASEURL + "memberRecordAPP/addRecord.shtml";
    /**用户绑定设备**/
    public final static String ADDMEMBEREQUIP = BASEURL + "memberEquipAPP/addMemberEquip.shtml";
    /**解绑设备**/
    public final static String DELMEMBEREQUIP = BASEURL + "memberEquipAPP/delMemberEquip.shtml";
    /**根据memberId取得用户所有绑定设备**/
    public final static String FINDEQUIPLIST = BASEURL + "memberEquipAPP/findEquipList.shtml?memberId=%s";
    // 趋势
    public final static String RECORDTREND = BASEURL + "memberRecordAPP/recordTrend.shtml";

    public final static String FINDRECORDDATE = BASEURL + "memberRecordAPP/findRecordDate.shtm";
    // 进入我的目标
    public final static String FINDMEMBERAIM = BASEURL + "memberAimAPP/findMemberAim.shtml";
    // 确定目标
    public final static String ADDORUPDATEAIM = BASEURL + "memberAimAPP/addOrUpdateAim.shtml";
    // 忘记密码获取验证码
    public final static String GETVCODEONLY = BASEURL + "memberLoginAPP/getVCodeOnly.shtml";
    // 修改密码
    
    public final static String FORGETPASSWORD = BASEURL + "memberLoginAPP/forgetPassword.shtml";

    // 修改密码
    public final static String MODIFYLOGININFO = BASEURL + "memberAPP/modifyLoginInfo.shtml";
    // 最近一次称重信息
    public final static String FINDLASTRECORD = BASEURL + "memberRecordAPP/findLastRecord.shtml";

    // 取得用户某天的所有记录（点击日历上的某一天或点击记录上的某一条）
    public final static String FINDRECORDBYDATE = BASEURL + "memberRecordAPP/findRecordByDate.shtml";
    // 删除某条记录
    public final static String DELETEENTITY = BASEURL + "memberRecordAPP/deleteEntity.shtml";

}
