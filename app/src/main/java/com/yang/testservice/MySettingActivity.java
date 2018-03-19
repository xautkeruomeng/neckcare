package com.yang.testservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MySettingActivity extends AppCompatActivity {

    private Intent backIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting);

        // 返回箭头
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbartwo);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MySettingActivity.this,SlidingMenuActivity.class);
                startActivity(intent);
            }
        });
    }
    public void change(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this,AboutOurActivity.class);
        startActivity(intent);
    }

    public void table(View view){
        Intent intent = new Intent();
        intent.setClass(this,MyHealthPlan.class);
        startActivity(intent);
    }

    public void changee(View view){
        Intent intent = new Intent();
        intent.setClass(this,PasswordActivity.class);
        startActivity(intent);
    }

    public void alter(View view){
        Intent intent = new Intent();
        intent.setClass(this,LoginActivity.class);
        startActivity(intent);
    }

}
