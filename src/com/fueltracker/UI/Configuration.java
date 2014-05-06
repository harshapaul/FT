package com.fueltracker.UI;

import com.fueltracker.FT_DB.ConfigurationAdapter;
import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @UiAuthor		: 	jitendra.chaure
 * @date			:	8/18/2011
 * @purpose			:	to display list of vehicles
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class Configuration extends Activity implements OnClickListener, HTTPResponseListener{

	final String TAG = getClass().getName();
	private Button saveConfig;
	private Spinner currencySpinner;
	private Spinner distSpinner;
	private Spinner fuelSpinner;
	private Spinner consumSpinner;
	private ArrayAdapter adapterCurr;
	private ArrayAdapter adapterDist;
	private ArrayAdapter adapterFuel;
	private ArrayAdapter adapterConsum;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        
        prepareUI();
        fillSpinners();
    }	
	
	private void prepareUI()
    {
		this.setTitle("Configuration");
		currencySpinner = (Spinner)findViewById(R.id.currtype);
		distSpinner = (Spinner)findViewById(R.id.disttype);
        fuelSpinner = (Spinner)findViewById(R.id.voltype);
        consumSpinner = (Spinner)findViewById(R.id.consmtype); 
        
        adapterCurr = ArrayAdapter.createFromResource(this,R.array.array_currency,R.layout.spinner_item);
        adapterCurr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapterCurr);
        adapterDist = ArrayAdapter.createFromResource(this, R.array.array_dist,R.layout.spinner_item);
        adapterDist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distSpinner.setAdapter(adapterDist);
        adapterFuel = ArrayAdapter.createFromResource(this, R.array.array_fuel,R.layout.spinner_item);
        adapterFuel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelSpinner.setAdapter(adapterFuel);        
        adapterConsum = ArrayAdapter.createFromResource(this, R.array.array_consum,R.layout.spinner_item);
        adapterConsum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        consumSpinner.setAdapter(adapterConsum);
        
        saveConfig = (Button)findViewById(R.id.saveconfig);
        saveConfig.setOnClickListener(this);
    }
	
	public void fillSpinners()
	{
		Log.i(TAG,"Filling configuration stored values in spinners ");
		ConfigurationAdapter configurationAdapter = new ConfigurationAdapter(getApplicationContext());
		Cursor cursor = configurationAdapter.getAllConfigurationInfo();
		Log.i(TAG,"Cursor count is " + cursor.getCount());
		if ( cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			Log.i(TAG,"Currency " + cursor.getString(cursor.getColumnIndex("currency")));
			Log.i(TAG,"Distance " + cursor.getString(cursor.getColumnIndex("distance_type")));
			Log.i(TAG,"Volume " + cursor.getString(cursor.getColumnIndex("volume_type")));
			Log.i(TAG,"Consumption " + cursor.getString(cursor.getColumnIndex("consumption_type")));
			int currpos = adapterCurr.getPosition(cursor.getString(cursor.getColumnIndex("currency")));
			currencySpinner.setSelection(currpos);
			int distpos = adapterDist.getPosition(cursor.getString(cursor.getColumnIndex("distance_type")));
			distSpinner.setSelection(distpos);
			int volpos = adapterFuel.getPosition(cursor.getString(cursor.getColumnIndex("volume_type")));
			fuelSpinner.setSelection(volpos);
			int consumpos = adapterConsum.getPosition(cursor.getString(cursor.getColumnIndex("consumption_type")));
			consumSpinner.setSelection(consumpos);
		}
	}
	
	private FuelTrackerAsync getAsynClassObject(String msg){
		
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(Configuration.this,msg);
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		
		return ftAsync;
	}
	
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
			case R.id.saveconfig:
				//Toast.makeText(getApplicationContext(),"Save Button clicked", Toast.LENGTH_SHORT).show();
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
				,currencySpinner.getSelectedItem().toString(),fuelSpinner.getSelectedItem().toString()
				,distSpinner.getSelectedItem().toString(),consumSpinner.getSelectedItem().toString(),1);
		
	}
	
	public void insertRecordInConfiguration(){
		
		FuelTrackerAsync ftAsync=getAsynClassObject("Please wait while saving record.");
		ftAsync.execute(FuelTrackerQueryCommand.INSERT_IN_CONFIGURATION_TABLE,FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST,getApplicationContext()
						,currencySpinner.getSelectedItem().toString(),fuelSpinner.getSelectedItem().toString()
						,distSpinner.getSelectedItem().toString(),consumSpinner.getSelectedItem().toString(),1);
		
	}

	public void onHTTPResponseProgress() {
		
	}
}
