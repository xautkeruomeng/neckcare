package com.yang.testservice;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.data.Data;
import com.yang.service.getUsePhoneTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by krm on 2017/6/7.
 */
public class MyGain extends AppCompatActivity {

    private Intent backIntent;
    int[] timeDay = new int[8];
    static int totalTime = 0;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    TextView suggestion;
    TextView plan;
    boolean is = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygain);
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbartwo);
        setSupportActionBar(mToolbarTb);

        init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**获取用户每天使用手机的时间*/
        /**获取用户名*/
        final String userName = Data.getUserName();
//        final int[] totalTime = {0};
        /**获取使用时长*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listDay = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listMonth = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listYear = new ArrayList<Map<String, Object>>();

                getUsePhoneTime getUserPhoneTime = new getUsePhoneTime();
                lists = getUserPhoneTime.HttpPost(userName);
                JSONArray jsonArray = new JSONArray(lists);

                Message msg = new Message();

                for (int i = 0; i < 8; i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        timeDay[i] = Integer.parseInt(jsonObject.getString("usephonetime"));

                        totalTime +=timeDay[i];

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*if (totalTime[0] != 0){
                    is = true;
                }*/
                if(totalTime != 0){
                    msg.what = 1;
                }
                handler.sendMessage(msg);
            }
        });
        thread.start();

        /*if(is){
            setStar(totalTime[0]);
        }*/
    }

    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e("星级", Integer.toString(totalTime));
                    setStar(totalTime);
                    break;
                default:
                    break;
            }
        }
    };

    private void init() {
        img1 = (ImageView) findViewById(R.id.imgStar1);
        img2 = (ImageView) findViewById(R.id.imgStar2);
        img3 = (ImageView) findViewById(R.id.imgStar3);
        img4 = (ImageView) findViewById(R.id.imgStar4);
        img5 = (ImageView) findViewById(R.id.imgStar5);
        suggestion = (TextView) findViewById(R.id.suggestion);
        plan = (TextView) findViewById(R.id.plan);
    }

    private void setStar(int totalTime) {
//        Toast.makeText(MyGain.this,totalTime,Toast.LENGTH_LONG).show();
        //使用手机时间少于两个小时，为5颗星
        Log.d("星级", Integer.toString(totalTime));
        if(totalTime <= 2 && totalTime > 0){
            img1.setImageResource(R.drawable.star);
            img2.setImageResource(R.drawable.star);
            img3.setImageResource(R.drawable.star);
            img4.setImageResource(R.drawable.star);
            img5.setImageResource(R.drawable.star);
            suggestion.setText("您的健康指数为五颗星，代表着您处于高等状态。您的健康状态较好，希望您能够继续保持。");
            plan.setText("先做立正姿势，两脚稍分开，两手撑腰。练习时头、颈先向右转，双目向右后方看，头颈再向左转，双目向左后方看，还原至预备姿势，低头看地，以下颌能触及胸骨柄为佳，在次还原。动作宜缓慢进行，以呼吸一次做一个动作为宜。");
        }
        //使用手机时间在两小时到四小时之间，为4颗星
        if(totalTime > 2 && totalTime <= 4){
            img1.setImageResource(R.drawable.star);
            img2.setImageResource(R.drawable.star);
            img3.setImageResource(R.drawable.star);
            img4.setImageResource(R.drawable.star);
            img5.setImageResource(R.drawable.nostar);
            suggestion.setText("您的健康指数为四颗星，代表着您处于中等偏上状态。您的健康状态较好，希望您能够继续保持。");
            plan.setText("先做立正姿势，两脚稍分开，两手撑腰。练习时头、颈先向右转，双目向右后方看，头颈再向左转，双目向左后方看，还原至预备姿势，低头看地，以下颌能触及胸骨柄为佳，在次还原。动作宜缓慢进行，以呼吸一次做一个动作为宜。");
        }
        //使用手机时间在四小时到六小时之间，为3颗星
        if(totalTime > 4 && totalTime <= 6){
            img1.setImageResource(R.drawable.star);
            img2.setImageResource(R.drawable.star);
            img3.setImageResource(R.drawable.star);
            img4.setImageResource(R.drawable.nostar);
            img5.setImageResource(R.drawable.nostar);
            suggestion.setText("您的健康指数为三颗星，代表着您处于中等状态。您的健康状态较好，希望您能够继续保持。");
            plan.setText("先做立正姿势，两脚稍分开，两手撑腰。练习时头、颈先向右转，双目向右后方看，头颈再向左转，双目向左后方看，还原至预备姿势，低头看地，以下颌能触及胸骨柄为佳，在次还原。动作宜缓慢进行，以呼吸一次做一个动作为宜。");
        }
        //使用手机时间在六小时到八小时之间，为2颗星
        if(totalTime > 6 && totalTime <= 8){
            img1.setImageResource(R.drawable.star);
            img2.setImageResource(R.drawable.star);
            img3.setImageResource(R.drawable.nostar);
            img4.setImageResource(R.drawable.nostar);
            img5.setImageResource(R.drawable.nostar);
            suggestion.setText("您的健康指数为两颗星，代表着您处于中等偏下状态。这对您的健康有很大的威胁，因为长时间低头会使颈部神经和血管受到挤压，为了您的健康，请多多抬头。");
            plan.setText("先做立正姿势，两脚稍分开，两手撑腰。练习时头、颈先向右转，双目向右后方看，头颈再向左转，双目向左后方看，还原至预备姿势，低头看地，以下颌能触及胸骨柄为佳，在次还原。动作宜缓慢进行，以呼吸一次做一个动作为宜。");
        }
        //使用手机时间大于八小时，为1颗星
        if(totalTime > 8){
            img1.setImageResource(R.drawable.star);
            img2.setImageResource(R.drawable.nostar);
            img3.setImageResource(R.drawable.nostar);
            img4.setImageResource(R.drawable.nostar);
            img5.setImageResource(R.drawable.nostar);
            suggestion.setText("您的健康指数为一颗星，代表着您处于低等状态。这对您的健康有很大的威胁，因为长时间低头会使颈部神经和血管受到挤压，为了您的健康，请多多抬头。");
            plan.setText("先做立正姿势，两脚稍分开，两手撑腰。练习时头、颈先向右转，双目向右后方看，头颈再向左转，双目向左后方看，还原至预备姿势，低头看地，以下颌能触及胸骨柄为佳，在次还原。动作宜缓慢进行，以呼吸一次做一个动作为宜。");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backIntent = new Intent(MyGain.this, SlidingMenuActivity.class);
                startActivity(backIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void click(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.index1:
                // 首页
                Intent intent = new Intent(MyGain.this, SlidingMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.index2:
                // 我的勋章
               /* Intent myGainIntent = new Intent(MyGain.this, MyGain.class);
                startActivity(myGainIntent);*/
                break;
            case R.id.index3:
                // 我的成绩
                Intent myScoreIntent = new Intent(MyGain.this, MyScoreActivity.class);
                startActivity(myScoreIntent);
                break;
            case R.id.index4:
                // 关于我们
                Intent AboutOurIntent = new Intent(MyGain.this, AboutOurActivity.class);
                startActivity(AboutOurIntent);
                break;
            default:
                break;
        }
    }
}
