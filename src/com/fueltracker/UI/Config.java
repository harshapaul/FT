package com.fueltracker.UI;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fueltracker.FT_DB.ConfigurationAdapter;
import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;
import com.fueltracker.model.ConfigurationItem;


/**
 * @UiAuthor		: 	jitendra.chaure
 * @date			:	8/23/2011
 * @purpose			:	to display list of vehicles
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class Config extends Activity implements OnClickListener,OnItemClickListener, HTTPResponseListener{

	final String TAG = getClass().getName();
	private Button saveConfig;
	private ListView configListView;
	private ArrayList<ConfigurationItem> configList = new ArrayList<ConfigurationItem>();
	private ListAdapter configListAdapter;
	private String currencyType="";
	private String distanceType="";
	private String fuelType="";
	private String consumptionType="";
	private int currencyTypePos=0, distanceTypePos=0, volumeTypePos=0, consumptionTypePos=0;	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        
        prepareUI();
        
    }	
	
	private void prepareUI()
    {
		this.setTitle("Configuration");		
        configListView = (ListView)findViewById(R.id.listinconfiguration);
        configListAdapter = new ListAdapter(this,R.layout.configurationrow,configList);
        configListView.setAdapter(configListAdapter);
        fillList();
        //configListView.setOnItemClickListener(this);
        saveConfig = (Button)findViewById(R.id.saveconfiguration);
        saveConfig.setOnClickListener(this);
    }
	
	public void fillList()
	{
		ConfigurationAdapter configurationAdapter = new ConfigurationAdapter(getApplicationContext());
		Cursor cursor = configurationAdapter.getAllConfigurationInfo();
		
		if ( cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			ConfigurationItem co1 = new ConfigurationItem();
			co1.setId(1);
			co1.setUnit("Currency");
			co1.setValue(cursor.getString(cursor.getColumnIndex("currency")));
			configListAdapter.items.add(co1);
			currencyType = cursor.getString(cursor.getColumnIndex("currency"));
			
			String [] currencyTypeList = getResources().getStringArray(R.array.array_currency);
			for ( int i=0 ; i< currencyTypeList.length; i++)
			{
				if ( currencyType.equals(currencyTypeList[i]))
				{
					currencyTypePos = i;
				}
			}			
			
			ConfigurationItem co2 = new ConfigurationItem();
			co2.setId(2);
			co2.setUnit("Distance");
			co2.setValue(cursor.getString(cursor.getColumnIndex("distance_type")));
			configListAdapter.items.add(co2);
			distanceType = cursor.getString(cursor.getColumnIndex("distance_type"));
	
			String [] distanceTypeList = getResources().getStringArray(R.array.array_dist);
			for( int i=0; i< distanceTypeList.length; i++)
			{
				if ( distanceType.equals(distanceTypeList[i]) )
				{
					distanceTypePos = i;
				}
			}
			
			ConfigurationItem co3 = new ConfigurationItem();
			co3.setId(3);
			co3.setUnit("Volume");
			co3.setValue(cursor.getString(cursor.getColumnIndex("volume_type")));
			configListAdapter.items.add(co3);
			fuelType = cursor.getString(cursor.getColumnIndex("volume_type"));
			
			String [] volumeTypeList = getResources().getStringArray(R.array.array_fuel);	
			for ( int i=0; i< volumeTypeList.length; i++ )
			{
				if ( fuelType.equals(volumeTypeList[i]) )
				{
					volumeTypePos = i;
				}
			}
			
			ConfigurationItem co4 = new ConfigurationItem();
			co4.setId(4);
			co4.setUnit("Consumption");
			co4.setValue(cursor.getString(cursor.getColumnIndex("consumption_type")));
			configListAdapter.items.add(co4);
			consumptionType = cursor.getString(cursor.getColumnIndex("consumption_type"));
			
			String [] consumptionTypeList = getResources().getStringArray(R.array.array_consum);
			for ( int i=0; i< consumptionTypeList.length; i++)
			{
				if ( consumptionType.equals(consumptionTypeList[i]) )
				{
					consumptionTypePos = i;
				}
			}
			
			configListAdapter.notifyDataSetChanged();
			
		}
		else
		{
			ConfigurationItem co1 = new ConfigurationItem();
			co1.setId(1);
			co1.setUnit("Currency");
			co1.setValue("USD - United States dollar");
			configListAdapter.items.add(co1);
			currencyType = "USD - United States dollar";
	
			ConfigurationItem co2 = new ConfigurationItem();
			co2.setId(2);
			co2.setUnit("Distance");
			co2.setValue("Kilometers(km)");
			configListAdapter.items.add(co2);
			distanceType = "Kilometers(km)";
	
			ConfigurationItem co3 = new ConfigurationItem();
			co3.setId(3);
			co3.setUnit("Volume");
			co3.setValue("Litres(l)");
			configListAdapter.items.add(co3);
			fuelType="Litres(l)";
			
			ConfigurationItem co4 = new ConfigurationItem();
			co4.setId(4);
			co4.setUnit("Consumption");
			co4.setValue("km/l");
			configListAdapter.items.add(co4);
			consumptionType = "km/l";
			
			configListAdapter.notifyDataSetChanged();
		}
		
		
	}
	
	private class ListAdapter extends ArrayAdapter<ConfigurationItem>{

		private LinearLayout configLayout;
		private ArrayList<ConfigurationItem> items;
		private ConfigurationItem o;
		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<ConfigurationItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		public View getView(int position, View convertView, ViewGroup parent) 
		{
									
			return FillDataForListItem(position, convertView);			

		}
		
		private View FillDataForListItem(int position, View convertView)
		{
			o = items.get(position);
			if (o != null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.configurationrow, null);
				
				LinearLayout ll= (LinearLayout) convertView.findViewById(R.id.configlayout);
				if(position!=0 && position%2!=0)
					ll.setBackgroundResource(R.drawable.selector_odd);
				final TextView unitTextView = (TextView)convertView.findViewById(R.id.configunit);
				final TextView valueTextView = (TextView)convertView.findViewById(R.id.configvalue);
				unitTextView.setText(o.getUnit());
				valueTextView.setText(o.getValue());
				configLayout = (LinearLayout)convertView.findViewById(R.id.configlayout);
				configLayout.setId(o.getId());
				configLayout.setOnClickListener(new OnClickListener(){			
					public void onClick(View arg0) {
						Log.i(TAG,"Linear layout clicked " +  arg0.getId());
						switch(arg0.getId()){
							case 1:								
					        	 new AlertDialog.Builder(Config.this)
					            .setTitle("Currency")
					            .setSingleChoiceItems(R.array.array_currency, currencyTypePos, new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                    
					                    String[] items = getResources().getStringArray(R.array.array_currency);	
					                    Log.i(TAG,"Item selected is " + items[which]);
					                    valueTextView.setText(items[which]);
					                    currencyType = items[which];
					                    currencyTypePos = which;
					                    dialog.cancel();
					                }
					            })					            
					            .create().show();
								break;
							case 2:
								new AlertDialog.Builder(Config.this)
					            .setTitle("Distance")
					            .setSingleChoiceItems(R.array.array_dist, distanceTypePos, new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                    
					                    String[] items = getResources().getStringArray(R.array.array_dist);	
					                    Log.i(TAG,"Item selected is " + items[which]);
					                    valueTextView.setText(items[which]);
					                    distanceType = items[which];
					                    distanceTypePos = which;
					                    dialog.cancel();
					                }
					            })					            					            
					            .create().show();
								break;
							case 3:
								new AlertDialog.Builder(Config.this)
					            .setTitle("Volume")
					            .setSingleChoiceItems(R.array.array_fuel, volumeTypePos,  new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                    
					                    String[] items = getResources().getStringArray(R.array.array_fuel);	
					                    Log.i(TAG,"Item selected is " + items[which]);
					                    valueTextView.setText(items[which]);
					                    fuelType = items[which];
					                    volumeTypePos = which;
					                    dialog.cancel();
					                }
					            })					            					            
					            .create().show();
								break;
							case 4:
								new AlertDialog.Builder(Config.this)
					            .setTitle("Consumption")
					            .setSingleChoiceItems(R.array.array_consum, consumptionTypePos,  new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                    
					                    String[] items = getResources().getStringArray(R.array.array_consum);	
					                    Log.i(TAG,"Item selected is " + items[which]);
					                    valueTextView.setText(items[which]);
					                    consumptionType = items[which];
					                    consumptionTypePos = which;
					                    dialog.cancel();
					                }
					            })					            					            
					            .create().show();
								break;
						}
						
					}});
				
				
				return convertView;
			}
			return null;
		}		
	}
			

	private FuelTrackerAsync getAsynClassObject(String msg){
		
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(Config.this,msg);
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		
		return ftAsync;
	}
	
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
			case R.id.saveconfiguration:				
				FuelTrackerAsync ftAsync=getAsynClassObject("Please wait while checking record.");
				ftAsync.execute(FuelTrackerQueryCommand.GET_ALL_CONFIGURATION_INFO,FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST,getApplicationContext());
				break;
		}
		
	}

	public void onHTTPResponseComplete(Object data, String request) {
		
		if ( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_ALL_CONFIGURATION_INFO)){
			Cursor cursor = (Cursor)data;
			Log.i(TAG,"No of records in configuration table " + cursor.getCount());
			if ( cursor.getCount()==0)
			{
				insertRecordInConfiguration();
			}
			else {
				updateRecordInConfiguration();
			}
				
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.INSERT_IN_CONFIGURATION_TABLE)){
			long no = (Long)data;
			Log.i(TAG,"No value after inserting record in configuration " +  no);
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.UPDATE_CONFIGURATION_TABLE)){
			int no = (Integer)data;
			Log.i(TAG,"No value after updating record in configuration " + no);
		}
		
	}
	
	public void updateRecordInConfiguration(){
		
		FuelTrackerAsync ftAsync=getAsynClassObject("Please wait while saving record.");
		ftAsync.execute(FuelTrackerQueryCommand.UPDATE_CONFIGURATION_TABLE,FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST,getApplicationContext(),1
				,currencyType,fuelType,distanceType,consumptionType,1);
		
	}
	
	public void insertRecordInConfiguration(){
		
		FuelTrackerAsync ftAsync=getAsynClassObject("Please wait while saving record.");
		ftAsync.execute(FuelTrackerQueryCommand.INSERT_IN_CONFIGURATION_TABLE,FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST,getApplicationContext()
						,currencyType,fuelType,distanceType,consumptionType,1);
		
	}

	public void onHTTPResponseProgress() {
		
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch(arg0.getId()){
			case R.id.listinconfiguration:
				Log.i(TAG,"Item selected : "+ arg0.getId() +" "+ arg1.getId() +" " + arg2 + " " + arg3);
				
		}
		
	}
}
