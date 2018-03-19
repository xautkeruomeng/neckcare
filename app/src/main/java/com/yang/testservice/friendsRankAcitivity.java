package com.yang.testservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by krm on 2017/6/27.
 */
public class friendsRankAcitivity extends AppCompatActivity {

    private Intent backIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsrank);

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
                backIntent = new Intent(friendsRankAcitivity.this, SlidingMenuActivity.class);
                startActivity(backIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}