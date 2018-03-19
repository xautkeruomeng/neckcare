package com.yang.database;

import org.litepal.crud.DataSupport;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liu on 2017/7/22.
 */

public class Accounts extends DataSupport{
    //Accounts类相当于数据库中的Accounts表
    //将Accounts类添加到映射模型列表中
    //只有private修饰的字段才会被映射到数据库表中
    // 即如果有某一个字段不想映射的话，就设置为public、protected或者default修饰符就可以了。

    //private long ID;
    private String password;
    private String gender;      //性别
    private String username;
    private String tel;
    private String authority;   //授权类别
    private Date datetime;
    private int rank;           //权限
    private String userimage;   //头像
    //Accounts类相当于数据库中的Accounts表
    //将Accounts类添加到映射模型列表中
    //只有private修饰的字段才会被映射到数据库表中
    // 即如果有某一个字段不想映射的话，就设置为public、protected或者default修饰符就可以了。


    //建立表之间关系
    //多对一
/*
    private List<Neckstate> neck = new ArrayList<Neckstate>();
    private List<Dayneckstate> dayneck = new ArrayList<Dayneckstate>();
*/

   /* public long getId() {
        return ID;
    }

    public void setId(long id) {
        this.ID = id;
    }*/



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }
}
