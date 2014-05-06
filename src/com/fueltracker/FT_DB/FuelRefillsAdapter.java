package com.fueltracker.FT_DB;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author			: 	ajay.sahani
 * @date			:	10th Aug 2011
 * @purpose			:	to maintain information about car_refills
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class FuelRefillsAdapter {

	// instance variable for BariDB class
	private FuelTrackerDB fuel_tracker_DB=null;
	// DATABASE_TABLE instance variable to hold table name
	private final String DATABASE_TABLE="fuel_refills";
	
	public FuelRefillsAdapter(Context context){
		fuel_tracker_DB = new FuelTrackerDB(context);

	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to create content value for table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:
	 * @Params			:fuel refills database attribute
	 * */
	
	private ContentValues createContentValues(String date_of_refills, float odometer_reading,float volume,
			String volume_type,float price,String currency_type,float total_cost,float current_fuel_level,int car_id,float mileage,String note){
		
		ContentValues values = new ContentValues();
		
		values.put("date_of_refill", date_of_refills);
		values.put("odometer_reading", odometer_reading);
		values.put("volume", volume);
		values.put("volume_type", volume_type);
		values.put("price",price);
		values.put("currency_type", currency_type);
		values.put("total_cost", total_cost);
		//values.put("tank_capacity", tank_capacity);
		values.put("current_fuel_level", current_fuel_level);
		values.put("car_id", car_id);
		values.put("mileage", mileage);
		values.put("note", note);
		
		return values;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to store information in fuel refill table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public long insertInFuelRefillsTable(String date_of_refills, float odometer_reading,float volume,
			String volume_type,float price,String currency_type,float total_cost,float current_fuel_level,int car_id,float mileage,String note) {
		
	
		ContentValues values = createContentValues(date_of_refills,odometer_reading,volume,volume_type,price,currency_type,
				total_cost,current_fuel_level,car_id,mileage,note);

		// now insert data in table
		
		long id= fuel_tracker_DB.insertEntry(DATABASE_TABLE, values);
		//fuel_tracker_DB.close();
		return id;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to update information in fuel refill  table
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public int updateFuelRefillsTable(int id,String date_of_refills, float odometer_reading,float volume,
			String volume_type,float price,String currency_type,float total_cost,float current_fuel_level,int car_id,float mileage,String note) {
		
		
		ContentValues values =  createContentValues(date_of_refills,odometer_reading,volume,volume_type,price,currency_type,
				total_cost,current_fuel_level,car_id,mileage,note);
		
		// now update data in table
		int no= fuel_tracker_DB.updateEntry(DATABASE_TABLE, id, values,"id");
		//fuel_tracker_DB.close();
		return no;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	delete fuel refill info
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public boolean  deleteFuelRefillsAccount(int fulRefilid){
		
		boolean flag= fuel_tracker_DB.removeEntry(DATABASE_TABLE, fulRefilid,"id");
		//fuel_tracker_DB.close();
		return flag;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	19th aug 2011
	 * @purpose			:	delete fuel refill info of particular car
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public boolean  delFulRefilUsingCarID(int car_id){
		
		boolean flag= fuel_tracker_DB.removeEntry(DATABASE_TABLE, car_id,"car_id");
		//fuel_tracker_DB.close();
		return flag;
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	19th aug 2011
	 * @purpose			:	delete fuel refill info of particular car
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public int  delAllFuleRefill(){
		
		int no= fuel_tracker_DB.removeAllEntry(DATABASE_TABLE);
		//fuel_tracker_DB.close();
		return no;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get single fuel refill info by using car_id
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getFuelRefillsInfo(int car_id){
	
		
		Cursor cursor=	fuel_tracker_DB.getAllFuelRefill(DATABASE_TABLE, car_id);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get single fuel refill info by using fuel refill id
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getFuelRefillsInfoUsingFulID(int ful_refill_id){
	
		
		Cursor cursor=	fuel_tracker_DB.getSingleEntries(DATABASE_TABLE, "id",ful_refill_id);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get all fuel refill info
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getAllFuelRefillsInfo(){
	
		
		Cursor cursor=	fuel_tracker_DB.getAllEntries(DATABASE_TABLE);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get latest odometer reading
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getLatestOdometerReading(){
	
		//select id,date_of_refill,odometer_reading,car_id,count(id) from fuel_refills group by car_id order  by date_of_refill desc;

		Cursor cursor=	fuel_tracker_DB.getLatestOdoReading(DATABASE_TABLE);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get latest odometer reading
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getLatestOdometerReadingUsingCar_ID(int car_id){
	
		//select id,date_of_refill,odometer_reading,car_id,count(id) from fuel_refills group by car_id order  by date_of_refill desc;

		Cursor cursor=	fuel_tracker_DB.getLatestOdometerReadingUsingCar_ID(DATABASE_TABLE,car_id);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get latest getFuelRefillUsingDate
	 * 						When the user changes date of a fillup the mileage will change as it is based on the immediate earlier row. This function will return cursor to the immediate earlier row.
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getFuelRefillUsingDate(String date_time, int car_id){
	
		//select id,date_of_refill,odometer_reading,car_id,count(id) from fuel_refills group by car_id order  by date_of_refill desc;

		Cursor cursor=	fuel_tracker_DB.getFuelRefillUsingDate(DATABASE_TABLE, "date_of_refill", date_time,car_id);

		//fuel_tracker_DB.close();
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
	
	public Cursor getFuelRefillUsingDateEdit(String dateOfRefills, int carId,
			int fillupId) {
		Cursor cursor=	fuel_tracker_DB.getFuelRefillUsingDateEdit(DATABASE_TABLE, "date_of_refill", dateOfRefills,carId,fillupId);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get latest getFuelRefillUsingDate next row(upper row to set mileage)
	 *`						 When the user changes date of a fillup the mileage will change as it is based on the immediate earlier row. This function will set the new mileage for the immediate upper row.
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getFuelRefillNextRow(String date_time, int car_id,int fillUpId){
	
		//select id,date_of_refill,odometer_reading,car_id,count(id) from fuel_refills group by car_id order  by date_of_refill desc;

		Cursor cursor=	fuel_tracker_DB. getFuelRefillNextRow(DATABASE_TABLE, date_time,car_id,fillUpId);

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	get latest getFuelRefillUsingDate next row(upper row to set mileage)
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public int setmileageNextRow(int id,float mileage){
	
		ContentValues values=new ContentValues();
		values.put("mileage", mileage);
		
		int no= fuel_tracker_DB.updateEntry(DATABASE_TABLE, id, values,"id");
		return no;
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	16th aug 2011
	 * @purpose			:	to get stats volume data
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor avgFuleConsumpPerMnt(int car_id){
	
		Cursor cursor=	fuel_tracker_DB.avgFuleConsumpPerMnt(DATABASE_TABLE, car_id);
		//System.out.println("cursor size--------"+cursor.getCount());

		//fuel_tracker_DB.close();
		return cursor;
	}
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	16th aug 2011
	 * @purpose			:	to get stats volume data
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getStatsData(int car_id){
	
		Cursor cursor=	fuel_tracker_DB.getStatsVolumeInfo(DATABASE_TABLE, car_id);
		//System.out.println("cursor size--------"+cursor.getCount());

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	16th aug 2011
	 * @purpose			:	to get cursor that contain 1st and last odometer reading along with 1st and last date
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getStatsOdoDateinfo(int car_id){
	
		Cursor cursor=	fuel_tracker_DB.getStatsOdo_dateInfo(DATABASE_TABLE, car_id);
		//System.out.println("cursor size--------"+cursor.getCount());

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	18th aug 2011
	 * @purpose			:	to get cursor that contain fuel refil info for given month
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	
	public Cursor getAccumulatedinfo(int car_id,int year,int mnth){
	
		Cursor cursor=	fuel_tracker_DB.getAccumulatedInfo(DATABASE_TABLE, car_id, year, mnth);
		//System.out.println("cursor size--------"+cursor.getCount());

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	18th aug 2011
	 * @purpose			:	to get cursor that contain fuel refil info to obtain 2nd last fuel refill volume
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getPreviousFuelRefillVolume( int car_id) {
		
		Cursor cursor=	fuel_tracker_DB.getPreviousFuelRefillVolume(DATABASE_TABLE, car_id);
		//System.out.println("cursor size--------"+cursor.getCount());

		//fuel_tracker_DB.close();
		return cursor;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	23 aug 2011
	 * @purpose			:	to get cursor that contain avg,total mileage ,total volume cost per month 
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public Cursor getAvgCostPermonth(int car_id){
		
		Cursor cursor=	fuel_tracker_DB.getAvgCostPermonth(DATABASE_TABLE, car_id);

		//fuel_tracker_DB.close();
		return cursor;
	}

	
	
}
