package com.eber.bfs.bean;

import java.io.Serializable;

/**
 * Created by wxd on 2017/4/30.
 */

public class User implements Serializable {

    public int id;
    public int parentId;
    public String cellphone;
    public String rigesterTime;
    public String rigesterIP;
    public String sessionId;
    public String birthday;
    public int sex;
    public String userName;
    public int height;
    public int waistline;
    public int hipline;
    public int pregnate;

    /**
     * 判断当前用户是否为父用户
     *
     * @return
     */
    public boolean isParentUser() {
        if (id == parentId) {
            return true;
        }
        return false;
    }

}
