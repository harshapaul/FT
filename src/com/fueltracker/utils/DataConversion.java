package com.fueltracker.utils;

import java.text.DecimalFormat;

/**
 * @UiAuthor : Ajay.Sahani
 * @date : 24/08/2011
 * @purpose : Functions to convert data from one unit to other unit
 *         
 * @ModifiedBy :
 * @ModificationDate:
 * @Modification :
 * */
public class DataConversion {

	private static DataConversion dataConversion=null;
	private DataConversion(){
		
	}
	
	public static DataConversion getStatsInstance(){
		
		if( dataConversion==null){
			 dataConversion=new DataConversion();
		}
		return  dataConversion;
	}

	public float roundTwoDecimals(double d) {
		 int Rpl=2;
		  float p = (float)Math.pow(10,Rpl);
		  d = d * p;
		  float tmp = Math.round(d);
		  return (float)tmp/p;
		  }
	
	
	//-------------------------------distanceConversion----------------------------------------------------//
	
	public float getMilesFromkm(float km){
		//1 kilometer = 0.621371192 miles
		return (float)roundTwoDecimals(km *  0.621371192f);
	}
	
	public float getKmFromMiles(float miles){
		//1 miles = 1.609344 kilometers
		return (float)roundTwoDecimals(miles *  1.609344f);
	}
	//-------------------------------volumeConversion()----------------------------------------------------//
	public float getLtrToGln(float ltr){
		//1 litres = 0.264172052 US gallons
		return (float)roundTwoDecimals(ltr *  0.264172052f);
	}
	
	public float getGlnToLtr(float gln){
		//1 US gallon = 3.78541178 liters
		return (float)roundTwoDecimals(gln *  3.78541178f);
	}
	
	public float getLtrToKilo(float ltr){
		//1 liter = 0.96 kilograms
		return (float)roundTwoDecimals(ltr *  0.96f);
	}
	
	public float getKiloToLtr(float kilo){
		//1 kilogram = 1.04 liters. 
		return (float)roundTwoDecimals(kilo *  1.04f);
	}
	
	//--------------------------------------------consumption type conversion-------------------------------------//
	public float getMi_per_ltrFromKm_per_ltr(float km_per_ltr){
		
		return (float)roundTwoDecimals(km_per_ltr * 0.621371192f);
	}
	
	public float getMi_per_glnFromKm_per_ltr(float km_per_ltr){
		
		return (float)roundTwoDecimals(km_per_ltr * 0.621371192f *   3.78541178f);
	}
	
	public float getKm_per_glnFromKm_per_ltr(float km_per_ltr){
		
		return (float)roundTwoDecimals(km_per_ltr *   3.78541178f);
	}
	
	public float getLtr_per_KmFromKm_per_ltr(float km_per_ltr){
		
		if(km_per_ltr==0){
			return 0.0f;
		}
		return (float)roundTwoDecimals(1/km_per_ltr )*1.0f;
	}
	
	public float getLtr_per_miFromKm_per_ltr(float km_per_ltr){
		
		if(km_per_ltr==0){
			return 0.0f;
		}
		return (float)roundTwoDecimals(1/getMi_per_ltrFromKm_per_ltr(km_per_ltr))*1.0f;
	}
	
	public float getGln_per_KmFromKm_per_ltr(float km_per_ltr){
		
		if(km_per_ltr==0){
			return 0.0f;
		}
		return (float)roundTwoDecimals(1/ getKm_per_glnFromKm_per_ltr(km_per_ltr))*1.0f;
	}
	
	public float getGln_per_miFromKm_per_ltr(float km_per_ltr){
		
		if(km_per_ltr==0){
			return 0.0f;
		}
		return (float)roundTwoDecimals(1/ getMi_per_glnFromKm_per_ltr(km_per_ltr))*1.0f;
	}
	
	
	
	
	
	public float convertConsumptionType(float mileage, String consumption_in) {
		// TODO Auto-generated method stub
		
		DataConversion dc=DataConversion.getStatsInstance();
		//float convert_mileage=0.0f;
		if(consumption_in.contentEquals("l/km"))	{
			return dc.getLtr_per_KmFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("mi/l")){
			return dc.getMi_per_ltrFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("l/mi")){
			return dc.getLtr_per_miFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("km/gal")){
			return dc.getKm_per_glnFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("gal/km")){
			return dc.getGln_per_KmFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("mi/gal")){
			return dc.getMi_per_glnFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("gal/mi")){
			return dc.getGln_per_miFromKm_per_ltr(mileage);
			
		}
		return  mileage;
	}
}


