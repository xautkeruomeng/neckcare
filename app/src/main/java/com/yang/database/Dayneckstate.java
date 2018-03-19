package com.yang.database;

import org.litepal.crud.DataSupport;

import java.sql.Date;

/**
 * Created by liu on 2017/7/23.
 */

public class Dayneckstate extends DataSupport {
    private String username;
    private Date datetime;
    private float averneckangle;
    private float maxneckangle;
    private float usephonetime;

    private Accounts accounts;

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

    public float getAverneckangle() {
        return averneckangle;
    }

    public void setAverneckangle(float averneckangle) {
        this.averneckangle = averneckangle;
    }

    public float getMaxneckangle() {
        return maxneckangle;
    }

    public void setMaxneckangle(float maxneckangle) {
        this.maxneckangle = maxneckangle;
    }

    public float getUsephonetime() {
        return usephonetime;
    }

    public void setUsephonetime(float usephonetime) {
        this.usephonetime = usephonetime;
    }
}
