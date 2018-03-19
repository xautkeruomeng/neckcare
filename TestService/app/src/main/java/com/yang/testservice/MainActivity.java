package com.yang.testservice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.yang.testservice.R;
import com.yang.service.LocalService;
import com.yang.service.SampleService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity{
	private Intent serviceIntent,sampleServiceIntent;
	private LocalService myService = null;
	private SampleService sampleService = null;

	private String str = "Hello";
	private Context context;

	private boolean mIsBound = false;
	private boolean sIsBound = false;
	private AlarmManager am = null;
	private Camera camera;
	private String tag = "MainActivity";
	private SensorManager sensorManager = null;
    private Toolbar toolbar;

	private EditText isOrNo,defaultDEyes,faceNum,DEyes,faceAngle,phoneAngle,focus,neckAngle;
	public  float phoneTiltAngle = 0;
	// 保证在活动被回收前调用
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		System.out.println("MainActivity.onSaveInstanceState()");
		str = "Hello World";
		outState.putString("key", str);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		System.out.println("MainActivity.onSaveInstanceState the key is "
				+ savedInstanceState == null ? "空" : savedInstanceState
				.getString("key"));
	}
/*
    @Override
	 public Object onRetainNonConfigurationInstance() {
		return str;
	}*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);



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

		// 获取传感器管理器
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// 注册监听器
		Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				phoneTiltAngle = event.values[1];
				defaultDEyes.setText(phoneTiltAngle+"");
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		}, gyroSensor, SensorManager.SENSOR_DELAY_GAME);

	}


	private void startMyService() {
		serviceIntent = new Intent(MainActivity.this, LocalService.class);
		startService(serviceIntent);
	}

	private void startSampleService() {
		sampleServiceIntent = new Intent(MainActivity.this,SampleService.class);
		startService(sampleServiceIntent);
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
				Log.e(tag,disEyes+"");
				// 获取采样结果
				if (disEyes.size() >= 10) {
					float totalEyesDis = 0;
					for (int i=0;i<disEyes.size();i++) {
						totalEyesDis += disEyes.get(i);
					}
					defaultDEyes.setText(totalEyesDis/disEyes.size()+"");
				}else {
					AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
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
				break;
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
				MainActivity.this.finish();
				break;
			default:
				break;
		}

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
	protected void onDestroy() {
		super.onDestroy();
		if (serviceIntent != null) {
			doUnbindService();
			stopService(serviceIntent);
		}
		if (sampleServiceIntent != null) {
			doUnbindSampleService();
			stopService(sampleServiceIntent);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// bindService(serviceIntent, serviceConnection,
		// Context.BIND_AUTO_CREATE);
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
