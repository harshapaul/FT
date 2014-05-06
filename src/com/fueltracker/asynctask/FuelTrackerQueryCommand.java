package com.fueltracker.asynctask;

public class FuelTrackerQueryCommand {

	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	store command depending upon that query get called
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	
	//define constant to identify request type
	public static final String CARS_TABLE_REQUEST="cars_table_request";
	public static final String FUEL_REFILLS_TABLE_REQUEST="fuel_refills_table_request";
	public static final String CONFIGURATION_TABLE_REQUEST="configuration_table_request";
	
	//for cars table 
	public static final String INSERT_IN_CARS_TABLE="insert_in_cars_table";
	public static final String UPDATE_CARS_TABLE="update_cars_table";
	public static final String DELETE_CARS_ACCOUNT="delete_car_account";
	public static final String GET_CARS_INFO="get_car_info";
	public static final String GET_ALL_CARS_INFO="get_all_car_info";
	public static final String DELETE_ALL_CAR="delete_all_car";
	
	//for fuel refils table
	public static final String INSERT_IN_FUEL_REFILS_TABLE="insert_in_FuelRefills_table";
	public static final String UPDATE_FUEL_REFILS_TABLE="update_FuelRefills_table";
	public static final String DELETE_FUEL_REFILS_ACCOUNT="delete_FuelRefills_account";
	public static final String GET_FUEL_REFILS_INFO="get_FuelRefills_info";
	public static final String GET_ALL_FUEL_REFILS_INFO="get_all_FuelRefills_info";
	public static final String GET_LATEST_ODOMETER_READING="get_Latest_Odometer_Reading";
	public static final String GET_FUEL_REFILS_INFO_USINGFUELID="getFuelRefillsInfoUsingFulID";
	public static final String GET_FUEL_REFILS_USINGDATE="getFuelRefillUsingDate";
	public static final String GET_FUEL_REFILS_STATS_VOLUME_DATA="getStatsVolumeData";
	public static final String GET_FUEL_REFILS_STATS_ODO_DATE_DATA="getStatsOdoDateInfo";//delAllFuleRefill
	public static final String GET_FUEL_REFILS_ACCUMULATED_INFO="getAccumulatedinfo";
	public static final String DEL_FULE_REFIL_USING_CAR_ID="delFulRefilUsingCarID";
	public static final String DEL_ALL_FULE_REFILL="delAllFuleRefill";
	
	//for configuration table
	public static final String INSERT_IN_CONFIGURATION_TABLE="insert_in_Configuration_table";
	public static final String UPDATE_CONFIGURATION_TABLE="update_Configuration_table";
	public static final String DELETE_CONFIGURATION_ACCOUNT="delete_Configuration_account";
	public static final String GET_CONFIGURATION_INFO="get_Configuration_info";
	public static final String GET_ALL_CONFIGURATION_INFO="get_all_Configuration_info";
}
