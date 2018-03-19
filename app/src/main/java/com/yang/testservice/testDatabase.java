package com.yang.testservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yang.database.Accounts;
import org.litepal.LitePal;
import org.litepal.exceptions.DataSupportException;

import java.util.Date;

/**
 * Created by liu on 2017/7/22.
 */

public class testDatabase extends AppCompatActivity {
    String TAG = "testDatabase";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        Button createDB = (Button)findViewById(R.id.create_db);
        createDB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG,"11111111111111");

                LitePal.getDatabase();
            }
        });
        Button createDB_plus = (Button)findViewById(R.id.create_db_plus);
        createDB_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Accounts accounts = new Accounts();

                //try {
                    Log.i(TAG, "111111112222222222");
                    //accounts.setId(1);
                    accounts.setUsername("Lily");
                    accounts.setTel("123");
                    accounts.setPassword("123");
                    accounts.setGender("123");
                    accounts.setAuthority("123");
                    accounts.setDatetime(new Date());
                    accounts.setRank(1);
                    accounts.setUserimage("123");
                    accounts.saveThrows();
               /* }catch(DataSupportException e)
                {
                    Log.d("TAG", "news id is " + accounts.getTel());
                }*/


                //news.save();

                Log.i(TAG,"111111133333333333");
                //news.save();
                if (accounts.save()) {
                    Toast.makeText(testDatabase.this, "存储成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(testDatabase.this, "存储失败", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG,"114444444444444444");

            }
        });
    }
}
