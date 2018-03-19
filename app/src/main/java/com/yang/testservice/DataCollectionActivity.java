package com.yang.testservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v7.widget.Toolbar;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.List;
import android.util.Log;
import com.yang.service.LocalService;
import com.yang.service.SampleService;
import com.yang.service.StartService;

public class DataCollectionActivity extends AppCompatActivity {
    private SampleService sampleService = null;
    private Context context;
    private LocalService myService = null;
    private StartService myStartService = null;
    private Intent serviceIntent,sampleServiceIntent;
    private boolean mIsBound = false;
    private boolean sIsBound = false;
    private String tag = "DataCollectionActivity";
    private  float averagelEyesDis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacollection);

        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbarplan);
        setSupportActionBar(mToolbarTb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* finish();*/
                Intent intent=new Intent(DataCollectionActivity.this,SlidingMenuActivity.class);
                startActivity(intent);
            }
        });
    }
    public void click(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.btnSample:
                // 样本数据采集
                startSampleService();
                doBindSampleService();
                break;
            case R.id.btnSampleOver:
                // 样本数据采集结束
                doUnbindSampleService();
                stopService(sampleServiceIntent);
                List<Float> disEyes = sampleService.getEyesList();
                Log.e(tag, disEyes + "");
                // 获取采样结果
                if (disEyes.size() >= 5) {
                    float totalEyesDis = 0;
                    for (int i = 0; i < disEyes.size(); i++) {
                        totalEyesDis += disEyes.get(i);
                    }
                    Log.e(tag, totalEyesDis + "");
                    averagelEyesDis = totalEyesDis / disEyes.size();    // 样本采集阶段平均两眼之间距离
                    Log.e(tag, averagelEyesDis + "");
                } else {
                    AlertDialog alert = new AlertDialog.Builder(DataCollectionActivity.this).create();
                    alert.setIcon(android.R.drawable.ic_dialog_info);
                    alert.setTitle("注意"); // 设置对话框的标题
                    alert.setMessage("采样数据小于5组，请重新采集样本数据!"); // 设置要显示的内容
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    alert.show(); // 创建对话框并显示
                }
                break;
        }
    }
    private void startSampleService() {
        sampleServiceIntent = new Intent(DataCollectionActivity.this,SampleService.class);
        startService(sampleServiceIntent);
    }
    void doBindService() {
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    void doUnbindService() {
        if (mIsBound) {
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }

    void doBindSampleService() {
        bindService(sampleServiceIntent, serviceSampleConnection, Context.BIND_AUTO_CREATE);
        sIsBound = true;
    }
    void doUnbindSampleService() {
        if (sIsBound) {
            unbindService(serviceSampleConnection);
            sIsBound = false;
        }
    }
    ServiceConnection serviceSampleConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            sampleService = ((SampleService.LocalBinder) service).getService();
        }
        public void onServiceDisconnected(ComponentName name) {
            sampleService = null;
        }
    };
    ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            myStartService = ((StartService.LocalBinder) service).getService();
        }
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };
}

