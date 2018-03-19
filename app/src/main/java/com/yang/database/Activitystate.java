package com.yang.database;

import org.litepal.crud.DataSupport;

import java.sql.Date;

/**
 * Created by liu on 2017/7/23.
 */

public class Activitystate extends DataSupport {


    private String username;
    private Date datetime;
    private float sittime;   //坐的时间
    private float walktime;  //行走的时间
    private float lietime;   //躺的时间



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public float getSittime() {
        return sittime;
    }

    public void setSittime(float sittime) {
        this.sittime = sittime;
    }

    public float getWalktime() {
        return walktime;
    }

    public void setWalktime(float walktime) {
        this.walktime = walktime;
    }

    public float getLietime() {
        return lietime;
    }

    public void setLietime(float lietime) {
        this.lietime = lietime;
    }
}