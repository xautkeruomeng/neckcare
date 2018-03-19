package com.yang.testservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by krm on 2017/6/27.
 */

public class consultActivity extends AppCompatActivity {

    private Intent backIntent;
    private TextView tutu,huida;
    private EditText text;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbartwo);
        setSupportActionBar(mToolbarTb);

        tutu = (TextView) findViewById(R.id.tutu);
        huida = (TextView) findViewById(R.id.huida);
        send = (Button) findViewById(R.id.send);
        text = (EditText) findViewById(R.id.text);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutu.setText("图图 回复 陶秀医生 发表于[2017/5/23 14:54:03]");
                huida.setText(text.getText());
                text.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backIntent = new Intent(consultActivity.this, SlidingMenuActivity.class);
                startActivity(backIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}