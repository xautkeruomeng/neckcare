package com.yang.testservice;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by krm on 2017/6/7.
 */
public class ParentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity);
    }
}