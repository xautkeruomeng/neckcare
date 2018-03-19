package com.yang.testservice;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.data.Data;
import com.yang.service.LocalService;
import com.yang.service.SampleService;
import com.yang.service.StartService;
import com.yang.service.recordNeckAngle;
import com.yang.service.recordTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;



// 获取时间差 引入命名空间
import java.util.Date;

/**
 * Created by krm on 2017/5/3.
 */
public class SlidingMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private LocalService myService = null;
    private StartService myStartService = null;
    private Intent serviceIntent, sampleServiceIntent, myGainIntent, myHealthIntent, myScoreIntent, myVedioIntent, mydb,AboutOurIntent,dataCollectionIntent,mySeetingIntent;
    private SampleService sampleService = null;
    private StartService startService;
    private boolean mIsBound = false;
    private boolean sIsBound = false;
    private String tag = "SlidingMenuActivity";

    private float averagelEyesDis = 0;
    private PieChartView pieChart;
    private PieChartData pieChardata;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private int[] data = {5, 19};

    private TextView totalTime, averageAngle, maxAverage;
    private Button button1;
    static long diff;
    static Date curDate;

    static List<Double> neckAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingmenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        totalTime = (TextView) findViewById(R.id.totalTime);
        averageAngle = (TextView) findViewById(R.id.averageAngle);
        maxAverage = (TextView) findViewById(R.id.maxAverage);
        startService = new StartService();
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
        BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;  //构造位图生成的参数，必须为565
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stat_running, BitmapFactoryOptionsbfo);
        saveFirstPicture(myBitmap);

        // 绘制饼状图
        pieChart = (PieChartView) findViewById(R.id.pie_chart);
        pieChart.setOnValueTouchListener(selectListener);//设置点击事件监听
        setPieChartData();
        initPieChart();
    }

    // 绘制饼状图

    /**
     * 获取数据
     */
    private void setPieChartData() {
        SliceValue sliceValue = new SliceValue((float) data[0], getResources().getColor(R.color.colorLightRed));
        values.add(sliceValue);
        SliceValue sliceValue1 = new SliceValue((float) data[1], getResources().getColor(R.color.colorBlackGreen));
        values.add(sliceValue1);

       /* for (int i = 0; i < data.length; ++i) {
            //SliceValue sliceValue = new SliceValue((float) data[i], Utils.pickColor());//这里的颜色是我写了一个工具类 是随机选择颜色的
            SliceValue sliceValue = new SliceValue((float) data[i],Color.GREEN);
            values.add(sliceValue);
        }*/
    }


    /**
     * 初始化
     */
    private void initPieChart() {
        pieChardata = new PieChartData();
        pieChardata.setHasLabels(true);//显示表情
        pieChardata.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        pieChardata.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieChardata.setHasCenterCircle(true);//是否是环形显示
        pieChardata.setValues(values);//填充数据
        pieChardata.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieChardata.setCenterCircleScale(0.65f);//设置环形的大小级别
        pieChardata.setCenterText1("5小时");//环形中间的文字1
        pieChardata.setCenterText1Color(Color.BLACK);//文字颜色
        pieChardata.setCenterText1FontSize(25);//文字大小

       /* pieChardata.setCenterText2("饼图测试");
        pieChardata.setCenterText2Color(Color.BLACK);
        pieChardata.setCenterText2FontSize(18);*/
        /**这里也可以自定义你的字体   Roboto-Italic.ttf这个就是你的字体库*/
//      Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
//      data.setCenterText1Typeface(tf);

        pieChart.setPieChartData(pieChardata);
        pieChart.setValueSelectionEnabled(false);//选择饼图某一块变大
        pieChart.setAlpha(0.9f);//设置透明度
        pieChart.setCircleFillRatio(1f);//设置饼图大小

    }

    /**
     * 监听事件
     */
    private PieChartOnValueSelectListener selectListener = new PieChartOnValueSelectListener() {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub
        }

        @Override
        public void onValueSelected(int arg0, SliceValue value) {
            // TODO Auto-generated method stub
            Toast.makeText(SlidingMenuActivity.this, "Selected: " + value.getValue(), Toast.LENGTH_SHORT).show();
        }
    };

    public void click(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.button1:
                // 开启服务
                startService();
                doBindService();
                // 获取开始时间
                curDate = new Date(System.currentTimeMillis());
                break;
            case R.id.button2:
                Date endDate = new Date(System.currentTimeMillis());  // 获取结束时间
                // 停止服务
                doUnbindService();
                stopService(serviceIntent);
                neckAngle = startService.getAnglesList();  //获取颈部弯曲角度数组

                final long diff = (endDate.getTime() - curDate.getTime())/36000;   // 时间差
                totalTime.setText(String.valueOf(diff));      //使用手机时长

                final Double average = Math.ceil(getAverage(neckAngle)/60);
                averageAngle.setText(Double.toString(average));   // 平均颈部弯曲角度

                final Double max = Math.ceil(getMax(neckAngle)/60);
                maxAverage.setText(Double.toString(max));    //最大颈部弯曲角度

                /**获取用户名*/
                final String userName = Data.getUserName();
                /**上传使用手机时长、平均颈部弯曲角度、最大颈部弯曲角度*/
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recordTime RecordTime = new recordTime();
                        RecordTime.httpPost(String.valueOf(diff),userName,String.valueOf(average),String.valueOf(max));
                    }
                });
                thread.start();

                break;
            case R.id.button3:
                // 后台运行
                SlidingMenuActivity.this.finish();
                break;
            case R.id.index1:
                // 首页
             /*   Intent intent = new Intent(SlidingMenuActivity.this,ShouYeActivity.class);
                startActivity(intent);*/
                break;
            case R.id.index2:
                // 我的勋章
                myGainIntent = new Intent(SlidingMenuActivity.this, MyGain.class);
                startActivity(myGainIntent);
                break;
            case R.id.index3:
                // 我的成绩
                myScoreIntent = new Intent(SlidingMenuActivity.this, MyScoreActivity.class);
                startActivity(myScoreIntent);
                break;
            case R.id.index4:
                // 视频教程
                AboutOurIntent = new Intent(SlidingMenuActivity.this, AboutOurActivity.class);
                startActivity(AboutOurIntent);
                break;
            default:
                break;
        }
    }

/*    private void startMyService() {
        serviceIntent = new Intent(SlidingMenuActivity.this, LocalService.class);
        startService(serviceIntent);
    }*/

    private void startService() {
        serviceIntent = new Intent(SlidingMenuActivity.this, StartService.class);
        if (averagelEyesDis != 0) {
            serviceIntent.putExtra("averageDis", averagelEyesDis);
        } else {
            serviceIntent.putExtra("averageDis", 100);
        }
        startService(serviceIntent);
    }

    private void startSampleService() {
        sampleServiceIntent = new Intent(SlidingMenuActivity.this, SampleService.class);
        startService(sampleServiceIntent);
    }

    void doBindService() {
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;

    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
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
            // Detach our existing connection.
            unbindService(serviceSampleConnection);
            sIsBound = false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.consult) {      // 我的勋章

            Intent consultIntent = new Intent(SlidingMenuActivity.this, consultActivity.class);
            startActivity(consultIntent);

        }/*else if (id == R.id.my_db) {   // 数据库
            mydb = new Intent(SlidingMenuActivity.this, testDatabase.class);
            startActivity(mydb);

        }else if (id == R.id.my_score) {   // 我的成绩

            myScoreIntent = new Intent(SlidingMenuActivity.this, MyScoreActivity.class);
            startActivity(myScoreIntent);

        } else if (id == R.id.consult) {   // 在线咨询

            Intent consultIntent = new Intent(SlidingMenuActivity.this, consultActivity.class);
            startActivity(consultIntent);

        }*/ else if (id == R.id.my_health) {   // 图文教程

            myHealthIntent = new Intent(SlidingMenuActivity.this, MyHealthPlan.class);
            startActivity(myHealthIntent);

        }
        else if (id == R.id.my_health_video) {   // 视频教程

            myVedioIntent = new Intent(SlidingMenuActivity.this, MyHealthVedio.class);
            startActivity(myVedioIntent);

        }
        else if (id == R.id.my_friendsRank) {   // 好友排行

            Intent friendsRankIntent = new Intent(SlidingMenuActivity.this, friendsRankAcitivity.class);
            startActivity(friendsRankIntent);

        } else if (id == R.id.my_settings) {   // 个人设置
            mySeetingIntent = new Intent(SlidingMenuActivity.this, MySettingActivity.class);
            startActivity(mySeetingIntent);

        } else if (id == R.id.users_manage) {  // 切换用户
            myHealthIntent = new Intent(SlidingMenuActivity.this, LoginActivity.class);
            startActivity(myHealthIntent);
        }/* else if (id == R.id.about_ours) {   // 关于我们
            AboutOurIntent = new Intent(SlidingMenuActivity.this,AboutOurActivity.class);
            startActivity(AboutOurIntent);

        }*/ else if (id == R.id.nav_share) {   // 分享好评
           /* myShareIntent = new Intent(SlidingMenuActivity.this, MyShareActivity.class);
            startActivity(myShareIntent);*/

        } else if (id == R.id.nav_send) {   // 采集数据
            dataCollectionIntent = new Intent(SlidingMenuActivity.this, DataCollectionActivity.class);
            startActivity(dataCollectionIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            myStartService = ((StartService.LocalBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }

    };

    ServiceConnection serviceSampleConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            sampleService = ((SampleService.LocalBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            sampleService = null;
        }

    };

    // 保存路径为："/storage/emulated/0/Pictures/ServiceCamera/Picture.jpg"的照片
    public void saveFirstPicture(Bitmap bitmap) {
        File pictureFileDir = getDir();
        Log.e("", "新建文件夹！");
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
        }
        String photoFile = "Picture" + ".jpg";
        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File file = new File(filename);
        System.out.println("firstFileName is " + filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                Log.e("", "图片已保存！");
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "ServiceCamera");
    }


    private static double getMax(List<Double> array) {
        double Max = array.get(0);
        for (int i = 0; i < array.size(); i++) {
            if (Max < array.get(i)) {
                Max = array.get(i);
            }
        }
        return Max;
    }

    private static double getAverage(List<Double> array) {
        double total = 0;
        for (int i = 0; i < array.size(); i++) {
            total = array.get(i) + total;
        }
        return total / array.size();
    }
    }







