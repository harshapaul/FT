package com.fueltracker.model;

import java.util.Date;

/**
 * @author : ruchi.malvankar
 * @date : 10th August 2011
 * @purpose : To create model class for FuelRefills
 * */

public class FuelRefills {
	private Date dateOfRefill;
	private float odometerReading;
	private float volume;
	private String volumeType;
	private float price ;
	private String currencyType;
	private float totalCost;
	private float currentFuelLevel;
	private int carID,id;
	private int days;
	private float odometerReading2;
	private float mileage;
	private String note;
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getMileage() {
		return mileage;
	}
	public void setMileage(float mileage) {
		this.mileage = mileage;
	}
	public float getOdometerReading2() {
		return odometerReading2;
	}
	public void setOdometerReading2(float odometerReading2) {
		this.odometerReading2 = odometerReading2;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Date getDateOfRefill() {
		return dateOfRefill;
	}
	public void setDateOfRefill(Date dateOfRefill) {
		this.dateOfRefill = dateOfRefill;
	}
	public float getOdometerReading() {
		return odometerReading;
	}
	public void setOdometerReading(float odometerReading) {
		this.odometerReading = odometerReading;
	}
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public String getVolumeType() {
		return volumeType;
	}
	public void setVolumeType(String volumeType) {
		this.volumeType = volumeType;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public float getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}
	public float getCurrentFuelLevel() {
		return currentFuelLevel;
	}
	public void setCurrentFuelLevel(float currentFuelLevel) {
		this.currentFuelLevel = currentFuelLevel;
	}
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
}
