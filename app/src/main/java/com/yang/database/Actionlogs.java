package com.yang.database;

import org.litepal.crud.DataSupport;

import java.sql.Date;

/**
 * Created by liu on 2017/7/23.
 */

public class Actionlogs extends DataSupport {
    private String oprtype;  //操作类型
    private String oprtb;    //操作结果
    private String opruser;  //用户名
    private Date oprtime;    //日期


    public Date getOprtime() {
        return oprtime;
    }

    public void setOprtime(Date oprtime) {
        this.oprtime = oprtime;
    }

    public String getOprtype() {
        return oprtype;
    }

    public void setOprtype(String oprtype) {
        this.oprtype = oprtype;
    }

    public String getOprtb() {
        return oprtb;
    }

    public void setOprtb(String oprtb) {
        this.oprtb = oprtb;
    }

    public String getOpruser() {
        return opruser;
    }

    public void setOpruser(String opruser) {
        this.opruser = opruser;
    }


}
