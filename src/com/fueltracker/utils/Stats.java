package com.fueltracker.utils;

/**
 * @UiAuthor : Shashank.Paralkar
 * @date : 12/08/2011
 * @purpose : Functions to calculate and get various statistics as defined in
 *          Phase I
 * @ModifiedBy :ajay.sahani
 * @ModificationDate:19/08/2011
 * @Modification :function name and new method added  getStatsInstance() to maintain single instance of class
 * */

public class Stats {

	private static Stats stats=null;
	public Stats(){
		
	}
	
	public static Stats getStatsInstance(){
		
		if(stats==null){
			stats=new Stats();
		}
		return stats;
	}
	/*public float Calculate_Average_Consumption(
			float total_refilled_fuel_volume, int total_no_of_fillups) {

		float average_consumption = 0.0f;

		average_consumption = total_refilled_fuel_volume / total_no_of_fillups;

		return average_consumption;
	}*/

	/*public float getAverageCostPerMonth(int total_refilled_days,
			float total_fuel_cost_full_month) {

		float amount_per_day = 0.0f;
		float average_cost_month = 0.0f;

		amount_per_day = total_fuel_cost_full_month / (float)total_refilled_days;
		average_cost_month = amount_per_day * 30.0f;
		return average_cost_month;
	}*/
	
	public float getAverageCostPerMonth(float total_cost,
			float lastReading,float firstReading) {

		float average_cost_month = 0.0f;

		if((lastReading-firstReading)>0){
			average_cost_month = total_cost / (lastReading-firstReading);
		}
		
		return average_cost_month;
	}

	public float getTotalMileage(float last_date_odometer_reading,
			float first_date_odometer_reading, float total_volume) {

		float total_mileage = 0.0f;

		total_mileage = (last_date_odometer_reading - first_date_odometer_reading)
				/ total_volume;

		return total_mileage;
	}

	public float getTotalFuel(float total_fuel_fillups) {

		return total_fuel_fillups;

	}

	public float getAverageFuelCostKM(float last_date_odometer_reading,
			float first_date_odometer_reading,
			float total_refilled_fuel_volume, float total_cost_fuel,
			int no_of_fillup) {

		float total_mileage = 0.0f;
		float average_fuel_cost_km = 0.0f;
		float avg_cost = total_cost_fuel / no_of_fillup;

		total_mileage = getTotalMileage(last_date_odometer_reading,
				first_date_odometer_reading, total_refilled_fuel_volume);

		average_fuel_cost_km = (total_mileage * avg_cost) / no_of_fillup;
		System.out.println("average_fuel_cost_km: "+average_fuel_cost_km);
		return average_fuel_cost_km;

	}

}
