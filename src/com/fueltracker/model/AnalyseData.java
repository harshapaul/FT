package com.fueltracker.model;
/**
 * @author : ruchi.malvankar
 * @date : 24th August 2011
 * @purpose : To create model class for Analysis
 * */
public class AnalyseData {
	int carId;
	float avgFuelConsumption;
	float avgCostPerMonth;
	float totalMileage;
	float totalFuel;
	float avgFuelCostPerKM;
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public float getAvgFuelConsumption() {
		return avgFuelConsumption;
	}
	public void setAvgFuelConsumption(float avgFuelConsumption) {
		this.avgFuelConsumption = avgFuelConsumption;
	}
	public float getAvgCostPerMonth() {
		return avgCostPerMonth;
	}
	public void setAvgCostPerMonth(float avgCostPerMonth) {
		this.avgCostPerMonth = avgCostPerMonth;
	}
	public float getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(float totalMileage) {
		this.totalMileage = totalMileage;
	}
	public float getTotalFuel() {
		return totalFuel;
	}
	public void setTotalFuel(float totalFuel) {
		this.totalFuel = totalFuel;
	}
	public float getAvgFuelCostPerKM() {
		return avgFuelCostPerKM;
	}
	public void setAvgFuelCostPerKM(float avgFuelCostPerKM) {
		this.avgFuelCostPerKM = avgFuelCostPerKM;
	}
}
