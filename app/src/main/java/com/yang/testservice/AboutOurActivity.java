package com.yang.testservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.model.SliceValue;

public class AboutOurActivity extends AppCompatActivity {
    private Intent backIntent;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private int[] data = {3,20};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutour);
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbartwo);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backIntent = new Intent(AboutOurActivity.this, SlidingMenuActivity.class);
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
                Intent intent = new Intent(AboutOurActivity.this, SlidingMenuActivity.class);
                startActivity(intent);
                break;

            case R.id.index2:
                // 我的勋章
                Intent myGainIntent = new Intent(AboutOurActivity.this, MyGain.class);
                startActivity(myGainIntent);
                break;
            case R.id.index3:
                // 我的成绩
                Intent myScoreIntent = new Intent(AboutOurActivity.this, MyScoreActivity.class);
                startActivity(myScoreIntent);
                break;
            case R.id.index4:
                // 视频教程
               /* Intent myVedioIntent = new Intent(MyHealthVedio.this, MyHealthVedio.class);
                startActivity(myVedioIntent);*/
                break;
            default:
                break;
        }
    }
}
