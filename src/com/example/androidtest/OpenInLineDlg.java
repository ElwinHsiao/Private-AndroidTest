package com.example.androidtest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class OpenInLineDlg extends Dialog {

	private Context mContext;

	public OpenInLineDlg(Context context) {
		super(context, R.style.openInLineDialogTheme);
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
		setTitle("快捷菜单");
		setContentView(R.layout.shortcut_menu);
		
		// wallpaper
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSetWallpaper(mContext);
//				findViewById(R.id.content_root).animate().scaleY(0.0f);
			}
		});
		
		// weather
		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startWeatherSetting();
				//findViewById(R.id.content_root).animate().scaleY(1.0f);
			}
		});
		
		// on boot
		findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startOnbootSetting();
			}
		});
		
		// sweeper
		findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSweep();
			}
		});
		
		// set the left margin of title icon
		((ViewGroup.MarginLayoutParams) findViewById(0x01020226).getLayoutParams()).setMargins(9, 0, 0, 9);
	}


	// TODO: move to Javiki
	private static void startSetWallpaper(Context context) {
		final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);  
		Intent chooser = Intent.createChooser(pickWallpaper, "Set wallpaper");
		context.startActivity(chooser);
		
//		Intent intent = new Intent(context, WallpaperChooser.class);
//		context.startActivity(intent);
	}
	
	private void startWeatherSetting() {
		// TODO 
		new WeatherSettingDialog(mContext).show();
		
//		View weatherView = LayoutInflater.from(mContext).inflate(R.layout.weather_setting, null);
//		findViewById(android.R.id.content);
	}
	
	private void startOnbootSetting() {
		// TODO 
		
	}
	
	private void startSweep() {
		// TODO 
		
	}
	
//	@Override
//	public void show() {
//		super.show();
//		findViewById(R.id.content_root).setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dialog_enter));
//	}
//
//	@Override
//	public void dismiss() {
//		findViewById(R.id.content_root).setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dialog_exit));
//		
////		super.dismiss();
//	}
}
