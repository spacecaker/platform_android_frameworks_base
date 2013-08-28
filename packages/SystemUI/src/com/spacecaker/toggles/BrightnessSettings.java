package com.spacecaker.toggles;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class BrightnessSettings extends ImageView {

	 public BrightnessSettings(final Context context, AttributeSet attrs) {
		  super(context, attrs);
		  
		  ImageView image = (ImageView) findViewById(R.id.brightness);
		  image.setImageResource(R.drawable.ic_notify_quicksettings);
			image.setOnClickListener(new View.OnClickListener() {
				 
				@Override
				public void onClick(View v) {
	                v.getContext().startActivity((new Intent("com.android.settings.DISPLAY_SETTINGS")).setFlags(0x10000000));
				    try{ 
				    	   Object service  = context.getSystemService("statusbar");
				    	   Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
				    	   Method collapse = statusbarManager.getMethod("collapse");
				    	   collapse.invoke(service);
				    	 }
				    	 catch(Exception ex){           

				    	 }
		        }
		});
	 }

	}
