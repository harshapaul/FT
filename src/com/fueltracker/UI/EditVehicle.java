package com.fueltracker.UI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;
/**
 * @UiAuthor		: 	jitendra.chaure
 * @date			:	8/10/2011
 * @purpose			:	to edit vehicle details
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class EditVehicle extends Activity implements OnClickListener,HTTPResponseListener {
	
	final String TAG = getClass().getName();
	private Spinner distSpinner;
	private Spinner fuelSpinner;
	@Override
	public boolean onSearchRequested() 
	{
		return getParent().onSearchRequested();
	}

	private Spinner consumSpinner;
	private EditText make;
	private EditText model;
	private EditText note;
	private EditText tankCapacity;
	private Button save;
	private Button cancel;
	private ArrayAdapter adapterDist;
	private ArrayAdapter adapterFuel;
	private ArrayAdapter adapterConsum;
	private String mssgForFields="";
	private boolean fieldsCheck;
	private Integer recordIdToUpdate = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.editvehicle);
        View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.editvehicle, null);
        setContentView(viewToLoad);
        Log.i(TAG,"Record id to be updated " + this.getIntent().getIntExtra("RecordId", 0));
        recordIdToUpdate = this.getIntent().getIntExtra("RecordId",0);
        
        prepareUI();
        
        if ( recordIdToUpdate != 0 )
        {
        	FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(getParent(),"Please wait while retreiving record.");
    		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);    		
    		ftAsync.execute(FuelTrackerQueryCommand.GET_CARS_INFO,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext(),
					recordIdToUpdate);
        	
        }
		
    }
	
	private void prepareUI()
    {
		this.setTitle("Edit");
        distSpinner = (Spinner)findViewById(R.id.dist);
        fuelSpinner = (Spinner)findViewById(R.id.vol);
        consumSpinner = (Spinner)findViewById(R.id.consm);    
      
        adapterDist = ArrayAdapter.createFromResource(this, R.array.array_dist,R.layout.spinner_item);
        adapterDist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distSpinner.setAdapter(adapterDist); 
        
        adapterFuel = ArrayAdapter.createFromResource(this, R.array.array_fuel,R.layout.spinner_item);
        adapterFuel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelSpinner.setAdapter(adapterFuel); 
        
        adapterConsum = ArrayAdapter.createFromResource(this, R.array.array_consum,R.layout.spinner_item);
        adapterConsum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        consumSpinner.setAdapter(adapterConsum);
        
        make = (EditText)findViewById(R.id.make);
        model = (EditText)findViewById(R.id.model);
        note = (EditText)findViewById(R.id.note);
        tankCapacity = (EditText)findViewById(R.id.tank_capacity);
        
        save = (Button)findViewById(R.id.savebutton);
        cancel = (Button)findViewById(R.id.cancelbutton);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        
        setSpinnerWithDefaultValue();
    }
	
	public void setSpinnerWithDefaultValue()
	{
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(getParent(),"Setting default values.");
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		ftAsync.execute(FuelTrackerQueryCommand.GET_CONFIGURATION_INFO,FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST,
						getApplicationContext(),1);
	}
	
	public void onBackPressed(){
		
		Uri data = null;
		Intent result = new Intent(null, data);
		setResult(RESULT_OK, result);
		super.onBackPressed();
	}

	public void onClick(View arg0) {
		
		switch(arg0.getId()){
			case R.id.savebutton:
				if (fieldCheck())
				{		
					if ( recordIdToUpdate != 0 )
			        {
						updateVehicleInfo(recordIdToUpdate);
			        }
					else
					{
						saveVehicleInfo();
					}
					//Toast.makeText(getApplicationContext(),"Save Button clicked", Toast.LENGTH_SHORT).show();
				}
				else 
				{
					Toast.makeText(getApplicationContext(),mssgForFields, Toast.LENGTH_SHORT).show();
				}
			
				
				break;
			case R.id.cancelbutton:
				Uri data = null;
				Intent result = new Intent(null, data);
				setResult(RESULT_OK, result);
				finish();				
				break;
		}
	}
	
	public void updateVehicleInfo(Integer recordIdToUpdate)
	{
		String tempDistVal;
		String tempFuelVal;
		tempDistVal = distSpinner.getSelectedItem().toString().substring(distSpinner.getSelectedItem().toString().indexOf("(")+1,
				distSpinner.getSelectedItem().toString().indexOf(")"));
		tempFuelVal = fuelSpinner.getSelectedItem().toString().substring(fuelSpinner.getSelectedItem().toString().indexOf("(")+1,
				fuelSpinner.getSelectedItem().toString().indexOf(")"));
		
		FuelTrackerAsync ftAsync=getAsynClassObject();				
		ftAsync.execute(FuelTrackerQueryCommand.UPDATE_CARS_TABLE,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext(),
							recordIdToUpdate,make.getText().toString(),note.getText().toString(),model.getText().toString(),tempDistVal,
							tempFuelVal,consumSpinner.getSelectedItem().toString(),
							Float.parseFloat(tankCapacity.getText().toString().trim().length()!=0?tankCapacity.getText().toString().trim():"0.0"));
	}
	
	public void saveVehicleInfo()
	{
		String tempDistVal;
		String tempFuelVal;
		tempDistVal = distSpinner.getSelectedItem().toString().substring(distSpinner.getSelectedItem().toString().indexOf("(")+1,
				distSpinner.getSelectedItem().toString().indexOf(")"));
		tempFuelVal = fuelSpinner.getSelectedItem().toString().substring(fuelSpinner.getSelectedItem().toString().indexOf("(")+1,
				fuelSpinner.getSelectedItem().toString().indexOf(")"));
		
		FuelTrackerAsync ftAsync=getAsynClassObject();				
		ftAsync.execute(FuelTrackerQueryCommand.INSERT_IN_CARS_TABLE,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext(),
							make.getText().toString(),note.getText().toString(),model.getText().toString(),tempDistVal,
							tempFuelVal,consumSpinner.getSelectedItem().toString(),
							Float.parseFloat(tankCapacity.getText().toString().trim().length()!=0?tankCapacity.getText().toString().trim():"0.0"));
	}
	
	public boolean fieldCheck()
	{		
		float value=0.0f;
		try{
			value=Float.parseFloat(tankCapacity.getText().toString().trim());
		}catch(Exception error){
			error.printStackTrace();
		}
		
		if (make.getText().toString().trim().length() == 0)
		{
			mssgForFields = "Make field is empty";
			return false;
			
		}
		else if (note.getText().toString().trim().length() ==0)
		{
			mssgForFields = "Note field is empty";
			return false;
		}
		else if (model.getText().toString().trim().length() == 0)
		{
			mssgForFields = "Model field is empty";
			return false;
		}
		else if (tankCapacity.getText().toString().trim().length() == 0)
		{
			mssgForFields = "Tank capacity field should not be empty";
			return false;
		}else if(value<=0){
			mssgForFields = "Tank capacity should be greater than 0";
			return false;
		}else if(value>=501){
			mssgForFields = "Tank capacity should not be greater than 500";
			return false;
		}
		return true;
	}
	
	private FuelTrackerAsync getAsynClassObject(){
		
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(getParent(),"Please wait while saving record");
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		
		return ftAsync;
	}

	public void onHTTPResponseComplete(Object data, String request) {
		if(data!=null && request.contentEquals(FuelTrackerQueryCommand.INSERT_IN_CARS_TABLE)){
			long no=(Long)data;
			Log.i("INSERT_IN_CARS_TABLE","<-------record no---------->"+no);
			Uri dat = null;
			Intent result = new Intent(null, dat);
			setResult(RESULT_OK, result);
			finish();
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.UPDATE_CARS_TABLE)){
			int no = (Integer)data;
			Log.i("UPDATE_IN_CARS_TABLE"," Return value :"+no);
			Uri dat = null;
			Intent result = new Intent(null, dat);
			setResult(RESULT_OK, result);
			finish();
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_CARS_INFO)){
			Cursor cursor = (Cursor)data;
			fillValuesInFields(cursor);
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_CONFIGURATION_INFO)){
			Cursor cursor = (Cursor)data;
			fillDefaultValuesInSpinner(cursor);
		}
		
	}

	public void onHTTPResponseProgress() {
	}
	
	public void fillDefaultValuesInSpinner(Cursor cursor)
	{
		Log.i(TAG,"Cursor count to fill default values in spinner " + cursor.getCount());
		if ( cursor.getCount() != 0 ){			
			cursor.moveToFirst();
			Log.i(TAG,"currency " + cursor.getString(cursor.getColumnIndex("currency")));
			Log.i(TAG,"volume type " + cursor.getString(cursor.getColumnIndex("volume_type")));
			Log.i(TAG,"distance type " + cursor.getString(cursor.getColumnIndex("distance_type")));
			Log.i(TAG,"consumption type " + cursor.getString(cursor.getColumnIndex("consumption_type")));
			int pos = adapterDist.getPosition(cursor.getString(cursor.getColumnIndex("distance_type")).toString());
			distSpinner.setSelection(pos, true);
			int pos1 = adapterFuel.getPosition(cursor.getString(cursor.getColumnIndex("volume_type")));
			fuelSpinner.setSelection(pos1, true);
			int pos2 = adapterConsum.getPosition(cursor.getString(cursor.getColumnIndex("consumption_type")));
			consumSpinner.setSelection(pos2, true);
		}
	}
	
	public void fillValuesInFields( Cursor cursor ){
		
		if (cursor.moveToNext())
		{			
			make.setText(cursor.getString(cursor.getColumnIndex("make")));
			note.setText(cursor.getString(cursor.getColumnIndex("note")));
			model.setText(cursor.getString(cursor.getColumnIndex("model")));			
			
			if ( cursor.getString(cursor.getColumnIndex("distance_in")).equalsIgnoreCase("km") ){
				int pos = adapterDist.getPosition("Kilometers(km)");
				distSpinner.setSelection(pos, true);
			}
			else if ( cursor.getString(cursor.getColumnIndex("distance_in")).equalsIgnoreCase("mi") ){
				int pos = adapterDist.getPosition("Miles(mi)");
				distSpinner.setSelection(pos, true);
			}
			else if ( cursor.getString(cursor.getColumnIndex("distance_in")).equalsIgnoreCase("h") ){
				int pos = adapterDist.getPosition("Hours(h)");
				distSpinner.setSelection(pos, true);
			}
			
			if ( cursor.getString(cursor.getColumnIndex("volume_in")).equalsIgnoreCase("l") ){
				int pos = adapterFuel.getPosition("Litres(l)");
				fuelSpinner.setSelection(pos, true);
			}
			else if ( cursor.getString(cursor.getColumnIndex("volume_in")).equalsIgnoreCase("gal") ){
				int pos = adapterFuel.getPosition("Gallons(gal)");
				fuelSpinner.setSelection(pos, true);
			}
			else if ( cursor.getString(cursor.getColumnIndex("volume_in")).equalsIgnoreCase("kg") ){
				int pos = adapterFuel.getPosition("KiloGram(kg)");
				fuelSpinner.setSelection(pos, true);
			}
			
			int pos = adapterConsum.getPosition(cursor.getString(cursor.getColumnIndex("consumption_in")));
			consumSpinner.setSelection(pos, true);			
			
			tankCapacity.setText(cursor.getString(cursor.getColumnIndex("tank_capacity")));
		}
		
	}
}
