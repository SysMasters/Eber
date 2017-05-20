package com.eber.bfs.bean;

import java.io.Serializable;

/**
 * Created by WangLibo on 2017/5/1.
 */

public class BodyInfo implements Serializable {


    /**
     * subcutaneousfat : 29
     * weight : 65
     * updateTime : 09:14
     * organfat : 6
     * score : 80
     * muscle : 39
     * bodywater : 50
     * id : 22
     * bodyAge : 18
     * bonerisk : 2
     * bodyShape : 1
     * protein : 16
     * bone : 2
     * fatRate : 25
     * BMI : 23.9
     * basicmetabolism : 1500
     */

    public String subcutaneousfat;
    public String weight;
    public String updateTime;
    public String organfat;
    public String score;
    public String muscle;
    public String bodywater;
    public String id;
    public String bodyAge;
    public String bonerisk;
    public String bodyShape;
    public String protein;
    public String bone;
    public String fatRate;
    public String BMI;
    public String basicmetabolism;

    @Override
    public String toString() {
        return "BodyInfo{" +
                "subcutaneousfat='" + subcutaneousfat + '\'' +
                ", weight='" + weight + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", organfat='" + organfat + '\'' +
                ", score='" + score + '\'' +
                ", muscle='" + muscle + '\'' +
                ", bodywater='" + bodywater + '\'' +
                ", id='" + id + '\'' +
                ", bodyAge='" + bodyAge + '\'' +
                ", bonerisk='" + bonerisk + '\'' +
                ", bodyShape='" + bodyShape + '\'' +
                ", protein='" + protein + '\'' +
                ", bone='" + bone + '\'' +
                ", fatRate='" + fatRate + '\'' +
                ", BMI='" + BMI + '\'' +
                ", basicmetabolism='" + basicmetabolism + '\'' +
                '}';
    }
}
