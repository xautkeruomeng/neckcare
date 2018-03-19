package com.yang.service;

import android.Manifest;
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

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;

import android.media.FaceDetector;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.yang.handle.PhotoHandler;
import com.yang.testservice.R;
import com.yang.testservice.SlidingMenuActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.util.Size;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.content.pm.PackageManager;
import android.view.Surface;
import java.io.File;
import android.os.Environment;
import java.nio.ByteBuffer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.view.TextureView;
import android.hardware.camera2.CameraAccessException;
import android.os.Handler;
import android.os.HandlerThread;


import android.support.annotation.NonNull;

// 样本数据采集
// 要求：采集10组及10组以上数据，否则提示采样数据太少，重新采集
// 将 默认两眼之间距离 和 嘴巴的长度 返回给MainActivity

public class SampleService extends Service {

   private AlarmManager am = null;
    protected CameraDevice cameraDevice; //定义代表摄像头的成员变量，代表系统摄像头
    //private Camera camera;
    private Button takePictureButton;
    private String cameraId;   // 摄像头ID，一般0是后视，1是前视
    protected CameraCaptureSession cameraCaptureSessions; // 定义CameraCaptureSession成员变量，是一个拍摄绘话的类，用来从摄像头拍摄图像或是重新拍摄图像
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension; //预览尺寸
    private ImageReader imageReader;
    public static final int TAKE_PHOTO = 1;
    private final IBinder mBinder = new LocalBinder();

    private NotificationManager mNM;

    private int NOTIFICATION = R.string.sample_service_started;
    private Uri imageUri;


    static final String tag = "SampleService";
    private static final String TAG = " dragon camera test";
    //请求码常量，可以自定义
    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

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
        // 通知栏
        showNotification();
        //init();

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
        //cancelAlertManager();

        //if (cameraDevice != null) {
           // cameraDevice.close();
           //cameraDevice = null;
        //}
        //if (null != imageReader) {
            //mageReader.close();
            //imageReader = null;
        //}

        Toast.makeText(this, R.string.sample_service_stopped, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }


     // Show a notification while this service is running.

    private void showNotification() {
        CharSequence text = getText(R.string.sample_service_started);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SlidingMenuActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.stat_running)
                .setContentTitle(text)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
        mNM.notify(1,notification);
    }

    /*private void init() {
        am = (AlarmManager) getSystemService(ALARM_SERVICE);  // AlarmManager:在特定的时刻广播一个指定的Intent

        openFacingFrontCamera();   // 打开前置摄像头
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.vegetables_source.alarm");
        registerReceiver(alarmReceiver, filter);

        Intent intent = new Intent();
        intent.setAction("com.vegeta     bles_source.alarm");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);  // Intent的封装包

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 5, pi);// 马上开始，每5秒钟触发一次
    }

    // 打开前置摄像头
    private void openFacingFrontCamera() {
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
                //startActivityForResult(intent,TAKE_PHOTO);
                startService(intent,TAKE_PHOTO);

    }
    @Override
    protected void onHandleIntent(int requestCode, int resultCode,Intent data)
    {
        switch (requestCode){
            case TAKE_PHOTO:
                //if(resultCode == RESULT_OK)
                try{
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                }
                catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }*/
    //----------------------------------------------------------------------------------------
    /*
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
    }*/


}