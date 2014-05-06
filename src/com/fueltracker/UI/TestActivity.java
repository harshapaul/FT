package com.fueltracker.UI;

import java.util.Calendar;
import java.util.Date;

import com.fueltracker.FT_DB.CarAdapter;
import com.fueltracker.FT_DB.FuelRefillsAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//insertIntoCars();
		//insertIntoRefills();
		//checkLatestOdometer();
		//getStatsInformation();
		//getStatsInformation2();
		//getAccumulatedInfo();
		 //getPreviousFuelRefillDate();
		//deleteAllCar();
		//getAvgCostPerMonth();
		//setMileageNextRecord("2011-12-07 14:23:48",8,85);
		getFuelConsumptionPerMonth();
	}

	
	private void getFuelConsumptionPerMonth() {
		// TODO Auto-generated method stub
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.avgFuleConsumpPerMnt(1);
		float total_volume=0.0f;
		float avg_vol_per_mnth=0.0f;
		int no_of_mnth=0;
		if(cursor!=null){
			no_of_mnth=cursor.getCount();
			//int table_index=cursor.getColumnIndex("monthyear");
			int table_index2=cursor.getColumnIndex("monthyear");
			int table_index3=cursor.getColumnIndex("totalvolume");
			
			
		while(cursor.moveToNext()){
		
			total_volume+=cursor.getLong(table_index3);
		System.out.println("<-----monthyear---->"+cursor.getString(table_index2));
		System.out.println("<-----totalvolume---->"+cursor.getFloat(table_index3));
		
		}
	System.out.println("<-----total_volume---->"+total_volume);
	avg_vol_per_mnth=(total_volume/no_of_mnth);
		System.out.println("<-----avg_vol_per_mnth---->"+avg_vol_per_mnth);
		}
	}
	
	private void setMileageNextRecord(String date_of_refills,int delCarID,int fillUpId) {
		
		// TODO Auto-generated method stub
		date_of_refills=date_of_refills.trim();
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillUsingDate(
				date_of_refills, delCarID);
		float next_cur_fule_level=0.0f,next_mileage=0.0f;
		String next_odo_rading="";
		int next_id=-1,no=0;//no--no of row updated
		if (rowCursor != null && rowCursor.getCount() > 0) {

			rowCursor.moveToFirst();
				
				next_odo_rading = rowCursor.getString(rowCursor
						.getColumnIndex("date_of_refill"));
				next_cur_fule_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));							
		}	
		
		Log.i("date_of_refill--------", ""+next_odo_rading);
		Log.i("next_cur_fule_level--------", ""+next_cur_fule_level);	
		Log.i("date_of_refills--------", ""+date_of_refills);
		
	}
	
	
	private void getAvgCostPerMonth() {
		// TODO Auto-generated method stub
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(2);
		float total_cost=0.0f;
		float avg_cost_per_mnth=0.0f;
		int no_of_mnth=0;
		if(cursor!=null){
			no_of_mnth=cursor.getCount();
			//int table_index=cursor.getColumnIndex("monthyear");
			int table_index2=cursor.getColumnIndex("totalcost");
			int table_index3=cursor.getColumnIndex("totalmileage");
			int table_index4=cursor.getColumnIndex("totalvolume");
			
			
		while(cursor.moveToNext()){
		
			total_cost+=cursor.getLong(table_index2);
		System.out.println("<-----totalmileage---->"+cursor.getLong(table_index3));
		System.out.println("<-----totalvolume---->"+cursor.getFloat(table_index4));
		
		}
		System.out.println("<-----total_cost---->"+total_cost);
		avg_cost_per_mnth=(total_cost/no_of_mnth);
		System.out.println("<-----avg_cost_per_mnth---->"+avg_cost_per_mnth);
		}
	}

	private void insertIntoCars() {
		// TODO Auto-generated method stub
		CarAdapter carAdapter=new CarAdapter(getApplicationContext());
		for(int i=1;i<11;i++){
		carAdapter.insertInCarsTable("Rolls Royce"+i, "rr"+i, "km", "ltr", "km/ltr","note1",40);
		}
		
	}

	private void insertIntoRefills(int car_id) {
		// TODO Auto-generated method stub
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Date date=new Date(System.currentTimeMillis());
		//date.getYear()+"-0"+date.getMonth()+"-"+date.getDate()
		Calendar cal = Calendar.getInstance();
		     int day = cal.get(Calendar.DATE);
		    int month = cal.get(Calendar.MONTH) + 1;
		 int year = cal.get(Calendar.YEAR);
		
		 for(int i=1;i<11;i++){
	fuelRefillsAdapter.insertInFuelRefillsTable(year+"-0"+(month)+"-"+day+i+" "+date.getHours()+":"+date.getMinutes(),3896.0f,435.0f,
			"KG",65.0f,"RS",100.0f,4.0f,car_id,15,"note"+i);
		 }
	/*fuelRefillsAdapter.insertInFuelRefillsTable(year+"-0"+(month+1)+"-"+day+" "+date.getHours()+":"+(date.getMinutes()+5),33467.0f,436.0f,
			"KG",65.0f,"RS",200.0f,4.0f,2,16.5f,"note2");
	fuelRefillsAdapter.insertInFuelRefillsTable(year+"-"+(month+2)+"-"+day+" "+date.getHours()+":"+(date.getMinutes()+2),4365.0f,437.0f,
			"KG",65.0f,"RS",300.0f,4.0f,2,17.f,"note3");
	fuelRefillsAdapter.insertInFuelRefillsTable((year+1)+"-"+(month+2)+"-"+day+" "+date.getHours()+":"+(date.getMinutes()+3),9999.0f,438.0f,
			"KG",65.0f,"RS",400.0f,4.0f,2,18.5f,"note4");
		System.out.println("date.getYear()--------------"+year);*/
	}

	private void checkLatestOdometer(){
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getLatestOdometerReading();
		cursor.moveToFirst();
		int table_index=cursor.getColumnIndex("date_of_refill");
		int table_index2=cursor.getColumnIndex("odometer_reading");
		int table_index3=cursor.getColumnIndex("count(id)");
		int table_index4=cursor.getColumnIndex("sum(odometer_reading)");
		
		System.out.println("<-----date of refill---->"+cursor.getLong(table_index));
		System.out.println("<-----odometer reading---->"+cursor.getFloat(table_index2));
		System.out.println("<-----count(id)---->"+cursor.getInt(table_index3));
		System.out.println("<-----sum(odometer_reading)---->"+cursor.getFloat(table_index4));
	}
	
	private void getStatsInformation(){//to get avg volume
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getStatsData(1);
		
		System.out.println("<----- cursor size---->"+cursor.getCount());
		//int table_index=cursor.getColumnIndex("date_of_refill");
		//int table_index2=cursor.getColumnIndex("odometer_reading");
		int table_index3=cursor.getColumnIndex("count(id)");
		int table_index4=cursor.getColumnIndex("sum(odometer_reading)");
		int table_index5=cursor.getColumnIndex("sum(volume)");
		int table_index6=cursor.getColumnIndex("avg(volume)");
		
		while(cursor.moveToNext()){
		//System.out.println("<----- first date of refill---->"+cursor.getLong(table_index));
		//System.out.println("<-----first odometer reading---->"+cursor.getFloat(table_index2));
		System.out.println("<-----count(id)---->"+cursor.getInt(table_index3));//total_no_of_fillups
		System.out.println("<-----sum(odometer_reading)---->"+cursor.getFloat(table_index4));
		System.out.println("<-----sum(volume)---->"+cursor.getFloat(table_index5));// total_refilled_fuel_volume
		System.out.println("<-----avg(volume)---->"+cursor.getFloat(table_index6));
		
		//cursor.moveToLast();
		/*System.out.println("<----- last date of refill---->"+cursor.getLong(table_index));
		System.out.println("<-----last odometer reading---->"+cursor.getFloat(table_index2));
		System.out.println("<-----count(id)---->"+cursor.getInt(table_index3));
		System.out.println("<-----sum(odometer_reading)---->"+cursor.getFloat(table_index4));
		System.out.println("<-----sum(volume)---->"+cursor.getFloat(table_index5));*/
		}
	}
	
	private void getStatsInformation2(){//
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getStatsOdoDateinfo(1);
		
		System.out.println("<----- cursor size---->"+cursor.getCount());
		int table_index=cursor.getColumnIndex("date_of_refill");
		int table_index2=cursor.getColumnIndex("odometer_reading");
		//int table_index3=cursor.getColumnIndex("count(id)");
		//int table_index4=cursor.getColumnIndex("sum(odometer_reading)");
		//int table_index5=cursor.getColumnIndex("sum(volume)");
		
		cursor.moveToFirst();
		System.out.println("<----- first date of refill---->"+cursor.getString(table_index));
		System.out.println("<-----first odometer reading---->"+cursor.getFloat(table_index2));
		//System.out.println("<-----count(id)---->"+cursor.getInt(table_index3));
		//System.out.println("<-----sum(odometer_reading)---->"+cursor.getFloat(table_index4));
		//System.out.println("<-----sum(volume)---->"+cursor.getFloat(table_index5));
		
		cursor.moveToLast();
		System.out.println("<----- last date of refill---->"+cursor.getString(table_index));
		System.out.println("<-----last odometer reading---->"+cursor.getFloat(table_index2));
		//System.out.println("<-----count(id)---->"+cursor.getInt(table_index3));
		//System.out.println("<-----sum(odometer_reading)---->"+cursor.getFloat(table_index4));
		//System.out.println("<-----sum(volume)---->"+cursor.getFloat(table_index5));
		}
	
	private void getAccumulatedInfo(){//
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		
		Calendar cal = Calendar.getInstance();
	     int day = cal.get(Calendar.DATE);
	    int month = cal.get(Calendar.MONTH) + 1;
	 int year = cal.get(Calendar.YEAR);
	 
		Cursor cursor=fuelRefillsAdapter.getAccumulatedinfo(1, year, month);
		
		System.out.println("<----- cursor size---->"+cursor.getCount());
		int table_index=cursor.getColumnIndex("max(date_of_refill)");
		int table_index2=cursor.getColumnIndex("min(date_of_refill)");
		int table_index3=cursor.getColumnIndex("sum(volume)");
		//int table_index4=cursor.getColumnIndex("sum(odometer_reading)");
		//int table_index5=cursor.getColumnIndex("sum(volume)");
		
		cursor.moveToFirst();
		System.out.println("<----- max(date_of_refill)---->"+cursor.getString(table_index));
		System.out.println("<-----min(date_of_refill)---->"+cursor.getString(table_index2));
		System.out.println("<-----sum(volume)---->"+cursor.getFloat(table_index3));
		
		}
	
	
	private void  getPreviousFuelRefillDate(){//
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		
		Cursor cursor=fuelRefillsAdapter.getPreviousFuelRefillVolume(1);//1 is car id
		
		System.out.println("<----- cursor size---->"+cursor.getCount());
		int table_index=cursor.getColumnIndex("volume");
		int table_index2=cursor.getColumnIndex("date_of_refill");
		//int table_index5=cursor.getColumnIndex("sum(volume)");
		
		cursor.moveToFirst();
		System.out.println("<----- 1st volume---->"+cursor.getString(table_index));
		System.out.println("<----- 1st date---->"+cursor.getString(table_index2));
		
		cursor.moveToLast();
		System.out.println("<----- 2nd volume---->"+cursor.getString(table_index));
		System.out.println("<----- 2nd date---->"+cursor.getString(table_index2));
		
		}
	
	private void deleteAllCar(){
		CarAdapter carAdapter=new CarAdapter(getApplicationContext());
		int no=carAdapter.deleteAllCar();
		System.out.println("----no of car deleted-------");
	}
}
