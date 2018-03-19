package com.yang.testservice;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * Created by krm on 2017/5/2.
 */
public class Huzhiyong extends Activity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hzy);

        //tv = (TextView) findViewById(R.id.textView1);

        String wserviceName = Context.WIFI_SERVICE;
        WifiManager wm = (WifiManager) getSystemService(wserviceName);

        // wm.startScan();
        List<ScanResult> results = wm.getScanResults();
        String otherwifi = "";

        // String otherwifi = "The existing network is: \n\n";
        for (ScanResult result : results) {

            otherwifi += result.SSID + " " + result.BSSID +" " + result.level+ "\n";
            //otherwifi += result.BSSID + "  " + result.level + "\n";
            // otherwifi += result.SSID + "\n";
        }

        //tv.setText(otherwifi);
        Log.e("ss",otherwifi);
        // TODO Auto-generated method stub
    }

    public String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

}
