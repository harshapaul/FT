package com.fueltracker.UI;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
/**
 * @UiAuthor		: 	ruchi.malvankar
 * @date			:	8/10/2011
 * @purpose			:	To create tab-wise representation for FuelTrracker elements 
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class FuelTracker extends TabActivity {
    
    public static TabHost tabHost = null; 
    public static View viewForKeyboard;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        /*if(customTitleSupported)
		{
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.custom_title);
			TextView tv = (TextView) findViewById(R.id.tv);
			tv.setText("Fuel Tracker");
		}*/
        //Resources res = getResources();
        //TabHost tabHost = getTabHost();  
        tabHost = getTabHost();
        TabHost.TabSpec spec;  
        Intent intent; 

       
        intent = new Intent().setClass(this, VehiclesTabGroup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        spec = tabHost.newTabSpec("Vehicles").setContent(intent).setIndicator(createTabView(tabHost.getContext(),"",R.drawable.vehicle_selector));
        tabHost.addTab(spec);
        
        /*intent = new Intent().setClass(this, Fillups.class);
        spec = tabHost.newTabSpec("Fill-ups").setContent(intent).setIndicator("Fillups",getResources().getDrawable(R.drawable.fillup_icon));
        tabHost.addTab(spec);*/
        tabHost.addTab(tabHost.newTabSpec("Fill-ups")
                .setIndicator(createTabView(tabHost.getContext(),"",R.drawable.fillups_selector))
                .setContent(new Intent(this, FillupsTabGroup.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
             
        intent = new Intent().setClass(this, Analysis.class);
        spec = tabHost.newTabSpec("Analysis").setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).setIndicator(createTabView(tabHost.getContext(),"",R.drawable.analysis_selector));
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Config.class);
        //intent = new Intent().setClass(this, Configuration.class);
        spec = tabHost.newTabSpec("Config").setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).setIndicator(createTabView(tabHost.getContext(),"",R.drawable.config_selector));
        tabHost.addTab(spec);
        
        
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tabHost.getWindowToken(), 0);
			}
		});
    }
    private View createTabView(final Context context, final String text,int id) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		ImageView imgv=(ImageView) view.findViewById(R.id.img1);
		imgv.setImageResource(id);
		tv.setText(text);
		tv.setVisibility(View.GONE);
		return view;
	}
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}