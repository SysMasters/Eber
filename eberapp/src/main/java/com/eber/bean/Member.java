package com.eber.bean;

// FIXME generate failure  field _$UserName274

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 主页成员信息
 * Created by WangLibo on 2017/4/29.
 */

public class Member implements Parcelable,Serializable {


    /**
     * id : 22
     * parentId : 22
     * sex : 2
     * userName : 我
     */

//    public String id;
//    public String parentId;
//    public String sex;
//    public String userName;

    public String id;
    public String parentId;
    public String birthday;
    public String sex;
    public String height;
    public String waistline;
    public String cellphone;
    public String description;
    public String hipline;
    public String userName;
    public String pregnate;
    public String rigesterIP;
    public String password;

    public Member(String id, String parentId, String sex, String userName) {
        this.id = id;
        this.parentId = parentId;
        this.sex = sex;
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.parentId);
        dest.writeString(this.sex);
        dest.writeString(this.userName);
    }

    public Member() {
    }

    protected Member(Parcel in) {
        this.id = in.readString();
        this.parentId = in.readString();
        this.sex = in.readString();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
