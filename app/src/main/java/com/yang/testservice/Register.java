package com.yang.testservice;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yang.database.Accounts;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by liu on 2017/7/22.
 */

public class Register extends AppCompatActivity{

    private Button button_reg = null;
    private EditText user = null;
    private EditText passwd = null;
    private EditText confirm = null;
    private EditText tel = null;
    private RadioGroup sex;
    private RadioButton radio = null;
    String TAG = "Register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user = (EditText) findViewById(R.id.editText_user);
        passwd = (EditText) findViewById(R.id.editText_passwd);
        confirm = (EditText) findViewById(R.id.editText_confirm);
        tel = (EditText) findViewById(R.id.editText_tel);
        sex = (RadioGroup) findViewById(R.id.sex);
        button_reg = (Button) findViewById(R.id.button_reg);

        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio = (RadioButton) findViewById(checkedId);
            }
        });

        //点击注册按钮
        button_reg.setOnClickListener(new regOnClickListener());


        //返回按钮
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

        class regOnClickListener implements OnClickListener {
            public void onClick(View v) {

                /*if (accounts.save()) {
                    Toast.makeText(Register.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "存储失败", Toast.LENGTH_SHORT).show();
                }*/

                //Accounts last = DataSupport.findLast(Accounts.class);
                //Toast.makeText(Register.this, last.getDatetime().toString()+last, Toast.LENGTH_SHORT).show();
                if(passwd.getText().toString().equals(confirm.getText().toString()))
                {
                    List<Accounts> accountList = DataSupport.where("username = ?", user.getText().toString()).find(Accounts.class);
                    if (accountList.isEmpty()) {
                        Accounts accounts = new Accounts();
                        accounts.setUsername(user.getText().toString());
                        accounts.setTel(tel.getText().toString());
                        accounts.setPassword(passwd.getText().toString());
                        accounts.setGender(radio.getText().toString());
                        //accounts.setAuthority("123");
                        accounts.setDatetime(new Date());
                        //accounts.setRank(1);
                        //accounts.setUserimage("123");
                        accounts.save();
                        Toast.makeText(Register.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Register.this, "密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        }
}
