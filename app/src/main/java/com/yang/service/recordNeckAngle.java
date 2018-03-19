package com.yang.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaoXixi on 2017/11/28.
 * 上传颈部弯曲角度至服务器
 */

public class recordNeckAngle {
    private static String URL = "http://10.102.3.101:8080/MapMutilNaviagtion/RecordNeckAngleServlet";
    private HttpEntity httpEntity;

    public boolean httpPost(String neckAngle,String userName){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        List<NameValuePair> parms = new ArrayList<>();
        parms.add(new BasicNameValuePair("neckAngle",neckAngle));
        parms.add(new BasicNameValuePair("userName",userName));
        //设置请求参数
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(parms, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entity);

        // 请求超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);

        //发送POST请求
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int ret = response.getStatusLine().getStatusCode();
            if(ret == 200){
                httpEntity = response.getEntity();

                String value = null;
                try {
                    value = EntityUtils.toString(httpEntity,"utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if("true".equals(value)){
                    return true;
                }
            }else {
                httpPost.abort();
            }
            return false;
        }catch (NullPointerException e){

        }
       return false;
    }
}
