package com.b16h22.statusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Battery extends LinearLayout {
	
	int level;
	String message;
	
	 public Battery(final Context context, AttributeSet attrs) {
		  super(context, attrs);
		  

          final ImageView battery =new ImageView(context);
          final TextView batteryText =new TextView (context);
          final TextView chargingText =new TextView (context);
		  LayoutParams text = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		  LayoutParams chargetext = new LayoutParams(70,LayoutParams.WRAP_CONTENT);
		  text.gravity=Gravity.CENTER_VERTICAL;
		  batteryText.setLayoutParams(text);
		  chargingText.setLayoutParams(chargetext);
		  chargetext.gravity=Gravity.CENTER_VERTICAL;
		  chargingText.setTextSize(15);
		  batteryText.setTextSize(13);
    	  chargingText.setText("Discharging");
		  this.addView(chargingText);
          this.addView(battery);
          this.addView(batteryText);
	 	    SharedPreferences sharedPreferences = context.getSharedPreferences("EvoPrefsFile",Context.MODE_PRIVATE);    
	 	    Boolean batteryVisibility = sharedPreferences.getBoolean("battery",false);
	 	      if (batteryVisibility == false){
	 	    	  String layoutType = sharedPreferences.getString("type","phablet");
	 	    	  
	              	if ("tablet".equals(layoutType )) {
	              		chargingText.setVisibility(GONE);
	              	}
	              	else if ("phablet".equals(layoutType )) {
	              		chargingText.setVisibility(VISIBLE);
	              	}
	              	else if ("normal".equals(layoutType )) {
	              		chargingText.setVisibility(VISIBLE);
	              	}
            	  batteryText.setVisibility(VISIBLE);
            	  battery.setVisibility(VISIBLE);
	 	      }
	 	      else{
            	  batteryText.setVisibility(GONE);
            	  battery.setVisibility(GONE);
            	  chargingText.setVisibility(GONE); 
	 	      }
          
          BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
              @Override
                //When Event is published, onReceive method is called
              public void onReceive(Context c, Intent i) {
                    //Get Battery %
                  level = i.getIntExtra("level", 0);
              	  battery.setImageLevel(level);
            	 
                  int status = i.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                  boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                        status == BatteryManager.BATTERY_STATUS_FULL;
                  if (isCharging == true){
                  	battery.setImageLevel(level);
        		    battery.setImageResource(R.drawable.stat_battery_level_charging);
                  }
                  else {
                	  battery.setImageResource(R.drawable.stat_battery_level);
                  }
                  
                  
                  batteryText.setText(Integer.toString(level) + "%");
                  int chargePlug = i.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                  boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                  boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        		    if (usbCharge == true){
                      chargingText.setText("Charging USB");
        		    }
        		    else if (acCharge == true){
                      chargingText.setText("Charging AC");
        		    }
                    else {
                  	  chargingText.setText("Discharging");  
                    }
             
              }

          };
          
          BroadcastReceiver mBatDisconnectedReceiver = new BroadcastReceiver() {
              @Override
                //When Event is published, onReceive method is called
              public void onReceive(Context c, Intent i) {
      		    
                	  chargingText.setText("Discharging");

              }
       
          };
          
          BroadcastReceiver mReceiver = new BroadcastReceiver() {
              @Override
              public void onReceive(Context c, Intent i) {
            	  batteryText.setVisibility(GONE);
            	  battery.setVisibility(GONE);
            	  chargingText.setVisibility(GONE);
                  SharedPreferences sharedPreferences = context.getSharedPreferences("EvoPrefsFile",Context.MODE_PRIVATE);
                  SharedPreferences.Editor editor = sharedPreferences.edit(); //opens the editor
                  editor.putBoolean("battery", true); //true or false
                  editor.commit();	 
              }
              
          }; 
          BroadcastReceiver mReceiver1 = new BroadcastReceiver() {
              @Override
              public void onReceive(Context c, Intent i) {
            	  batteryText.setVisibility(VISIBLE);
            	  battery.setVisibility(VISIBLE);
            	  chargingText.setVisibility(VISIBLE);
                  SharedPreferences sharedPreferences = context.getSharedPreferences("EvoPrefsFile",Context.MODE_PRIVATE);
                  SharedPreferences.Editor editor = sharedPreferences.edit(); //opens the editor
                  editor.putBoolean("battery", false); //true or false
                  editor.commit();	 
              }
              
          }; 
          BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
              @Override
              public void onReceive(Context c, Intent i) {
              	message = i.getStringExtra("layoutType");
              	
              	if ("tablet".equals(message )) {
              		chargingText.setVisibility(GONE);
              	}
              	else if ("phablet".equals(message )) {
              		chargingText.setVisibility(VISIBLE);
              	}
              	else if ("normal".equals(message )) {
              		chargingText.setVisibility(VISIBLE);
              	}
              }
              
          };      
          
          context.registerReceiver(mReceiver2, new IntentFilter("com.b16h22.statusbar.CHANGE_LAYOUT")); 
          context.registerReceiver(mBatInfoReceiver, new IntentFilter(
                  Intent.ACTION_BATTERY_CHANGED));
          context.registerReceiver(mBatDisconnectedReceiver, new IntentFilter(
                  Intent.ACTION_POWER_DISCONNECTED));
          context.registerReceiver(mReceiver, new IntentFilter("com.b16h22.statusbar.HIDE_BATTERY")); 
          context.registerReceiver(mReceiver1, new IntentFilter("com.b16h22.statusbar.UNHIDE_BATTERY"));
	 }	
}
