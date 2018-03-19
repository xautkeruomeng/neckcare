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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoXixi on 2017/12/1.
 * 从服务端获取使用手机时长
 */

public class getUsePhoneTime {
    private static String URL = "http://10.102.3.101:8080/MapMutilNaviagtion/GetTimeServlet";
    private HttpEntity httpEntity;
    String result;

    public List<Map<String,Object>> HttpPost(final String username){
        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String,Object>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        try{
            List<NameValuePair> parms = new ArrayList<>();
            parms.add(new BasicNameValuePair("userName",username));
            //设置请求参数
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parms,"UTF-8");
            httpPost.setEntity(entity);
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

//            List<Map<String,Object>> lists0 = new ArrayList<Map<String,Object>>();
//            Map<String,Object> map0 = new HashMap<String,Object>();

            //发送POST请求
            HttpResponse response = httpClient.execute(httpPost);
            int ret = response.getStatusLine().getStatusCode();
            if(ret == 200)
            {
                httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                //解析数据
                lists = userInfoJsonTool(result);
            }else {
                httpPost.abort();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return lists;
    }

    public List<Map<String,Object>> userInfoJsonTool(String result){
        List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String,Object> map = new HashMap<String,Object>();
                String usephonetime = jsonObject.getString("usephonetime");
                map.put("usephonetime",usephonetime);
                lists.add(map);
            }
            return lists;
        }catch (Exception e){
            e.printStackTrace();
        }

        return lists;
    }
}
