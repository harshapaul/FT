package com.fueltracker.FT_DB;




import com.fueltracker.utils.FuelTrackerApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author : ajay.sahani
 * @date : 10th aug 2011
 * @purpose : To create fuel_tracker analytical table
 * @ModifiedBy : 
 * @ModificationDate: 
 * @Modification : 
 *               
 * */

public class FuelTrackerDB {
	public static final String DATABASE_NAME = "fuel_tracker.db";
	private static final int DATABASE_VERSION = 1;
	  

	// define all the table here
	// public static final String DATABASE_TABLE="mainTable";
	private static final String CARS = "cars";
	private static final String FUEL_REFILLS = "fuel_refills";
	private static final String CONFIGURATION = "configuration";
	
	
	private final String TAG = "FuelTrackerDB";


	private SQLiteDatabase db = null;
	// to set some parameter which is usefull in future
	// private BeriApplication application;

	// the name and column index of each column in your database
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;

	// SQL statement to create new database table

	public static final String CARS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ CARS
			+ "( id INTEGER PRIMARY KEY AUTOINCREMENT not null, make VARCHAR(20) not null, note TEXT ,"
			+ " model VARCHAR(20) not null,distance_in VARCHAR(20) not null,volume_in VARCHAR(20) not null,consumption_in VARCHAR(20) not null,tank_capacity  FLOAT  not null)";

	
	
	public static final String FUEL_REFILLS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+FUEL_REFILLS
			+ "(id INTEGER PRIMARY KEY AUTOINCREMENT not null,date_of_refill DATETIME not null,"
			+ "odometer_reading FLOAT  not null, volume FLOAT  not null,volume_type VARCHAR(20) not null,price FLOAT  not null,currency_type VARCHAR(20) not null,total_cost FLOAT  not null,"
			+"current_fuel_level  FLOAT  not null,car_id INTEGER not null, mileage  FLOAT  not null,note TEXT,Foreign key (car_id) REFERENCES cars(id) )";

	
	public static final String CONFIGURATION_TABLE = "CREATE TABLE IF NOT EXISTS "
		+ CONFIGURATION
		+ "(id INTEGER PRIMARY KEY AUTOINCREMENT not null,currency VARCHAR(20) not null,"
		+ "volume_type VARCHAR(20) not null,distance_type VARCHAR(20) not null,consumption_type VARCHAR(20) not null,sel_car_id INTEGER not null)";

	// variable to hold database instance
	// private SQLiteDatabase db;

	// context of application using database
	private final Context context;

	// Database open/upgrade helper
	private FuelTrackerDBHelper dbHelper;

	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : To create bari analytical table
	 * @ModifiedBy :
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	public FuelTrackerDB(Context _context) {
		context = _context;
		// Log.i(TAG,"before dbHelper=new BariDbHelper(context,DATABASE_NAME");
		dbHelper = new FuelTrackerDBHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		
		 db=FuelTrackerApplication.db;
		open();
		
	}

	// open database to write

	/**
	 * @author : ajay.sahani
	 * @purpose : open database to write
	 * @ModifiedBy : ajay.sahani
	 * @ModificationDate: 10th Aug  2011
	 * @Modification : Added in order open data base to write
	 * */
	public FuelTrackerDB open() throws SQLException {

		// Log.i(TAG,"before SysConstant.db=dbHelper.getWritableDatabase");
								
				/*if(db==null){
				db= dbHelper.getWritableDatabase();
				}*/
			if(FuelTrackerApplication.db==null){			
					
					db=FuelTrackerApplication.db = dbHelper.getWritableDatabase();
					
			}else{
				if(!FuelTrackerApplication.db.isOpen()){
					db=FuelTrackerApplication.db = dbHelper.getWritableDatabase();
				}
			}
				
		return this;
	}

	// to close database
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : close database
	 * @ModifiedBy :
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	public void close() {

		// Log.i(TAG,"before SysConstant.db.close()");
		try{
			dbHelper.close();
		}catch(Exception error){
			error.printStackTrace();
		}
	}

	// to enter data in table
	/**
	 * @author : ajay.sahani
	 * @date : 18th April 2011
	 * @purpose : to enter data in table
	 * @ModifiedBy :
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	public long insertEntry(String DATABASE_TABLE, ContentValues values) {
		// ContentValues is key :value pair
		// key---->column name
		// values------>value to be inserted in that column
		ContentValues contentValues = values;
		// fill the content values to reprsent new
		return db.insert(DATABASE_TABLE, null, contentValues);
	}

	// to remove row where rowIndex is int
	/**
	 * @author : ajay.sahani
	 * @date : 19 aug 2011
	 * @purpose : delete all entry from particular table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	public int removeAllEntry(String table_name) {
		// return db.delete(table_name, key_name+"="+_rowIndex,null)>0;
		return db.delete(table_name, null, null);
	}
	
	// to remove row where rowIndex is int
	/**
	 * @author : ajay.sahani
	 * @date : 10 aug 2011
	 * @purpose : remove row from table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	public boolean removeEntry(String table_name, int _rowIndex, String key_name) {
		// return db.delete(table_name, key_name+"="+_rowIndex,null)>0;
		return db.delete(table_name, key_name + "=?", new String[] { ""
				+ _rowIndex }) > 0;
	}

	// to remove row where rowIndex is String
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : remove row from table where row index is string
	 * @ModifiedBy : 
	 * @ModificationDate:
	 * @Modification :
	 * */
	public boolean removeEntry(String table_name, String _rowIndex,
			String key_name) {
		return db.delete(table_name, key_name + "=?",
				new String[] { _rowIndex }) > 0;
		// the number of rows affected if a whereClause is passed in, 0
		// otherwise. To remove all rows and get a count pass "1" as the
		// whereClause
	}

	// to get all of particular table
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	public Cursor getLatestOdoReading(String table_name) {
		String[] return_column=new String[]{"id","date_of_refill","odometer_reading","car_id","count(id)"};
		return db.query(table_name, return_column, null, null,"car_id", null,"date_of_refill desc");
	}

	/**
	 * @author : ajay.sahani
	 * @date : 21th Aug  2011
	 * @purpose : use to calculate mileage
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	public Cursor getLatestOdometerReadingUsingCar_ID(String table_name,int car_id){
		String[] return_column=new String[]{"odometer_reading","volume","current_fuel_level"};
		String where ="car_id=?";
		return db.query(table_name, return_column, where, new String[]{""+car_id},"car_id", null,"date_of_refill desc limit 1");
	}
	
	
	/**
	 * @author : ajay.sahani
	 * @date : 21th Aug  2011
	 * @purpose : use to calculate mileage
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	public Cursor avgFuleConsumpPerMnt(String table_name,int car_id){
		
		String where = "car_id = ?";
		String[] return_column=new String[]{" ( strftime('%m',date_of_refill) || strftime('%Y',date_of_refill)) as monthyear","sum(volume) totalvolume"};
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] { ""+car_id }, "monthyear", null,null);
		return cursor;	
	}
	
	
	/**
	 * @author : ajay.sahani
	 * @date : 16th Aug  2011
	 * @purpose : to get cursor that contain all stats related data
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	public Cursor getStatsVolumeInfo(String table_name,int car_id) {
		String where_col="car_id = ?";
		//String[] return_column=new String[]{"id","date_of_refill","odometer_reading","count(id)","sum(volume)","sum(odometer_reading)"};
		String[] return_column=new String[]{"count(id)","sum(volume)","sum(odometer_reading)","avg(volume)","sum(total_cost)","sum(mileage)"};
		return db.query(table_name, return_column, where_col,new String[]{""+car_id},null, null,"date_of_refill desc");
	}
	
	/**
	 * @author : ajay.sahani
	 * @date : 16th Aug  2011
	 * @purpose : to get cursor that 1st and last odometer reading along with 1st and last date
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	//currently not in used
	public Cursor getStatsOdo_dateInfo(String table_name,int car_id) {
		String where_col="car_id = ?";
		String[] return_column=new String[]{"id","date_of_refill","odometer_reading","current_fuel_level"};
		return db.query(table_name, return_column, where_col,new String[]{""+car_id},null, null,"date_of_refill desc");
	}
	
	// to get all of particular table
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain previous row
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification :
	 *             
	 * */
	public Cursor getFuelRefillUsingDate(String table_name, String col_name, String date ,int car_id) {
		//select id,date,carid,odometer from fuel_refills where date<11113 order by date desc limit 1
		
		String[] return_column=new String[]{"id","date_of_refill","odometer_reading","car_id","volume","current_fuel_level","currency_type"};
	
/*		String query = "select id,date_of_refill,odometer_reading,current_fuel_level from fuel_refills where date_of_refill < '" + date + "' and car_id=" + car_id + " and id!=" + fillupId + " order by date_of_refill desc limit 1";

		Log.i(TAG,"query = " + query);
		
		Cursor cursor  = db.rawQuery(query, null);
		
		Log.i(TAG, "Cursor size = " + cursor.getCount());
*/		

		String where = "date_of_refill <? and car_id = ? and date_of_refill != ?";
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] {date,""+car_id,""+date}, null, null,"date_of_refill DESC limit 1");		
		
		Log.i(TAG, "current date = " + date);
		Log.i(TAG, "current car_id= " + car_id);
		//Log.i(TAG, "currentfillup_id= " + fillupId);
		Log.i(TAG, "Cursor size = " + cursor.getCount());
		
		return cursor;
		
	}

	
	/**
	 * @author : ajay.sahani
	 * @date : 15sep  2011
	 * @purpose : to get cursor that contain previous row during edit mode
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @param:			get fillup where fillup id should not equal to this fillupid
	 * @Modification :
	 *             
	 * */
	public Cursor getFuelRefillUsingDateEdit(String table_name, String col_name, String date ,int car_id, int fillupId) {
		// TODO Auto-generated method stub
		String[] return_column=new String[]{"id","date_of_refill","odometer_reading","car_id","volume","current_fuel_level","currency_type"};
		
		String where = "date_of_refill <? and car_id = ? and id != ?";
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] {date,""+car_id,""+fillupId}, null, null,"date_of_refill DESC limit 1");		
		
		Log.i(TAG, "current date = " + date);
		Log.i(TAG, "current car_id= " + car_id);
		Log.i(TAG, "currentfillup_id= " + fillupId);
		Log.i(TAG, "Cursor size = " + cursor.getCount());
		
		return cursor;
		
	}
	
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain next row(upper row)
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification :
	 *             
	 * */
	public Cursor getFuelRefillNextRow(String table_name,String date ,int car_id,int fillupId) {
		//select id,date_of_refill,odometer_reading,current_fuel_level from fuel_refills where date_of_refill > '2011-09-10 17:44:51' and car_id='6' order by date_of_refill asc limit 1;
		
		/*Log.i(TAG,"Table name = " + table_name);
		Log.i(TAG,"date = " + date);
		Log.i(TAG,"car id = " + car_id);*/
		
		String where = "date_of_refill" + " > ? and car_id = ? and id != ?";
		String[] return_column=new String[]{"id","odometer_reading","current_fuel_level","date_of_refill"};
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] {date,""+car_id ,""+fillupId}, null, null,"date_of_refill ASC limit 1");
		return cursor;	
	}
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification :
	 *             
	 * */
	public Cursor getAccumulatedInfo(String table_name,int car_id,int year,int mnth) {
		//String where = car_id + "<?" +" and date_of_refill like '%2011-8-%' or  '%2011-08-%'";
		
		String where =" car_id " + "=?" +" and date_of_refill like '%"+year+"-"+mnth+"-%' or " +"'%"+year+"-0"+mnth+"-%'";
		
		String[] return_column=new String[]{" max(date_of_refill)"," min(date_of_refill)"," sum(volume)"};
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] {  ""+car_id }, null, null,null);
		return cursor;	
	}
	
	
	//////
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification :
	 *             
	 * */
	public Cursor getPreviousFuelRefillVolume(String table_name, int car_id) {
		//select id,date,carid,odometer from fuel_refills where date<11113 order by date desc limit 1
		String where = "car_id = ?";
		String[] return_column=new String[]{"date_of_refill","volume"};
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] { ""+car_id }, null, null,"date_of_refill desc limit 2");
		return cursor;	
	}
	
	
	/**
	 * @author : ajay.sahani
	 * @date : 23 Aug  2011
	 * @purpose : to get cursor that contain avg cost for all month
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification :
	 *             
	 * */
	public Cursor getAvgCostPermonth(String table_name, int car_id) {
		//SELECT ( strftime('%m',date_of_refill) || strftime('%Y',date_of_refill)) as monthyear, (sum(total_cost)/count(total_cost)) as avgcost FROM fuel_refills   where car_id=4 group by monthyear;
		  
		String where = "car_id = ?";
		String[] return_column=new String[]{" ( strftime('%m',date_of_refill) || strftime('%Y',date_of_refill)) as monthyear","sum(mileage) as totalmileage","sum(volume) totalvolume","sum(total_cost) as totalcost"};
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] { ""+car_id }, "monthyear", null,null);
		return cursor;	
	}
	
	/**
	 * @author : ajay.sahani
	 * @date : 24 Aug  2011
	 * @purpose : to get cursor that contain totalmileage and totalvolume for each month
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification :
	 *             
	 * *//*
	public Cursor getVolMileagePermonth(String table_name, int car_id) {
		//SELECT ( strftime('%m',date_of_refill) || strftime('%Y',date_of_refill)) as monthyear, sum(mileage) as totalmileage,sum(volume) totalvolume FROM fuel_refills   where car_id=2 group by monthyear;
		String where = "car_id = ?";
		String[] return_column=new String[]{" ( strftime('%m',date_of_refill) || strftime('%Y',date_of_refill)) as monthyear","sum(mileage) as totalmileage","sum(volume) totalvolume"};
		
		Cursor cursor = db.query(table_name, return_column, where, new String[] { ""+car_id }, "monthyear", null,null);
		return cursor;	
	}*/
	// to get all of particular table
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	

	public Cursor getAllFuelRefill(String table_name ,int car_id) {
		String where="car_id=?";
		return db.query(table_name, null, where, new String[]{""+car_id}, null, null, "date_of_refill desc");
	}
	
	
	
	
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain all fillup sorted according to date
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	

	public Cursor getAllEntries(String table_name) {
		return db.query(table_name, null, null, null, null, null, null);
	}
	// to get single row of particular table
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug 2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *               
	 * */
	
	
	public Cursor getSingleEntries(String table_name, String col_name, int id) {
		// String where=col_name+"="+id;
		String where = col_name + "=?";

		open();
		Cursor cursor = db.query(table_name, null, where, new String[] { ""
				+ id }, null, null, null);
		return cursor;				
	}

	// to get single row of particular table
	// using 2 selection parameter
	/**
	 * @author : ajay.sahani
	 * @date : 10th Aug  2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 *              
	 * */
	public Cursor getSingleEntries(String table_name, String col_name1,
			String col_name2, int id1, String id2) {
		// String where=col_name1+"="+id1+" and "+col_name2+"="+id2;
		String where = col_name1 + "=?" + " and " + col_name2 + "=?";

		Cursor cursor = db.query(table_name, null, where, new String[] {
				"" + id1, id2 }, null, null, null);

		return cursor;
	}

	// to get single row of particular table
	// both id is string parameter
	/**
	 * @author : ajay.sahani
	 * @date : 18th April 2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy :
	 * @ModificationDate: 
	 * @Modification : 
	 *               
	 * */
	public Cursor getSingleEntries(String table_name, String col_name1,
			String col_name2, String id1, String id2) {
		// String where=col_name1+"="+id1+" and "+col_name2+"="+id2;
		String where = col_name1 + "=?" + " and " + col_name2 + "=?";
		Cursor value = db.query(table_name, null, where, new String[] { id1,
				id2 }, null, null, null);
		return value;
	}

	// to get single row of particular table
	// id is string
	/**
	 * @author : ajay.sahani
	 * @date : 18th April 2011
	 * @purpose : to get cursor that contain all row of table
	 * @ModifiedBy :
	 * @ModificationDate: 
	 * @Modification :
	 * */
	public Cursor getSingleEntries(String table_name, String col_name, String id) {
		String where = col_name + "=?";
		Cursor value = db.query(table_name, null, where, new String[] { id },
				null, null, null);
		return value;
	}
	
	


	/**
	 * @author : ajay.sahani
	 * @date : 18th April 2011
	 * @purpose : to update table row where id is integer
	 * @ModifiedBy :
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	// public int updateEntry(String table_name,long _rowIndex,Object obj){
	public int updateEntry(String table_name, int _rowIndex,
			ContentValues values, String key_name) {
		// String where =key_name+"="+_rowIndex;
		String where = key_name + "=?";
		ContentValues contentValues = values;
		// fill the content values by reteriving value from object
		return db.update(table_name, contentValues, where, new String[] { ""
				+ _rowIndex });
		// the number of rows affected
	}

	/**
	 * @author : ajay.sahani
	 * @date : 18th April 2011
	 * @purpose : to update table row where id is string
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	// public int updateEntry(String table_name,long _rowIndex,Object obj){
	public int updateEntry(String table_name, String _rowIndex,
			ContentValues values, String key_name) {
		String where = key_name + "=?";
		ContentValues contentValues = values;
		// fill the content values by reteriving value from object
		return db.update(table_name, contentValues, where,
				new String[] { _rowIndex });
	}

	
	
}
