package com.eber.bfs.bean;

// FIXME generate failure  field _$UserName274

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 主页成员信息
 * Created by WangLibo on 2017/4/29.
 */

public class Member implements Serializable, Parcelable {


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

    public Member() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.parentId);
        dest.writeString(this.birthday);
        dest.writeString(this.sex);
        dest.writeString(this.height);
        dest.writeString(this.waistline);
        dest.writeString(this.cellphone);
        dest.writeString(this.description);
        dest.writeString(this.hipline);
        dest.writeString(this.userName);
        dest.writeString(this.pregnate);
        dest.writeString(this.rigesterIP);
        dest.writeString(this.password);
    }

    protected Member(Parcel in) {
        this.id = in.readString();
        this.parentId = in.readString();
        this.birthday = in.readString();
        this.sex = in.readString();
        this.height = in.readString();
        this.waistline = in.readString();
        this.cellphone = in.readString();
        this.description = in.readString();
        this.hipline = in.readString();
        this.userName = in.readString();
        this.pregnate = in.readString();
        this.rigesterIP = in.readString();
        this.password = in.readString();
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
