package com.example.androidtest;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.os.Build;
import android.util.Log;
import swl.lib.common.SwlConfig;
import swl.lib.common.SwlDeviceInfo;

public class DeviceSecret {

	private static final String TAG = "DeviceSecret";
	private Thread mWorkThread;

	public String getDeviceSerialNumber() {
		return SwlDeviceInfo.getStbSn();
//		return null;
	}
	
	public String getSystemVersion() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "SW-" + SwlConfig.getSystemVersion() + " " + sdf.format(Build.TIME);
//		return null;
	}
	
	public String getCustomId() {
		return SwlConfig.getCustomerName();
//		return null;
	}
	
	public boolean burnSerialNumber() throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		String commad[] = {"/system/bin/snTarget", "192.168.1.58", "m"};
		Process pcs =rt.exec(commad);
		int exitValue = pcs.waitFor();
		Log.i(TAG, "snTarget exit code: " + exitValue);
		
		return (exitValue == 0);
//		return false;
	}
	
	public void burnSerialNumber(final OnBurnSerialNumberListener listener) {
		if (mWorkThread != null) {
			if (mWorkThread.getState() != Thread.State.TERMINATED) {
				throw new IllegalThreadStateException("task is running.");
			}
		}
		
		mWorkThread = new Thread(new Runnable() {
			@Override
			public void run() {
				listener.onStarted();
				try {
					boolean result = burnSerialNumber();
					if (result) {
						listener.onSuccess();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				listener.onFailed();
			}
		});
		mWorkThread.start();
	}
	
	public interface OnBurnSerialNumberListener {
		void onStarted();
		void onSuccess();
		void onFailed();
	}
}
