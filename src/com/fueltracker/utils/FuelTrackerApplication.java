package com.fueltracker.utils;

import android.app.Application;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

/**
 * @UiAuthor		: 	ajay.sahani
 * @date			:	12/08/2011
 * @purpose			:	to store application constant
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class FuelTrackerApplication extends Application{
	
	public static SQLiteDatabase db=null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

}
