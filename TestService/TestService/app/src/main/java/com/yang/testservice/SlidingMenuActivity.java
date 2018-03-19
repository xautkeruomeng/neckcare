package com.yang.testservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.yang.View.WaterWaveProgress;
import com.yang.service.LocalService;
import com.yang.service.SampleService;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Color;


/**
 * Created by krm on 2017/5/3.
 */
public class SlidingMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Context context;
    private LocalService myService = null;
    private Intent serviceIntent,sampleServiceIntent,cameraIntent;
    private SampleService sampleService = null;
    private boolean mIsBound = false;
    private boolean sIsBound = false;
    private String tag = "SlidingMenuActivity";
    private EditText defaultDEyes;
    public  float phoneTiltAngle = 0;
    private SensorManager sensorManager = null;
    private ImageView picture;

    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int REQUEST_CROP_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingmenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        WaterWaveProgress bar = (WaterWaveProgress) findViewById(R.id.water_wave_view);
        //bar.setMax(500);
        //bar.setProgressSync(361.8f);
        bar.setmRingColor(Color.parseColor("#66CCCC"));
        //bar.setmWaterColor(Color.parseColor("#66CCCC"));
        bar.setMaxProgress(100);
        bar.setProgress(65);
        //bar.animateWave();
        //bar.getProgress();
        //bar.setWaveSpeed(8);
        //bar.setWaterColor(Color.parseColor("#66CCCC"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        defaultDEyes = (EditText) findViewById(R.id.defaultDEyes);

        System.out.println("MainActivity.onCreate the key is "
                + (savedInstanceState == null ? "空" : savedInstanceState
                .getString("key")));
        System.out
                .println("MainActivity.onCreate getLastNonConfigurationInstance "
                        + (getLastNonConfigurationInstance() != null ? "空"
                        : getLastNonConfigurationInstance()));

        BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
        BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;  //构造位图生成的参数，必须为565
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stat_running, BitmapFactoryOptionsbfo);
        saveFirstPicture(myBitmap);
        // 获取传感器管理器（获得SensorManager对象,返回的就是一个硬件设备的控制器  ）
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 注册监听器（获得特定的传感器 : 方向传感器 ）
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(new SensorEventListener() { //注册传感器事件监听事件
            @Override
            public void onSensorChanged(SensorEvent event) { //创建SensorEventListener监听传感器的值改变并且做出相应的动作
                phoneTiltAngle = event.values[1];  //传感器的值改变调用此方法
                defaultDEyes.setText(phoneTiltAngle+"");
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {  //传感器的精确度改变调用此方法

            }
        }, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void startMyService() {
        serviceIntent = new Intent(SlidingMenuActivity.this, LocalService.class);
        startService(serviceIntent);
    }

    private void startSampleService() {
        sampleServiceIntent = new Intent(SlidingMenuActivity.this,SampleService.class);
        startService(sampleServiceIntent);
        //sampleServiceIntent = new Intent(SlidingMenuActivity.this,TakePhoto.class);
        //startActivity(sampleServiceIntent);
    }

    private void startCameraActivity() {
        cameraIntent = new Intent(SlidingMenuActivity.this,CameraActivity.class);
        startActivity(cameraIntent);
    }
    public void click(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.btnSample:
                // 样本数据采集
                startSampleService();
                doBindSampleService();
                startCameraActivity();
                break;
                /*
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(SlidingMenuActivity.this,
                            "com.yang.testservice.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                break;*/
            /*
            case R.id.btnSampleOver:
                // 样本数据采集结束
                doUnbindSampleService();
                stopService(sampleServiceIntent);
                List<Float> disEyes = sampleService.getEyesList();
                Log.e(tag,disEyes+"");
                // 获取采样结果
                if (disEyes.size() >= 10) {
                    float totalEyesDis = 0;
                    for (int i=0;i<disEyes.size();i++) {
                        totalEyesDis += disEyes.get(i);
                    }
                }else {
                    AlertDialog alert = new AlertDialog.Builder(SlidingMenuActivity.this).create();
                    alert.setIcon(android.R.drawable.ic_dialog_info);
                    alert.setTitle("注意"); // 设置对话框的标题
                    alert.setMessage("采样数据小于10组，请重新采集样本数据!"); // 设置要显示的内容

                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    alert.show(); // 创建对话框并显示
                }
                break;*/
            case R.id.button1:
                // 开启服务
                startMyService();
                doBindService();
                break;
            case R.id.button2:
                // 停止服务
                doUnbindService();
                stopService(serviceIntent);
                break;
            case R.id.button3:
                // 后台运行
                SlidingMenuActivity.this.finish();
                break;
            default:
                break;
        }

    }
   /*
    public void startPhotoZoom(Uri uri, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", width/height);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:返回uri，false：不返回uri
        // 同一个地址下 裁剪的图片覆盖之前得到的图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }*/
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            break;
                /*
                if(resultCode == RESULT_OK) {
                    //try {
                        //startPhotoZoom(imageUri, 256, 256);
                        //picture = (ImageView)findViewById(R.id.picture);
                        //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //picture.setImageBitmap(bitmap);
                    //} //catch (FileNotFoundException e) {
                        //e.printStackTrace();
                    //}
                }
                break;*/
            /*case REQUEST_CROP_PHOTO:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    picture = (ImageView)findViewById(R.id.picture);
                    //Bitmap photo = extras.getParcelable("data");
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    //把图片显示到ImgeView
                    picture.setImageBitmap(photo);

                }
                break;/
            default:
                break;
        }

    }*/


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

        if (id == R.id.nav_changeuser) {
            // Handle the camera action
        } else if (id == R.id.nav_mymedal) {

        } else if (id == R.id.nav_mygrade) {

        } else if (id == R.id.nav_video) {

        } else if (id == R.id.nav_myset) {

        } else if (id == R.id.nav_aboutus) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = ((LocalService.LocalBinder) service).getService();
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
        Log.e("","新建文件夹！");
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
        }
        String photoFile = "Picture" + ".jpg";
        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File file = new File(filename);
        System.out.println("firstFileName is "+ filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                Log.e("","图片已保存！");
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
}
