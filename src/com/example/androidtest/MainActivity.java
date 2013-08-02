package com.example.androidtest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
//import com.example.librarystudy.ReuseDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getName();
	private MyServer mMyserver;
	private Thread mThread;
	private TextView mOutPutView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mOutPutView = (TextView) findViewById(R.id.textView1);
		
//		SharedPreferences sp = getSharedPreferences("defaultSp", MODE_PRIVATE);
//		Set<String> set = new HashSet<String>();
//		sp.edit().putStringSet("", null);
		
//		String[] testArr = { "b", "a", "c", "dba", "fkdsl", "kfdsj", "fsfjsdlflsjf"};
//		Set<String> set = new LinkedHashSet<String>();
//		set.addAll(Arrays.asList(testArr));
//		Object[] arr2 = set.toArray();
//		for (Object obj:arr2) {
//			Log.d(TAG, "arr2=" + obj);
//		}
//		HandlerThread thread = new HandlerThread("backthread");
//		thread.start();
//		mMyserver = new MyServer(MainActivity.this, thread.getLooper());
//		
//		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//			private boolean visible = true;

			@Override
			public void onClick(View v) {
				//TextView tv = (TextView) v;
				
				//mMyserver.obtainMessage(0).sendToTarget();
//				try {
//					new KeyStoreManager().initialize(MainActivity.this);
//				} catch (GeneralSecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				new ReuseDialog(MainActivity.this).show();
//				visible = !visible;
//				setSystemBarVisible(MainActivity.this, visible);
//				scanMedia();
				studyExif();
			}
		});
		
//		findViewById(R.id.editText1).setActivated(true);
		
//		Intent intent = new Intent();
//		Log.d(TAG, "new Intent(): " + intent.toUri(Intent.URI_INTENT_SCHEME));
//		intent.setClass(this, KeyStoreManager.class);
//		Log.d(TAG, "setClass: " + intent.toUri(Intent.URI_INTENT_SCHEME));
//		intent.setData(Uri.parse("http://www.baidu.com"));
//		Log.d(TAG, "setData: " + intent.toUri(Intent.URI_INTENT_SCHEME));
//		try {
//			Intent intent2 = Intent.parseUri(intent.toUri(Intent.URI_INTENT_SCHEME), Intent.URI_INTENT_SCHEME);
//			Log.d(TAG, "intent2: " + intent2.toUri(Intent.URI_INTENT_SCHEME));
//			Log.d(TAG, "parseUri: " + "intent==intent?" + intent.equals(intent2));
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		View v = LayoutInflater.from(this).inflate(R.layout.dialog_test, null);
//		AlertDialog dlg = new AlertDialog.Builder(this)
//		.setView(v).create();
//		dlg.show();
//		dlg.getWindow().setLayout(500, 500);
		
//		LayoutParams p = dlg.getWindow().getAttributes();
//		p.width = 1920;
//		p.height = 720;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static void setSystemBarVisible(final Activity context,boolean visible) {
    	Log.d(TAG, "in setSystemBarVisible, set visiable to " + visible);
	    int flag = context.getWindow().getDecorView().getSystemUiVisibility();   // 获取当前SystemUI显示状态
	    // int fullScreen = View.SYSTEM_UI_FLAG_SHOW_FULLSCREEN;
	    int fullScreen = 0x8;   // 4.1 View.java的源码里面隐藏的常量SYSTEM_UI_FLAG_SHOW_FULLSCREEN，其实Eclipse里面也可以调用系统隐藏接口，重新提取下android.jar，这里就不述了。
	    if(visible) {   // 显示系统栏
	        if((flag & fullScreen) == 1) {  // flag标志位中已经拥有全屏标志SYSTEM_UI_FLAG_SHOW_FULLSCREEN
	        	Log.d(TAG, "in setSystemBarVisible, set to true");
	            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);   // 显示系统栏
	        }
	    } else {    // 隐藏系统栏
	        if((flag & fullScreen) == 0) {  // flag标志位中不存在全屏标志SYSTEM_UI_FLAG_SHOW_FULLSCREEN
	        	Log.d(TAG, "in setSystemBarVisible, set to false");
	            context.getWindow().getDecorView().setSystemUiVisibility(flag | fullScreen); // 把全屏标志位加进去
	        }
	    }
	}
	
	private void scanMedia() {
		String filePath = Environment.getExternalStorageDirectory()
				+ "/DCIM/Camera";
		File file = new File(filePath);
		final File[] files = file.listFiles();
		Log.d(TAG, "files length = " + files.length);
		String[] filePaths = new String[files.length];
		for (int i = 0; i < files.length; ++i) {
			filePaths[i] = files[i].getPath();
		}
		scanByService(filePaths);
	}
	
	private void scanByBroadCast(String path) {
		Log.d(TAG, "Path=" + path);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
		        + path)));
	}
	
	private void scanByService(String[] paths) {
		if (paths == null || paths.length == 0) {
			Log.e(TAG, "in scanByService, paths have nothing");
			return;
		}
		
		String[] mimeTypes = new String[paths.length];
		Arrays.fill(mimeTypes, "image/jpeg");
		
		MediaScannerConnection.scanFile(MainActivity.this, paths, mimeTypes, new MediaScannerConnection.OnScanCompletedListener() {
			@Override
			public void onScanCompleted(String path, Uri uri) {
				Log.d(TAG, "in onScanCompleted: path=" + path + " uri=" + uri);
			}
		});
	}

	private void downloadBySystem() {
		DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);  
        
	    Uri uri = Uri.parse("fileUrl");  
	    Request request = new Request(uri);  
	  
	    //设置允许使用的网络类型，这里是移动网络和wifi都可以    
	    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);    
	  
	    //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION    
	    //request.setShowRunningNotification(false);    
	  
	    //不显示下载界面    
	    request.setVisibleInDownloadsUi(false);  
	        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件        在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，不设置，下载后的文件在/cache这个  目录下面*/  
	    //request.setDestinationInExternalFilesDir(this, null, "tar.apk");  
	long id = downloadManager.enqueue(request);  
		//把id保存好，在接收者里面要用，最好保存在Preferences里面
	}

	private void studyExif() {
//		String baseName = "IMG_20130210_161320";
//		String filePath = Environment.getExternalStorageDirectory() + "/DCIM/" + baseName + ".jpg";		//	Camera/
		String baseName = "C360_2013-02-10-16-13-49";
		String filePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + baseName + ".jpg";		//	Camera/
		
		printExifInfo(filePath);
//		setExifInfo(filePath, getDataTimeByFileName(filePath));
//		printExifInfo(filePath);
	}
	
	private void printExifInfo(String filePath) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		 String item = exif.getAttribute(ExifInterface.TAG_DATETIME);
		 mOutPutView.append("ExifInterface.TAG_DATETIME=" + item + "\n");
//		 item = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
//		 mOutPutView.append("TAG_GPS_DATESTAMP=" + item + "\n");
	}
	
	private void setExifInfo(String filePath, String dateTime) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		exif.setAttribute("DateTimeOriginal", dateTime);
		try {
			exif.saveAttributes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getDataTimeByFileName(String filePath) {
		Pattern pattern = Pattern.compile("IMG_([0-9]{8})_([0-9]{6})");
		Matcher matcher = pattern.matcher(filePath);

		if (matcher.find()) {
			System.out.println("matcher.group()=" + matcher.group());
			String date = matcher.group(1);
			String time = matcher.group(2);
			date = date.substring(0, 4) + ":" + date.substring(4, 6) + ":"
					+ date.substring(6, 8);
			time = time.substring(0, 2) + ":" + time.substring(2, 4) + ":"
					+ time.substring(4, 6);
			String dateTime = date + " " + time;
			System.out.println("dateTime=" + dateTime);
			return dateTime;
		}

		pattern = Pattern.compile("C360_([0-9-]{10})-([0-9-]{8})");
		matcher = pattern.matcher(filePath);
		if (matcher.find()) {
			System.out.println("matcher.group()=" + matcher.group());
			String date = matcher.group(1).replace('-', ':');
			String time = matcher.group(2).replace('-', ':');
			String dateTime = date + " " + time;
			System.out.println("dateTime=" + dateTime);
			return dateTime;
		}

		System.err.println("have not matched!");
		return "";
	}
}
