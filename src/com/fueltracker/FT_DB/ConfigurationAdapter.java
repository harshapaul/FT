package com.fueltracker.FT_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author			: 	ajay.sahani
 * @date			:	10th aug 2011
 * @purpose			:	to maintain information about configuration
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class ConfigurationAdapter {

	// instance variable for BariDB class
	private FuelTrackerDB fuel_tracker_DB=null;
	// DATABASE_TABLE instance variable to hold table name
	private final String  DATABASE_TABLE="configuration";
	
	public ConfigurationAdapter(Context context){
		fuel_tracker_DB = new FuelTrackerDB(context);
		
}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to create content value for table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:
	 * @Params			:configuration database attribute
	 * */
	private ContentValues createContentValues(String currency,String volume_type,String distance_type,String consumption_type, int sel_car_id){
		
		ContentValues values = new ContentValues();
		if(currency!=null)
			values.put("currency", currency);
		if(volume_type!=null)
			values.put("volume_type", volume_type);
		if(distance_type!=null)
			values.put("distance_type", distance_type);
		if(consumption_type!=null)
			values.put("consumption_type",consumption_type);
		if(sel_car_id!=-1)
			values.put("sel_car_id",sel_car_id);
		else {
		values.put("sel_car_id",-1);
		}
		return values;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to store information in configuration table
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:configuration database attribute	
	 * @Modification	:	
	 * */
	public long insertInConfigurationTable(String currency,String volume_type,String distance_type,String consumption_type, int sel_car_id) {
		
		ContentValues values = createContentValues(currency,volume_type,distance_type,consumption_type,sel_car_id);

		// now insert data in table
		return fuel_tracker_DB.insertEntry(DATABASE_TABLE, values);

	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to update information in configuration table
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:configuration database attribute	
	 * @Modification	:	
	 * */
	public int updateConfigurationTable(int id,String currency,String volume_type,String distance_type,String consumption_type, int sel_car_id) {
		
		ContentValues values = createContentValues(currency,volume_type,distance_type,consumption_type,sel_car_id);
		
		// now update data in table
		return fuel_tracker_DB.updateEntry(DATABASE_TABLE, id, values,"id");
		
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	delete  configuration
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public boolean deleteConfigurationAccount(int id){
		
		return fuel_tracker_DB.removeEntry(DATABASE_TABLE, id,"id");
		
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get single configuration info
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getConfigurationInfo(int id){
	
		Cursor cursor=	fuel_tracker_DB.getSingleEntries(DATABASE_TABLE, "id",id);

		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get all configuration info
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getAllConfigurationInfo(){
	
		Cursor cursor=	fuel_tracker_DB.getAllEntries(DATABASE_TABLE );

		return cursor;
	}
	
	
}
