package com.fueltracker.FT_DB;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;



/**
 * @author			: 	ajay.sahani
 * @date			:		10th Aug  2011
 * @purpose			:	Base class that create table and data base
 * @ModifiedBy		:	ajay.sahani
 * @ModificationDate:	18th April 2011
 * @Modification	:	Added in order to call function that enter value in particular column and table
 * */
public class FuelTrackerDBHelper extends SQLiteOpenHelper {

	Context context;
	SQLiteDatabase db;
	
	public final String TAG = "FuelTrackerDB";
	
	public FuelTrackerDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	//this method get called only once in life cyle of application
	//or if we change name of db
	/**
	 * @author			: 	ajay.sahani
	 * @date			:		10th Aug 2011
	 * @purpose			:	this method get called only once in life cyle of application
							or if we change name of db
	 * @ModifiedBy		:	ajay.sahani
	 * @ModificationDate:	18th April 2011
	 * @Modification	:	
	 * */
	public void onCreate(SQLiteDatabase _db) {
		
		//_db.execSQL(JaxterDB.DATABASE_CREATE);
		_db.execSQL(FuelTrackerDB.CARS_TABLE);		
		_db.execSQL(FuelTrackerDB.FUEL_REFILLS_TABLE);
		_db.execSQL(FuelTrackerDB.CONFIGURATION_TABLE);
				
	}

	//this method will get call when db version get upgrade
	/**
	 * @author			: 	ajay.sahani
	 * @date			:		10th Aug  2011
	 * @purpose			:	this method will get call when db version get upgrade
	 * @ModifiedBy		:	ajay.sahani
	 * @ModificationDate:	18th April 2011
	 * @Modification	:	
	 * */
	public void onUpgrade(SQLiteDatabase _db, int arg1, int arg2) {
	
		_db.execSQL("DROP TABLE IF EXISTS "+FuelTrackerDB.DATABASE_NAME);
		onCreate(_db);
	}

	
}
