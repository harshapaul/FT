package com.fueltracker.model;
/**
 * @UiAuthor		: 	jitendra.chaure
 * @date			:	8/10/2011
 * @purpose			:	to maintain vehicle features
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class ConfigurationItem {
	private Integer id;
	private String unit;
	private String value;
	
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getId()
	{
		return this.id;		
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	public String getUnit()
	{
		return this.unit;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getValue()
	{
		return this.value;
	}
	
}
