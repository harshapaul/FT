package com.fueltracker.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @UiAuthor		: 	jitendra.chaure
 * @date			:	8/10/2011
 * @purpose			:	to maintain vehicle features
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class VehicleItem {
	private Integer id;
	private String make;
	private String model;
	private String carName;
	private String distance;
	private String volume; 
	private String consumption;
	private String distCovered;
	private String fillups;
	private String consumValue;
	private float tank_capacity;
	private String note;
	
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public float getTank_capacity() {
		return tank_capacity;
	}
	public void setTank_capacity(float tankCapacity) {
		tank_capacity = tankCapacity;
	}
	
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setMake(String make)
	{
		this.make = make;
	}
	public String getMake()
	{
		return this.make;
	}    	
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getModel()
	{
		return this.model;
	}    	
	public void setDistance(String distance)
	{
		this.distance = distance;
	}
	public String getDistance()
	{
		return this.distance;
   	}
	public void setVolume(String volume)
	{
		this.volume = volume;
	}
	public String getVolume()
	{
		return this.volume;
	}
	public void setConsumption(String consumption)
	{
		this.consumption = consumption;
	}
	public String getConsumption()
	{
		return this.consumption;
	}
	public void setDistCovered(String distCovered)
	{
		this.distCovered = distCovered;
	}
	public String getDistCovered()
	{
		return this.distCovered;
	}
	public void setFillUps(String fillups)
	{
		this.fillups = fillups;
	}
	public String getFillUps()
	{
		return this.fillups;
	}
	public void setConsumValue(String consumValue)
	{
		this.consumValue = consumValue;
	}
	public String getConsumValue()
	{
		return this.consumValue;
	}
	public String toString()
	{
		return getMake()+" "+getModel();
	}
	
	
}
