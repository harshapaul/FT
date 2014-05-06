package com.fueltracker.UI;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fueltracker.FT_DB.FuelRefillsAdapter;
import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;
import com.fueltracker.utils.DataConversion;
import com.fueltracker.utils.Stats;

/**
 * @UiAuthor : ruchi.malvankar
 * @date : 8/10/2011
 * @purpose : To add/edit fill-ups
 * @ModifiedBy :
 * @ModificationDate:
 * @Modification :
 * */
public class AddFillup extends Activity implements HTTPResponseListener,
		OnClickListener, OnKeyListener {

	@Override
	public boolean onSearchRequested() 
	{
		return getParent().onSearchRequested();
	}

	private final String TAG = getClass().getName();
	private Button save, cancel, dateOfRefills, now;
	private EditText odometer, fuel, volumePrice, totalCost, note, currentfuel;
	private TextView disttype, voltype, currencyvoltype, currencytype,
			currtvoltype;
	private int carId, fillupId = -1;;
	private Calendar selectedDate;
	private String distance_in,volume_in,consumption_in,error = null,currencyType="",initialDate="";;
	private DateFormat dateFormat;
	private float tankCapacity;
	private boolean edit=false, total_vol_check = true;// true means total volume is
													// not negative ,if it come
													// -ve than we make it false
	private boolean flag=true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.addfillup);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.addfillup, null);
        setContentView(viewToLoad); 
		loadUI();
	}

	public void loadUI() {
        disttype = (TextView) findViewById(R.id.disttype);
        voltype = (TextView) findViewById(R.id.voltype);
        currencyvoltype = (TextView) findViewById(R.id.currencyvoltype);
        currencytype = (TextView) findViewById(R.id.currencytype);
        currtvoltype = (TextView) findViewById(R.id.currtvoltype);
        save = (Button) findViewById(R.id.save);
        now = (Button) findViewById(R.id.now);
        cancel = (Button) findViewById(R.id.cancel);
        dateOfRefills = (Button) findViewById(R.id.dateRefill);
        odometer = (EditText) findViewById(R.id.odometer);
        fuel = (EditText) findViewById(R.id.volume);
        volumePrice = (EditText) findViewById(R.id.price);
        totalCost = (EditText) findViewById(R.id.totalcost);
        note = (EditText) findViewById(R.id.note);
        currentfuel = (EditText) findViewById(R.id.currentfuel);
        note = (EditText) findViewById(R.id.note);

        Intent intent = getIntent();
        setTodaysDate();
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");//Hour in day (1-24) 
        getIntentData(intent);

   }

	private void getIntentData(Intent intent){
		
		String dateValue ="";
		if (intent.getExtras() != null) {
			carId = intent.getIntExtra("carId", carId);
			tankCapacity = intent.getFloatExtra("tankCapacity", 0);
			distance_in=intent.getStringExtra("distance_in");
			volume_in=intent.getStringExtra("volume_in");
			consumption_in=intent.getStringExtra("consumption_in");
			currencyType=intent.getStringExtra("currency_type");
			
			Log.i("currencyType:" ,""+ currencyType);
			Log.i("tankCapacity:" ,""+ tankCapacity);
			Log.i("distance_in:" ,""+ distance_in);
			Log.i("volume_in:" ,""+ volume_in);
			Log.i("consumption_in:" ,""+ consumption_in);

			disttype.setText(distance_in);
			voltype.setText(volume_in);
			
			currencyvoltype.setText(currencyType+"/"+volume_in);
			currtvoltype.setText(volume_in);
			
			fillupId = intent.getIntExtra("fillupId", carId);
			edit = intent.getBooleanExtra("edit", edit);

			initialDate=  dateValue = intent.getStringExtra("dateOfRefill");
			Log.i("dateV:" ,""+ dateValue);
			
			String odometerV = intent.getStringExtra("odometer");
			String volumeV = intent.getStringExtra("volume");
			String priceV = intent.getStringExtra("price");
			String totalCostV = intent.getStringExtra("totalCost");
			String fuelLevelV = intent.getStringExtra("fuelLevel");
			String noteV = intent.getStringExtra("note");
			if (dateValue != null) {
				try {
					selectedDate.setTime(dateFormat.parse(dateValue));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				dateOfRefills.setText(selectedDate.get(Calendar.DAY_OF_MONTH)
						+ "/" + (selectedDate.get(Calendar.MONTH) + 1) + "/"
						+ selectedDate.get(Calendar.YEAR));
			}
			if (odometerV != null){
				odometer.setText("" + odometerV);
				
			}
			if (volumeV != null){
				fuel.setText("" + volumeV);
				
			}
			if (priceV != null)
				volumePrice.setText("" + priceV);
			if (totalCostV != null)
				totalCost.setText("" + totalCostV);
			if (fuelLevelV != null){
				currentfuel.setText("" + fuelLevelV);
				
			}
			if (noteV != null)
				note.setText("" + noteV);

		}

		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		fuel.setOnKeyListener(this);
		volumePrice.setOnKeyListener(this);
		dateOfRefills.setOnClickListener(this);
		now.setOnClickListener(this);

		if(edit){
			currencyvoltype.setText(currencyType + "/" + volume_in);
			currencytype.setText(currencyType);
			
		}else{
			//after 1st entry
			int no=setCurrencyType(dateOfRefills.getText().toString());
			Log.i("setCurrencyType--","no:"+no);
			//for first entry only
			if(no==0){
			setTextViewWithDefaultValues();
			}
		}
		
	}
	
	//set currency type for the fillup entry which come after 1st
	private int setCurrencyType(String dateOfRefills) {
		// TODO Auto-generated method stub
		
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		Cursor rowCursor=null;
		
		 rowCursor = fuelRefillsAdapter.getFuelRefillsInfo(carId);
	
		if (rowCursor != null && rowCursor.getCount() > 0) {

			rowCursor.moveToFirst();				
				currencyType = rowCursor.getString(
						rowCursor.getColumnIndex("currency_type"));
			Log.i("setCurrencyType--","currencyType:"+currencyType);
			
			currencyvoltype.setText(currencyType + "/" + volume_in);
			currencytype.setText(currencyType);
		}
				
		return rowCursor.getCount();
	}

	/**
	 * @author			: 	ajay.sahani
	 * @date			:	30th aug 2011
	 * @purpose			:	get latest getFuelRefillUsingDate
	 * 						When the user changes date of a fillup the mileage will change as it is based on the immediate earlier row. This function will return cursor to the immediate earlier row.
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	//set mileage of next row on basis of initial date
	private void setPrevMileage(String dateValue) {
		// TODO Auto-generated method stub
		
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillUsingDateEdit(
				dateValue, carId,fillupId);
		
		String date="";
		float prev_date_odometer_reading = 0,  prev_volume = 0.0f, pre_fule_level = 0.0f;

		if (rowCursor != null && rowCursor.getCount() > 0) {
			Log.i("initial_date_odometer_reading::","setPrevMileage");
			rowCursor.moveToFirst();

			if (rowCursor.getString(rowCursor.getColumnIndex("date_of_refill")) != null) {
				
				prev_date_odometer_reading = rowCursor.getFloat(rowCursor
						.getColumnIndex("odometer_reading"));
				prev_volume = rowCursor.getFloat(rowCursor
						.getColumnIndex("volume"));
				pre_fule_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));
				
				currencyType=rowCursor.getString(rowCursor.getColumnIndex("currency_type"));
				//date=rowCursor.getString(rowCursor.getColumnIndex("date_of_refill"));
				Log.i("initial_date_odometer_reading::",""+prev_date_odometer_reading);
				Log.i("prev_volume::",""+prev_volume);
				Log.i("pre_fule_level::",""+pre_fule_level);
				
				//if(prev_date_odometer_reading!=0){
					//setMileageNextRecord(dateValue,prev_date_odometer_reading, prev_volume,pre_fule_level);
							
				//}
			}
		}
		setMileageNextRecord(dateValue,prev_date_odometer_reading, prev_volume,pre_fule_level);
		
		
	}

	public void setTextViewWithDefaultValues() {
		FuelTrackerProgressBar ftpBar = new FuelTrackerProgressBar(
				getParent(), "Setting default values.");
		FuelTrackerAsync ftAsync = new FuelTrackerAsync(this, ftpBar);
		ftAsync.execute(FuelTrackerQueryCommand.GET_CONFIGURATION_INFO,
				FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST,
				getApplicationContext(), 1);
	}

	public void setTodaysDate() {
		selectedDate = Calendar.getInstance();

		dateOfRefills.setText(selectedDate.get(Calendar.DATE) + "/"
				+ (selectedDate.get(Calendar.MONTH) + 1) + "/"
				+ selectedDate.get(Calendar.YEAR));
	}

	public void showDatePickerDailog() {
		View view;
		AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
		final String defa = "Select Date";
		LayoutInflater factory3 = LayoutInflater.from(this);
		view = factory3.inflate(R.layout.date_dialog, null);
		alert.setTitle(defa);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.datepicker);

		datePicker.init(selectedDate.get(Calendar.YEAR), selectedDate
				.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), null);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int day = datePicker.getDayOfMonth();
				int month = datePicker.getMonth();
				int year = datePicker.getYear();

				selectedDate.set(Calendar.DATE, day);
				selectedDate.set(Calendar.MONTH, month);
				selectedDate.set(Calendar.YEAR, year);
				// System.out.println(selectedDate);
				dateOfRefills.setText(day + "/" + (month + 1) + "/" + year);
			}
		});
		alert.setView(view);

		alert.show();
	}

	public void saveFillup() {
		total_vol_check = true;
		
		String date_of_refills, volume_type = "", currency_type = "", noteValue;
		float mileage = 0.0f, odometer_reading = 0.0f, volume = 0.0f, price = 0.0f, total_cost = 0.0f, current_fuel_level = 0.0f;
		date_of_refills = dateOfRefills.getText().toString();
		error = null;
		noteValue = note.getText().toString();

		odometer_reading = validateValues(odometer, "odometer");
		//Log.i("saveFillup::odometer_reading before",""+odometer_reading);
		//Log.i("saveFillup:: distance_in",""+ distance_in);
		
		if(error == null && distance_in.contentEquals("mi"))	{
			odometer_reading=DataConversion.getStatsInstance().getKmFromMiles(odometer_reading);
			//Log.i("saveFillup::odometer_reading",""+odometer_reading);
		}

		if (error == null) {
			volume = validateValues(fuel, "fuel");// user should not enter volume more than its tank capacity
			
				if(volume_in.contentEquals("gal")){
					volume=DataConversion.getStatsInstance().getGlnToLtr(volume);
					
				}else if(volume_in.contentEquals("kg")){
					volume=DataConversion.getStatsInstance().getKiloToLtr(volume);
					
				}
		}
		if (error == null) {
			price = validateValues(volumePrice, "volume price");
		}
		if (error == null) {
			total_cost = validateValues(totalCost, "total cost");
		}
		if (error == null) {
			current_fuel_level = validateValues(currentfuel, "current fuel");
			Log.i("current_fuel_level before", ""+current_fuel_level);
			if(volume_in.contentEquals("gal")){
				current_fuel_level=DataConversion.getStatsInstance().getGlnToLtr(current_fuel_level);
				//Log.i("current_fuel_level gal", ""+current_fuel_level);
				
			}else if(volume_in.contentEquals("kg")){
				current_fuel_level=DataConversion.getStatsInstance().getKiloToLtr(current_fuel_level);
				//Log.i("current_fuel_level kg", ""+current_fuel_level);
				
			}
		}

		if (error == null) {

			date_of_refills = dateFormat.format(new Date(selectedDate
					.getTimeInMillis()));
			mileage = getCurrentMileage(date_of_refills.trim(), odometer_reading,
					current_fuel_level);
			Log.i("mileage::",""+mileage);
			
      
			if (total_vol_check) {
				noteValue = note.getText().toString();
				//volume_type=distance_in;
			if (edit){//on basis of initial date
				setPrevMileage(initialDate);
			}
				float next_mileage =setMileageNextRecord(date_of_refills,odometer_reading,volume,current_fuel_level);
				if(next_mileage>=0){
					
				FuelTrackerAsync ftAsync = getAsynClassObject();
				if (edit){
					                        
					ftAsync.execute(
							FuelTrackerQueryCommand.UPDATE_FUEL_REFILS_TABLE,
							FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,
							getApplicationContext(), fillupId, date_of_refills,
							odometer_reading, volume, volume_type, price,
							currencyType, total_cost, current_fuel_level,
							carId, mileage, noteValue);
				}
				else
					ftAsync
							.execute(
									FuelTrackerQueryCommand.INSERT_IN_FUEL_REFILS_TABLE,
									FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,
									getApplicationContext(), date_of_refills,
									odometer_reading, volume, volume_type,
									price, currencyType, total_cost,
									current_fuel_level, carId, mileage,
									noteValue);
				}else{
					showAlert(error);
				}
			}
		} else {
			showAlert(error);
		}
	}

	/**
	 * @author			: 	ajay.sahani
	 * @date			:	30th aug 2011
	 * @purpose			:	
	 * 						When the user changes date of a fillup the mileage will change as it is based on the immediate above this row. This function will return cursor to the immediate above row.
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	
	private float setMileageNextRecord(String date_of_refills,
			float odometerReading,float volume, float currentFuelLevel) {
		// TODO Auto-generated method stub
		error=null;
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillNextRow(
				date_of_refills, carId,fillupId);
		float next_odo_rading=0.0f,next_cur_fule_level=0.0f,next_mileage=0.0f;
		int next_id=-1,no=0;//no--no of row updated
		if (rowCursor != null && rowCursor.getCount() > 0) {

			rowCursor.moveToFirst();
				
				next_id = rowCursor.getInt(rowCursor
						.getColumnIndex("id"));
				next_odo_rading = rowCursor.getFloat(rowCursor
						.getColumnIndex("odometer_reading"));
				next_cur_fule_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));							

				if(odometerReading>0){
				next_mileage=((next_odo_rading -odometerReading)/(volume+currentFuelLevel-next_cur_fule_level));
				if(next_mileage<=0){
					error="Please check  date and odometer reading";
				}else{
				no=fuelRefillsAdapter.setmileageNextRow(next_id, next_mileage);
				}
				}else{
					no=fuelRefillsAdapter.setmileageNextRow(next_id,0.00f);
				}
		}//for 1st record
		else{
			flag=true;
			updateFirstRecord(fuelRefillsAdapter,date_of_refills);
			
		}
		//no=fuelRefillsAdapter.setmileageNextRow(next_id, next_mileage);
		/*Log.i("carid--------", ""+carId);
		Log.i("odometerReading--------", ""+odometerReading);
		Log.i("next_odo_rading--------", ""+next_odo_rading);
		Log.i("0d0 reading--------", ""+(next_odo_rading -odometerReading));
		Log.i("prev volume--------", ""+(volume));	
		Log.i("prev currentFuelLevel--------", ""+(currentFuelLevel));	
		Log.i("next_cur_fule_level--------", ""+(next_cur_fule_level));	
		Log.i("0d0 volume--------", ""+(volume+currentFuelLevel-next_cur_fule_level));	
		Log.i("next_mileage--------", ""+next_mileage);	
		Log.i("no of row updated--------", ""+no);		
		Log.i("id of  updated row--------", ""+next_id);		
		Log.i("date_of_refills--------", ""+date_of_refills);*/
		
		return next_mileage;
	}

	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	16th sep 2011
	 * @purpose			:	
	 * 						//this case arise when we have three entry in fillup and we are trying to shift 2nd position item
							//to 1st position than mileage of ,item that have been shifted in 2nd position should change..........
							//here we are calling updateFirstRecord function two time bcse we have to get all information of item that 
							//is on 3rd position now
	
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	private void updateFirstRecord(FuelRefillsAdapter fuelRefillsAdapter,String date_of_refills) {
		// TODO Auto-generated method stub
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillUsingDateEdit(
				date_of_refills, carId,fillupId);
		
		float prev_date_odometer_reading = 0,  prev_volume = 0.0f, pre_fule_level = 0.0f;

		if (rowCursor != null && rowCursor.getCount() > 0) {
			Log.i("initial_date_odometer_reading::","setPrevMileage");
			rowCursor.moveToFirst();

			if (rowCursor.getString(rowCursor.getColumnIndex("date_of_refill")) != null) {
				
				prev_date_odometer_reading = rowCursor.getFloat(rowCursor
						.getColumnIndex("odometer_reading"));
				prev_volume = rowCursor.getFloat(rowCursor
						.getColumnIndex("volume"));
				pre_fule_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));
				
				date_of_refills=rowCursor.getString(rowCursor.getColumnIndex("date_of_refill"));
			}
			if(flag){
				flag=false;
				updateFirstRecord(fuelRefillsAdapter,date_of_refills);
				setMileageNextRecord(date_of_refills,prev_date_odometer_reading, prev_volume,pre_fule_level);						
			}
		}
	}

	/**
	 * @author			: 	ajay.sahani
	 * @date			:	30th aug 2011
	 * @purpose			:	
	 * 						When the user changes date of a fillup the mileage will change as it is based on the immediate earlier row. This function will return cursor to the immediate earlier row.
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */
	public float getCurrentMileage(String date_of_refills,
			float last_date_odometer_reading, float current_fuel_level) {
		
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		Cursor rowCursor=null;
		/*
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillUsingDate(
				date_of_refills, carId,fillupId);*/
		
		if(edit){
			 rowCursor = fuelRefillsAdapter.getFuelRefillUsingDateEdit(
					date_of_refills, carId,fillupId);
			
		}else{
		 rowCursor = fuelRefillsAdapter.getFuelRefillUsingDate(
				date_of_refills, carId);
		}
		total_vol_check = true;

		float first_date_odometer_reading = 0, total_volume = 0, mileage = 0, prev_volume = 0.0f, pre_fule_level = 0.0f;
		Log.i("getMileage cursor length::",""+rowCursor.getCount());
		if (rowCursor != null && rowCursor.getCount() > 0) {

			rowCursor.moveToFirst();	
		
			if (rowCursor.getString(rowCursor.getColumnIndex("date_of_refill")) != null) {
				
				Log.i(TAG,"inside if (rowCursor.getString(rowCursor.getColumnIndex(date_of_refill)) != null)");

				Log.i("last_date2:",""+rowCursor.getString(rowCursor.getColumnIndex("date_of_refill")));
				first_date_odometer_reading = rowCursor.getFloat(rowCursor
						.getColumnIndex("odometer_reading"));
				prev_volume = rowCursor.getFloat(rowCursor
						.getColumnIndex("volume"));
				pre_fule_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));
				total_volume = (prev_volume + pre_fule_level)
						- current_fuel_level;
				currencyType = rowCursor.getString(
						rowCursor.getColumnIndex("currency_type"));
				
				/*Log.i("last_date_odometer_reading::",""+last_date_odometer_reading);
				Log.i("last_date_odometer_reading::",""+last_date_odometer_reading);
				Log.i("first_date_odometer_reading::",""+first_date_odometer_reading);
				Log.i("prev_volume::",""+prev_volume);
				Log.i("pre_fule_level::",""+pre_fule_level);
				Log.i("total_volume::",""+total_volume);
				Log.i("currencyType::",""+currencyType);*/
				
				if (total_volume < 0) {
					showAlert("Please enter correct current fuel level");
					total_vol_check = false;
				} else {
					if (total_volume>= 0) {
						
						if(total_volume == 0)
						total_volume = 1.0f;
						
						if (last_date_odometer_reading < first_date_odometer_reading) {
							showAlert("Please check  date and odometer reading");
																																							// reading
							total_vol_check = false;
						}else {
							mileage = Stats.getStatsInstance().getTotalMileage(
									last_date_odometer_reading,
									first_date_odometer_reading, total_volume);
							Log.i("mileage::",""+mileage);
							total_vol_check = true;
						}
					} 

				}
			}

		}else{
			mileage=0.0f;
		}
		/*if(mileage<0){
			mileage=mileage * (-1.0f);//if mileage comes -ve make it positive
		}*/
		return mileage;
	}

	public float validateValues(EditText editText, String msg) {
		float reading = 0.0f;
		
		if (editText.getText().toString() != null
				&& editText.getText().toString().trim().length() > 0) {
			try {
				reading = Float.parseFloat(editText.getText().toString());
			} catch (Exception e) {
				e.printStackTrace();
				error = "Please enter numerical value for " + msg;
			}
			if(error==null)
			{
				if (msg.contentEquals("fuel") ) {// user should not enter volume more than its tank capacity
		
					tankCapacityValidation(msg);
				}
			}
				
		} else {
			error = msg + " can not be blank";
		}
		return reading;
	}

	
	private void tankCapacityValidation(String msg) {
		
		// TODO Auto-generated method stub
		float current_fuel_level = 0.0f,volume_level=0.0f;
		try {
			if ( currentfuel.getText() != null && currentfuel.getText().toString().length()>0 ){
				current_fuel_level= Float.parseFloat(currentfuel.getText().toString());
				Log.i("current_fuel_level---------", ""+current_fuel_level);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error = "Please enter numerical value for " + msg;
		}
		try {
			volume_level= Float.parseFloat(fuel.getText().toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			error = "Please enter numerical value for " + msg;
		}
		
		if(current_fuel_level>tankCapacity)
			error = "current fuel can not be more than tank capacity";
		else if(volume_level>tankCapacity)
			error = "fuel can not be more than tank capacity";
		else if(current_fuel_level+volume_level>tankCapacity)
			error ="Please check Fuel quantity is more than tank capacity";
		/*if((reading+current_fuel_level)>tankCapacity){								
		error = "Please check Fuel quantity is more than tank capacity";
		}else{
			error=null;
		}*/
	}


	private void showAlert(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private FuelTrackerAsync getAsynClassObject() {

		FuelTrackerProgressBar ftpBar = new FuelTrackerProgressBar(
				getParent(), "Please wait while saving record");
		FuelTrackerAsync ftAsync = new FuelTrackerAsync(this, ftpBar);

		return ftAsync;
	}

	public void onBackPressed() {

		Uri data = null;
		Intent result = new Intent(null, data);
		setResult(RESULT_OK, result);
		super.onBackPressed();
	}

	public void onHTTPResponseComplete(Object data, String request) {
		//System.out.println(data);
		if (data != null
				&& request
						.contentEquals(FuelTrackerQueryCommand.INSERT_IN_FUEL_REFILS_TABLE)) {
			long no = (Long) data;
			//Log.i("INSERT_IN_FUEL_TABLE", "<-------record no---------->" + no);
			Uri d = null;
			Intent result = new Intent(null, d);
			setResult(RESULT_OK, result);
			finish();
		} else if (data != null
				&& request
						.contentEquals(FuelTrackerQueryCommand.UPDATE_FUEL_REFILS_TABLE)) {
			System.out.println("updated");
			Uri d = null;
			Intent result = new Intent(null, d);
			setResult(RESULT_OK, result);
			finish();
		} else if (data != null
				&& request
						.contentEquals(FuelTrackerQueryCommand.GET_CONFIGURATION_INFO)) {
			Cursor cursor = (Cursor) data;
			fillDefaultValuesInTextView(cursor);
		}

	}

	public void fillDefaultValuesInTextView(Cursor cursor) {
		
		String distanceType="";
		String volumeType="";
		//System.out.println("Inside fill default values in text view *******************");
		
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			
			if(consumption_in==null)
			consumption_in=cursor.getString(cursor
					.getColumnIndex("consumption_type"));
			/*
			if ( cursor.getString(cursor.getColumnIndex("distance_type")).toString().length()!=0 ){
				distanceType = cursor.getString(
						cursor.getColumnIndex("distance_type")).toString()
						.substring(
								cursor.getString(
										cursor.getColumnIndex("distance_type"))
										.toString().indexOf("(") + 1,
								cursor.getString(
										cursor.getColumnIndex("distance_type"))
										.toString().indexOf(")"));
			}
			else{
				distanceType = "km";
			}
			if ( cursor.getString(cursor.getColumnIndex("volume_type")).toString().length()!=0 ){
				volumeType = cursor.getString(
						cursor.getColumnIndex("volume_type")).toString().substring(
						cursor.getString(cursor.getColumnIndex("volume_type"))
								.toString().indexOf("(") + 1,
						cursor.getString(cursor.getColumnIndex("volume_type"))
								.toString().indexOf(")"));
			}
			else{
				volumeType = "l";
			}*/
			if ( cursor.getString(cursor.getColumnIndex("currency")).toString().length()!=0  ){
			currencyType = cursor.getString(
					cursor.getColumnIndex("currency")).toString().substring(
					0,
					cursor.getString(cursor.getColumnIndex("currency"))
							.toString().indexOf("-"));
			Log.i("currencyType:",currencyType);
			}
			else{
				currencyType = "USD";
			}
			//disttype.setText(distanceType);
			//voltype.setText(volumeType);
			currencyvoltype.setText(currencyType + "/" + volume_in);
			currencytype.setText(currencyType);
			//currtvoltype.setText(volumeType);
		}
		/*if(distance_in==null)
			distance_in=distanceType;
		if(volume_in==null)
			volume_in=volumeType ;*/
	}

	public void onHTTPResponseProgress() {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			saveFillup();
			break;
		case R.id.cancel:
			finish();
			break;
		case R.id.dateRefill:
			showDatePickerDailog();
			break;
		case R.id.now:
			setTodaysDate();
			break;
		}
	}

	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

		getTotalCost(fuel.getText().toString(), volumePrice.getText()
				.toString());
		return false;
	}

	public void getTotalCost(String volumeText, String priceText) {
		totalCost.setText("");
		if (volumeText != null && volumeText.trim().length() > 0) {
			if (priceText != null && priceText.trim().length() > 0) {
				float volumeVal = Float.parseFloat(volumeText.trim());
				float priceVal = Float.parseFloat(priceText.trim());
				if (priceVal > 0 && volumeVal > 0) {
					float tCost = priceVal * volumeVal;
					DecimalFormat dc = new DecimalFormat("0.00");
					totalCost.setText(dc.format(tCost));
				}
			}
		}
	}

}
