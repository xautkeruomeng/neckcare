package com.yang.data;

/**
 * Created by GaoXixi on 2017/11/29.
 */

public class Data {
    private static String userName;

    public static String getUserName(){
        return userName;
    }
    public static void setUserName(String userName){
        Data.userName = userName;
    }
}
