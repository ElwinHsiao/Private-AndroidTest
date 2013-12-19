package com.example.androidtest;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class WeatherSettingDialog extends Dialog {

	private Context mContext;

	public WeatherSettingDialog(Context context) {
		super(context, R.style.weatherSettingTheme);
		mContext = context;
		setupAttribute();
		setupContent();
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.edit);
	}

	private void setupAttribute() {
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_LEFT_ICON);
//		window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
//		window.setFeatureDrawableAlpha(Window.FEATURE_LEFT_ICON, 255);
//		window.requestFeature(Window.FEATURE_NO_TITLE);
//		window.setWindowAnimations(R.style.openInLineAnimate);
//		window.setDimAmount(0.5f);
//		window.setGravity(Gravity.LEFT | Gravity.TOP);
		LayoutParams param = window.getAttributes();
//		param.gravity = Gravity.CENTER;
		param.height = LayoutParams.WRAP_CONTENT;
		param.width = 700;
//		param.flags |= LayoutParams.FLAG_FULLSCREEN;
//		param.dimAmount = 0.5f;
//		window.setAttributes(param);
	}

	private void setupContent() {
		setTitle("天气设置");
		setContentView(R.layout.weather_setting);
		
		// set the left margin of title icon
		((ViewGroup.MarginLayoutParams) findViewById(0x01020226).getLayoutParams()).setMargins(9, 0, 0, 9);
	}
	
	public void show() {
		super.show();
		findViewById(R.id.radioGroup1).requestFocus();
	}

}
