package com.yang.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.media.FaceDetector;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.handle.PhotoHandler;
import com.yang.testservice.MainActivity;
import com.yang.testservice.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 样本数据采集
// 要求：采集10组及10组以上数据，否则提示采样数据太少，重新采集
// 将 默认两眼之间距离 和 嘴巴的长度 返回给MainActivity

public class SampleService extends Service {

    private AlarmManager am = null;
    private Camera camera;

    private final IBinder mBinder = new LocalBinder();

    private NotificationManager mNM;

    private int NOTIFICATION = R.string.sample_service_started;


    static final String tag = "SampleService";

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SampleService getService() {
            return SampleService.this;
        }

    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        init();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mNM.cancel(NOTIFICATION);
        // 注销广播
        cancelAlertManager();

        if (camera != null) {
            camera.release();
            camera = null;
        }

        Toast.makeText(this, R.string.sample_service_stopped, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.sample_service_started);

        Notification notification = new Notification(R.drawable.stat_running,
                text, System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        //notification.setLatestEventInfo(this,
              //  getText(R.string.sample_service_label), text, contentIntent);

        mNM.notify(NOTIFICATION, notification);
    }

    private void init() {
        am = (AlarmManager) getSystemService(ALARM_SERVICE);  // AlarmManager:在特定的时刻广播一个指定的Intent

        camera = openFacingFrontCamera();   // 打开前置摄像头
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vegetables_source.alarm");
        registerReceiver(alarmReceiver, filter);

        Intent intent = new Intent();
        intent.setAction("com.vegetables_source.alarm");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);  // Intent的封装包

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 5, pi);// 马上开始，每5秒钟触发一次
    }

    // 打开前置摄像头
    private Camera openFacingFrontCamera() {
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        return cam;
    }


    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.vegetables_source.alarm".equals(intent.getAction())) {

                if (camera != null) {
                    SurfaceView dummy = new SurfaceView(getBaseContext());
                    try {
                        camera.setPreviewDisplay(dummy.getHolder());     // 设置显示
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    camera.startPreview();            // 开始预览
                    camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
                    // 创建vibrator对象
                    Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    boolean getvib = vibrator.hasVibrator();// 检测vibrator是否存在
                    Log.e(tag,"获取临界值");
                    detectFace();
                }
            }
        }
    };


    // 旋转图片
    public Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public boolean checkFace(Rect rect){
        int w = rect.width();
        int h = rect.height();
        int s = w*h;
        Log.i(tag, getResources().getString(R.string.face_w)+ w + getResources().getString(R.string.face_h) + h + getResources().getString(R.string.face_s) + s);
        if(s < 10000){
            Log.i(tag, getResources().getString(R.string.invaild));
            return false;
        }
        else{
            Log.i(tag, getResources().getString(R.string.vaild));
            return true;
        }
    }

    FaceDetector faceDetector = null;
    Bitmap srcImg = null;
    Bitmap srcFace = null;
    FaceDetector.Face[] face;
    final int N_MAX = 5;
    float dis = 0;
    int j = 0;
    List<Float> disEyes = new ArrayList<Float>();

    public void detectFace(){

        srcImg = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/ServiceCamera/Picture.jpg");
        srcImg = rotaingImageView(-90,srcImg);   // 原始图片旋转90度
        this.srcFace = srcImg.copy(Bitmap.Config.RGB_565, true);
        int w = srcFace.getWidth();
        int h = srcFace.getHeight();
        Log.i(tag, getResources().getString(R.string.image_w) + w + getResources().getString(R.string.image_h) + h);
        faceDetector = new FaceDetector(w, h, N_MAX);
        face = new FaceDetector.Face[N_MAX];
        int nFace = faceDetector.findFaces(srcFace, face);   // 检测到人脸数
        Log.e(tag, getResources().getString(R.string.face_number) + nFace);
        if (nFace == 1){
            for(int i=0; i<nFace; i++){
                FaceDetector.Face f  = face[i];
                PointF midPoint = new PointF();
                dis = f.eyesDistance();         // 两眼之间的距离
                f.getMidPoint(midPoint);
                int dd = (int)(dis);
                Point eyeLeft = new Point((int)(midPoint.x - dis/2), (int)midPoint.y);
                Point eyeRight = new Point((int)(midPoint.x + dis/2), (int)midPoint.y);
                Rect faceRect = new Rect((int)(midPoint.x - dd), (int)(midPoint.y - dd), (int)(midPoint.x + dd), (int)(midPoint.y + dd));
                Log.i(tag, getResources().getString(R.string.left_eye) + eyeLeft.x + getResources().getString(R.string.right_eye) + eyeLeft.y);
                Log.e("两眼之间距离",dis+"");
                Log.e(tag,"j="+ j);
                disEyes.add(dis);
                Log.e("disEyes["+j+"]",disEyes.get(j)+"");
                j++;
            }
        }
    }

    public List getEyesList(){
        return disEyes;
    }

    private void cancelAlertManager() {
        Intent intent = new Intent();
        intent.setAction("com.vegetables_source.alarm");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.cancel(pi);
        // 注销广播
        unregisterReceiver(alarmReceiver);
    }
}