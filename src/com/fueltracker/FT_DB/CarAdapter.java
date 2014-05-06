package com.fueltracker.FT_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author			: 	ajay.sahani
 * @date			:	26th April 2011
 * @purpose			:	to maintain information about car
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class CarAdapter {

	// instance variable for BariDB class
	private FuelTrackerDB fuel_tracker_DB=null;
	// DATABASE_TABLE instance variable to hold table name
	private final String DATABASE_TABLE="cars";
	
	public CarAdapter(Context context){
		fuel_tracker_DB = new FuelTrackerDB(context);

	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to create content value for table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:
	 * @Params			:car database attribute
	 * */
	
	private ContentValues createContentValues(String make,String note, String model,
			String distance_in,String volume_in,String consumption_in,float tank_capacity){
		ContentValues values = new ContentValues();
		values.put("make", make);
		values.put("note", note);
		values.put("model", model);
		values.put("distance_in", distance_in);
		values.put("volume_in", volume_in);
		values.put("consumption_in",consumption_in);
		values.put("tank_capacity",tank_capacity);
		return values;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to store information in car table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 *  @Params			:car  database attribute
	 * @Modification	:	
	 * */
	public long insertInCarsTable(String make,String note, String model,
			String distance_in,String volume_in,String consumption_in,float tank_capacity) {
	
		ContentValues values = createContentValues(make,note,model,distance_in,volume_in,consumption_in,tank_capacity);

		// now insert data in table
		return fuel_tracker_DB.insertEntry(DATABASE_TABLE, values);
	
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to update information in car table
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:car database attribute	
	 * @Modification	:	
	 * */
	public int updateCarsTable(int car_id,String make,String note, String model,
			String distance_in,String volume_in,String consumption_in,float tank_capacity) {
		
		ContentValues values = createContentValues(make,note,model,distance_in,volume_in,consumption_in,tank_capacity);
		
		// now update data in table
		return fuel_tracker_DB.updateEntry(DATABASE_TABLE, car_id, values,"id");
		
	}
	
	/**
	 * @author : ajay.sahani
	 * @date : 19 aug 2011
	 * @purpose : delete all entry from particular table
	 * @ModifiedBy : 
	 * @ModificationDate: 
	 * @Modification : 
	 * */
	public int deleteAllCar() {
		
		return fuel_tracker_DB.removeAllEntry(DATABASE_TABLE);
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	26th April 2011
	 * @purpose			:	delete car from table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public boolean deleteCarAccount(int id){
		
		//medi_alert_DB.open();
		return fuel_tracker_DB.removeEntry(DATABASE_TABLE, id,"id");
		//closeDB();
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get single car info
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getCarInfo(int car_id){
	
		Cursor cursor=	fuel_tracker_DB.getSingleEntries(DATABASE_TABLE, "id",car_id);

		return cursor;
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get all car info
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getAllCarInfo(){
	
		Cursor cursor=	fuel_tracker_DB.getAllEntries(DATABASE_TABLE );

		return cursor;
	}
	
}
