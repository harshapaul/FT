package com.fueltracker.UI;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.fueltracker.FT_DB.ConfigurationAdapter;
import com.fueltracker.FT_DB.FuelRefillsAdapter;
import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;
import com.fueltracker.model.FuelRefills;
import com.fueltracker.model.VehicleItem;
import com.fueltracker.utils.DataConversion;
import com.fueltracker.utils.Stats;
/**
 * @UiAuthor		: 	ruchi.malvankar
 * @date			:	8/10/2011
 * @purpose			:	To list fill-ups respective to a vehicle
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class Fillups extends ActivityGroup implements OnItemClickListener,HTTPResponseListener,OnClickListener,OnItemSelectedListener {
	private ListAdapter listAdapter;
	private ImageButton addFillup;
	private Spinner car;
	private int selectedCarId=-1,carId,fillupId,listPos;
	private float tankCapacity,distance_;
	private String distance_in,volume_in,consumption_in;
	private DecimalFormat dc;
	static final int DELETE_ALL=1;
	private float prev_date_odometer_reading = 0, prev_volume = 0.0f,prev_fuel_level=0.0f;
	public static int fillupCarId=-1;
	private ListView listView;
	private static final int SHOW_ADDFILLUP=1;
	private static final String TAG = "Fillups";
	public static boolean frmFillups=false;
	@Override
    public boolean onSearchRequested() {
		return getParent().onSearchRequested();
    }
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.fillups, null);
        setContentView(viewToLoad); 
        //setContentView(R.layout.fillups);
    	loadUI();
	}
	public void loadUI()
	{
		fillupCarId=-1;
		car = (Spinner) findViewById(R.id.car);
		addFillup=(ImageButton)findViewById(R.id.add);
		addFillup.setOnClickListener(this);
		car.setOnItemSelectedListener(this);
		car.setVisibility(View.GONE);
		addFillup.setVisibility(View.GONE);
		listView = (ListView)findViewById(android.R.id.list);    
		listView.setOnItemClickListener(this);
		dc = new DecimalFormat("0.00");	
		registerForContextMenu(listView);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(FuelTracker.tabHost.getWindowToken(), 0);
		loadAllCars();
		  
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		Date dateOfR=listAdapter.getItem(info.position).getDateOfRefill();
		DateFormat df = new SimpleDateFormat("M/dd/yyyy");
		menu.setHeaderTitle(car.getSelectedItem()+" "+df.format(dateOfR));
		String[] menuItems = getResources().getStringArray(R.array.fiilupmenu);
		for (int i = 0; i < menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
		fillupId=listAdapter.getItem(info.position).getId();
		listPos=info.position;
		/*if (info.position != 0) {
			header = listAdapter.getItem(info.position).getName();
			menu.setHeaderTitle(header);
			contextId = info.position;
			phoneIds = listAdapter.getItem(info.position).getToid();*/
			
		//}

	}
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		System.out.println(item.getTitle());
		
		if(item.getTitle().toString().compareTo("Edit")==0)
		{
			Bundle bundle = new Bundle();
			
			bundle.putInt("fillupId",listAdapter.getItem(listPos).getId());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			bundle.putString("dateOfRefill",dateFormat.format(listAdapter.getItem(listPos).getDateOfRefill()));
			bundle.putString("odometer",dc.format(listAdapter.getItem(listPos).getOdometerReading()));
			bundle.putString("volume",dc.format(listAdapter.getItem(listPos).getVolume()));
			bundle.putString("price",dc.format(listAdapter.getItem(listPos).getPrice()));
			bundle.putString("totalCost",dc.format(listAdapter.getItem(listPos).getTotalCost()));
			bundle.putString("fuelLevel",dc.format(listAdapter.getItem(listPos).getCurrentFuelLevel()));
			bundle.putString("note",String.valueOf(listAdapter.getItem(listPos).getNote()));
			bundle.putString("currency_type",String.valueOf(listAdapter.getItem(listPos).getCurrencyType()));
			
			
			bundle.putFloat("tankCapacity",tankCapacity);
			bundle.putString("distance_in",distance_in);
			bundle.putString("volume_in",volume_in);
			bundle.putString("consumption_in",consumption_in);
			bundle.putInt("carId", carId);
			
			bundle.putBoolean("edit", true);
			/*Intent myIntent;
			myIntent = new Intent(Fillups.this, AddFillup.class);
			myIntent.putExtras(bundle);
			
			startActivityForResult(myIntent, SHOW_ADDFILLUP); */ 
			Intent myIntent = new Intent(getParent(), AddFillup.class);
			myIntent.putExtras(bundle);
	        TabGroupActivity parentActivity = (TabGroupActivity)getParent();
	        parentActivity.startChildActivity("AddFillup", myIntent);
		}
		else if(item.getTitle().toString().compareTo("Delete")==0)
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			String date=dateFormat.format(listAdapter.getItem(listPos).getDateOfRefill()).trim();
			fillupId=listAdapter.getItem(listPos).getId();
			updateMileageNextRecord(fillupId,date);
			//deleteFillup(fillupId);
		}
		else if(item.getTitle().toString().compareTo("Delete All")==0)
		{
			showDialog(DELETE_ALL);
		}
		return true;
	}
	
	private void updateMileageNextRecord(int fillupId,
			String dateOfRefill) {
		// TODO Auto-generated method stub
		float next_mileage=0.0f;
		getPreviousRcordInfo(fillupId,dateOfRefill);
		next_mileage=setMileageNextRecord(fillupId,dateOfRefill);
		int updatePos=listPos-1;
		//update element at next postion
		if(updatePos>=0){
		FuelRefills fr=listAdapter.getItem(updatePos);
		fr.setMileage(next_mileage);
		}
		
		deleteFillup(fillupId);
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DELETE_ALL:
			return new AlertDialog.Builder(getParent()).setTitle("Delete")
					.setMessage("Do you want to delete all the fillups?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									deleteAllFillups();
								
								}
							}).setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();
		
		}
		return null;
	}

	public void deleteAllFillups()
	{
		FuelTrackerAsync ftAsync=getAsynClassObject();	
		ftAsync.execute(FuelTrackerQueryCommand.DEL_FULE_REFIL_USING_CAR_ID,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),carId);    
	}
	public void deleteFillup(int fillupId)
	{
		FuelTrackerAsync ftAsync=getAsynClassObject();	
		ftAsync.execute(FuelTrackerQueryCommand.DELETE_FUEL_REFILS_ACCOUNT,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),fillupId);    
	}
	public void loadAllCars()
	{
		FuelTrackerAsync ftAsync=getAsynClassObject();				
		ftAsync.execute(FuelTrackerQueryCommand.GET_ALL_CARS_INFO,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext());    
	}
	public void getConfiguration()
	{
		ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
		Cursor rowCursor=configurationAdapter.getAllConfigurationInfo();
		if(rowCursor!=null && rowCursor.getCount()>0)
		{
			rowCursor.moveToFirst();
			//System.out.println("currency..."+rowCursor.getString(rowCursor.getColumnIndex("currency")));
			selectedCarId=rowCursor.getInt(rowCursor.getColumnIndex("sel_car_id"));
			Log.i(TAG, "Selected Car ID in Fillups: "+selectedCarId);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == SHOW_ADDFILLUP)
		{			
			
			//listAdapter.items.clear();
			getFillups();
			//listAdapter.notifyDataSetChanged();
		
		}
	}
	public void getFillups()
	{
		FuelTrackerAsync ftAsync=getAsynClassObject();		
		ftAsync.execute(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),carId);
	}
	private FuelTrackerAsync getAsynClassObject(){
		
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(getParent(),"Please wait while retrieving records");
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		
		return ftAsync;
	}
	
	private class ListAdapter extends ArrayAdapter<FuelRefills> {

		private ArrayList<FuelRefills> items;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<FuelRefills> items) {
			super(context, textViewResourceId, items);
			this.items = items;

		}

		public View getView(int position, View convertView, ViewGroup parent) {

			
			FuelRefills o = items.get(position);
			
			if (o != null) {
				
				
					LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					convertView = mInflater.inflate(R.layout.fillupsrow, null);
									
					DateFormat df = new SimpleDateFormat("M/dd/yyyy");
					
					LinearLayout ll= (LinearLayout) convertView.findViewById(R.id.ll);
					if(position!=0 && position%2!=0)
						ll.setBackgroundResource(R.drawable.selector_odd);
					
					TextView date= (TextView) convertView.findViewById(R.id.date);		
					String formattedDate=df.format(o.getDateOfRefill());
					date.setText(formattedDate);

					String fuelLevel=dc.format(o.getVolume());
					TextView fuels= (TextView) convertView.findViewById(R.id.fuel);		
					fuels.setText(fuelLevel+" "+volume_in);
					
					String odometerValue=dc.format(o.getOdometerReading());
					TextView odometer= (TextView) convertView.findViewById(R.id.odometer);		
					odometer.setText(odometerValue+" "+distance_in);
					
					String totalCost=dc.format(o.getTotalCost());
					TextView total= (TextView) convertView.findViewById(R.id.totalcost);		
					total.setText(totalCost+" "+o.getCurrencyType());
					
					String volumePrice=dc.format(o.getPrice());
					TextView volumeprice= (TextView) convertView.findViewById(R.id.volumeprice);		
					volumeprice.setText(volumePrice+" "+o.getCurrencyType());
				
					if(o.getDays()>0)
					{
						TextView days= (TextView) convertView.findViewById(R.id.days);	
						String day="(+"+o.getDays()+"days)";
						days.setText(day);
					}
					if(o.getOdometerReading2()>0)
					{
						TextView meter2= (TextView) convertView.findViewById(R.id.meter2);	
						String odometer2="+"+dc.format(o.getOdometerReading2());
						meter2.setText(odometer2+" "+distance_in);
					}
					if(o.getMileage()>0)
					{
						TextView mileage= (TextView) convertView.findViewById(R.id.mileage);	
						String mileageV=dc.format(o.getMileage());
						mileage.setText(mileageV+" "+consumption_in);
					}
					if(o.getNote()!=null && o.getNote().trim().length()>0)
					{
						TextView note= (TextView) convertView.findViewById(R.id.note);	
						TableRow rowDash=(TableRow)convertView.findViewById(R.id.rowdash);	
						TableRow rowNote=(TableRow)convertView.findViewById(R.id.rownote);
						rowDash.setVisibility(View.VISIBLE);
						rowNote.setVisibility(View.VISIBLE);
						note.setText(o.getNote().trim());
					}
				
			}

			return convertView;

		}

	}
	
	public void onHTTPResponseComplete(Object data, String request) {
		// TODO Auto-generated method stub
		if ( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_ALL_CARS_INFO))
		{
			Cursor cursor = (Cursor)data;
			showVehiclesList(cursor);
			
			
		}
		else if( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO))
		{
		
			
			//Cursor cursor=(Cursor)data;
			
			showFillupsList((Cursor)data);
		}
		else if( data != null && request.contentEquals(FuelTrackerQueryCommand.DELETE_FUEL_REFILS_ACCOUNT))
		{
			//System.out.println("deletion");
			Boolean deleteStatus = (Boolean)data;
			if(deleteStatus!=null && deleteStatus)
				removeFillup();
		}
		else if( data != null && request.contentEquals(FuelTrackerQueryCommand.DEL_FULE_REFIL_USING_CAR_ID))
		{
			Boolean deleteStatus = (Boolean)data;
			if(deleteStatus!=null && deleteStatus)
				cleraList();
		}
		
	}
	public void cleraList()
	{
		listAdapter.clear();
		listAdapter.notifyDataSetChanged();
	}
	public void removeFillup()
	{
		listAdapter.items.remove(listPos);
		listAdapter.notifyDataSetChanged();
		
	}
	private static int dateDiffInDays(Date toDate, Date fromDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		long ms = cal.getTimeInMillis();
		cal.setTime(fromDate);
		ms -= cal.getTimeInMillis();
		final long msPerHour = 1000L * 60L * 60L * 24;
		int hours = (int) (ms / msPerHour);
		return hours;
		
	} 
	
	public void showFillupsList(Cursor cursor)
	{
		ArrayList<FuelRefills> fuelRefills=new ArrayList<FuelRefills>();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		
		while (cursor!=null && cursor.moveToNext()) 
		{
		
			FuelRefills fR1=new FuelRefills();
			String currency_type="";
			String date_time=cursor.getString(cursor.getColumnIndex("date_of_refill"));
			float price=cursor.getFloat(cursor.getColumnIndex("price"));
			int days=0;
			float totalcost=cursor.getFloat(cursor.getColumnIndex("total_cost"));
			 currency_type=cursor.getString(cursor.getColumnIndex("currency_type"));
			
			float fuelLevel=cursor.getFloat(cursor.getColumnIndex("current_fuel_level"));
			//perform conversion for fuelLevel as we have stored volume in form of liter
			if(volume_in.contentEquals("gal")){
				fuelLevel=DataConversion.getStatsInstance().getLtrToGln(fuelLevel);
				//Log.i(" fillup current_fuel_level gal", ""+fuelLevel);
			}else if(volume_in.contentEquals("kg")){
				fuelLevel=DataConversion.getStatsInstance().getLtrToKilo(fuelLevel);
				//Log.i(" fillup current_fuel_level kg", ""+fuelLevel);
			}
			
			float volume=cursor.getFloat(cursor.getColumnIndex("volume"));
			//perform conversion for volume as we have stored volume in form of liter
			if(volume_in.contentEquals("gal")){
				volume=DataConversion.getStatsInstance().getLtrToGln(volume);
				
			}else if(volume_in.contentEquals("kg")){
				volume=DataConversion.getStatsInstance().getLtrToKilo(volume);
				
			}
			
			int id=cursor.getInt(cursor.getColumnIndex("id"));
			float odometer=cursor.getFloat(cursor.getColumnIndex("odometer_reading"));
			//Log.i(" fillup odometer km", ""+odometer);
			//perform conversion for odometer reading as we have stored distance in form of Km
			if(distance_in.contentEquals("mi"))	{
				odometer=DataConversion.getStatsInstance().getMilesFromkm(odometer);
				//Log.i(" fillup odometer mi", ""+odometer);
			}

			
			float mileage=cursor.getFloat(cursor.getColumnIndex("mileage"));
			//perform conversion for mileage as we have stored distance in form of Km/l
			if(!consumption_in.contentEquals("km/l"))	{
				mileage=DataConversion.getStatsInstance().convertConsumptionType(mileage,consumption_in);
			}

			
			String note=cursor.getString(cursor.getColumnIndex("note"));
			
			FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
			//Cursor rowCursor=fuelRefillsAdapter.getFuelRefillUsingDate(date_time,carId,id);
			Cursor rowCursor=fuelRefillsAdapter.getFuelRefillUsingDate(date_time,carId);
			Date current=new Date();
			try {
				current=dateFormat.parse(date_time);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			float odometerO=0;
		
			if(rowCursor!=null && rowCursor.getCount()>0)
			{
				
				rowCursor.moveToFirst();
			
				Date secondLast=new Date();
				try {
					if(rowCursor.getString(rowCursor.getColumnIndex("date_of_refill"))!=null)
						secondLast=dateFormat.parse(rowCursor.getString(rowCursor.getColumnIndex("date_of_refill")));
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				days =dateDiffInDays(current,secondLast);
			
				if(rowCursor.getString(rowCursor.getColumnIndex("date_of_refill"))!=null){
					odometerO=odometer-rowCursor.getFloat(rowCursor.getColumnIndex("odometer_reading"));
				if(distance_in.contentEquals("mi"))	{
					odometerO=odometer-DataConversion.getStatsInstance().getMilesFromkm(rowCursor.getFloat(rowCursor.getColumnIndex("odometer_reading")));
				}
				}
			}
			fR1.setDays(days);
			fR1.setId(id);
			fR1.setDateOfRefill(current);			       
	        fR1.setOdometerReading(odometer);
	        fR1.setVolume(volume);
	        fR1.setPrice(price);		        
	        fR1.setTotalCost(totalcost);		       
	        fR1.setCurrentFuelLevel(fuelLevel);
	        fR1.setOdometerReading2(odometerO);
	        fR1.setMileage(DataConversion.getStatsInstance().roundTwoDecimals(mileage));
	        fR1.setNote(note);
	        fR1.setCurrencyType(currency_type);
	        fuelRefills.add(fR1);
		}
		listAdapter=new ListAdapter(getApplicationContext(), R.layout.fillupsrow, fuelRefills);
		listView.setAdapter(listAdapter);
		if(selectedCarId!=-1)
		{
			//update
			ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
			configurationAdapter.updateConfigurationTable(1, null, null, null, null, carId);
		}
		else
		{
			//insert
			
			ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
			String[] arrayConsume=getResources().getStringArray(R.array.array_consum);
			String[] arrayCurrency=getResources().getStringArray(R.array.array_currency);
			String[] arrayDistance=getResources().getStringArray(R.array.array_dist);
			String[] arrayVolume=getResources().getStringArray(R.array.array_fuel);
			configurationAdapter.insertInConfigurationTable(arrayCurrency[0], arrayVolume[0], arrayDistance[0], arrayConsume[0], carId);
			selectedCarId=carId;
		}
	}
	
	public void showVehiclesList(Cursor cursor)
	{
		ArrayList<VehicleItem> vehicleListAdapter = new ArrayList<VehicleItem>();
		if(cursor!=null && cursor.getCount()>0)
		{
			while (cursor!=null && cursor.moveToNext()) 
			{
				
				VehicleItem vi1 = new VehicleItem();	
				vi1.setId(cursor.getInt(cursor.getColumnIndex("id")));
				vi1.setMake(cursor.getString(cursor.getColumnIndex("make")));
				vi1.setModel(cursor.getString(cursor.getColumnIndex("model")));
				vi1.setDistance(cursor.getString(cursor.getColumnIndex("distance_in")));
				vi1.setVolume(cursor.getString(cursor.getColumnIndex("volume_in")));
				vi1.setConsumption(cursor.getString(cursor.getColumnIndex("consumption_in")));
				vi1.setTank_capacity(cursor.getFloat(cursor.getColumnIndex("tank_capacity")));
				vi1.setDistCovered("240km");
				vi1.setFillUps("4 Fill-ups");
				vi1.setConsumValue("6.8km/l");
				vehicleListAdapter.add(vi1);			
			}
			car.setVisibility(View.VISIBLE);
			addFillup.setVisibility(View.VISIBLE);
			spinnerArrayAdapter = new ArrayAdapter<VehicleItem>(this,R.layout.spinner_item_vehicle,
					vehicleListAdapter);
			//when it create list it will call to string() method of this object
			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			car.setAdapter(spinnerArrayAdapter);
			getConfiguration();
			Log.i(TAG, "Selected car id before pos: "+selectedCarId);
			if(selectedCarId!=-1)
			{
				int pos=-1;
				Log.i(TAG, "vehicleListAdapter.size(): "+vehicleListAdapter.size());
				if(vehicleListAdapter.size()>0)
				{
					for(int i=0;i<vehicleListAdapter.size();i++)
					{
						if(vehicleListAdapter.get(i).getId().compareTo(selectedCarId)==0)
						{
							Log.i(TAG, "Pos in fillups: "+pos);
							pos=i;
							break;
						}
					}
				}
				if(pos!=-1)
					car.setSelection(pos);
				
				Log.i(TAG, "Pos: "+pos);
			}
		}
		
		
	}
	ArrayAdapter<VehicleItem> spinnerArrayAdapter;
	
	
	public void onHTTPResponseProgress() {
		
	}
	
	public void onClick(View v) 
	{
		switch(v.getId())
		{  
	    	case R.id.add: 
	    		
				Bundle bundle=new Bundle();
				bundle.putInt("carId", carId);
				bundle.putFloat("tankCapacity",tankCapacity);
				bundle.putString("distance_in",distance_in);
				bundle.putString("volume_in",volume_in);
				bundle.putString("consumption_in",consumption_in);
				/*Intent intent = new Intent(Fillups.this,AddFillup.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, SHOW_ADDFILLUP);  */
				
				Intent myIntent = new Intent(getParent(), AddFillup.class);
				myIntent.putExtras(bundle);
		        TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		        parentActivity.startChildActivity("AddFillup", myIntent);
	    		break;  
	    	
		}  
	}
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Bundle bundle = new Bundle();
		
		bundle.putInt("fillupId",listAdapter.getItem(position).getId());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		bundle.putString("dateOfRefill",dateFormat.format(listAdapter.getItem(position).getDateOfRefill()));
		bundle.putString("odometer",dc.format(listAdapter.getItem(position).getOdometerReading()));
		bundle.putString("volume",dc.format(listAdapter.getItem(position).getVolume()));
		bundle.putString("price",dc.format(listAdapter.getItem(position).getPrice()));
		bundle.putString("totalCost",dc.format(listAdapter.getItem(position).getTotalCost()));
		bundle.putString("fuelLevel",dc.format(listAdapter.getItem(position).getCurrentFuelLevel()));
		bundle.putString("note",String.valueOf(listAdapter.getItem(position).getNote()));
		bundle.putString("currency_type",String.valueOf(listAdapter.getItem(position).getCurrencyType()));
		
		bundle.putFloat("tankCapacity",tankCapacity);
		bundle.putInt("carId", carId);
		bundle.putBoolean("edit", true);
		
		
		bundle.putString("distance_in", distance_in);
		bundle.putString("volume_in", volume_in);
		bundle.putString("consumption_in", consumption_in);
		
		/*Intent myIntent;
		myIntent = new Intent(Fillups.this, AddFillup.class);
		myIntent.putExtras(bundle);
		
		startActivityForResult(myIntent, SHOW_ADDFILLUP);*/ 
		Intent myIntent = new Intent(getParent(), AddFillup.class);
		myIntent.putExtras(bundle);
        TabGroupActivity parentActivity = (TabGroupActivity)getParent();
        parentActivity.startChildActivity("AddFillup", myIntent);
		

	}
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		VehicleItem item=(VehicleItem)car.getItemAtPosition(arg2);
		carId=item.getId();
		tankCapacity=item.getTank_capacity();
		distance_in=item.getDistance();
		volume_in=item.getVolume();
		consumption_in=item.getConsumption();
		
		fillupCarId=arg2;
		frmFillups=true;
		Log.i(TAG, "frmFillups: "+frmFillups);
		Log.i("carId:",""+carId);
		Log.i("tankCapacity:",""+tankCapacity);
		Log.i("distance_in:",distance_in);
		Log.i("volume_in:",volume_in);
		Log.i("consumption_in:",consumption_in);
		//Log.i(item.getMake());
		getFillups();
		
	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}
	
	
	private float setMileageNextRecord(int fillUpId,String date_of_refills) {
			
		// TODO Auto-generated method stub
		//date_of_refills="2011-09-13 17:45:24";
		date_of_refills=date_of_refills.trim();
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		
		
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillNextRow(
				date_of_refills,carId,fillUpId);
		float next_odo_rading=0.0f,next_cur_fule_level=0.0f,next_mileage=0.0f;
		int next_id=-1,no=0;//no--no of row updated
		String next_date="";
		Log.i("cursor size--------", ""+rowCursor.getCount());
		if (rowCursor != null && rowCursor.getCount() > 0) {

			rowCursor.moveToFirst();
				
				next_id = rowCursor.getInt(rowCursor
					.getColumnIndex("id"));
				next_odo_rading = rowCursor.getFloat(rowCursor
						.getColumnIndex("odometer_reading"));
				next_cur_fule_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));							

				next_date = rowCursor.getString(rowCursor
						.getColumnIndex("date_of_refill"));
				if(prev_date_odometer_reading==0){
					next_mileage=0.0f;
				}else{
				next_mileage=((next_odo_rading -prev_date_odometer_reading)/(prev_volume+prev_fuel_level-next_cur_fule_level));
				if(next_mileage<=0){
					Log.i("next_mileage---","less than zero");	
				}else{
				no=fuelRefillsAdapter.setmileageNextRow(next_id, next_mileage);
				}
				}
				
		}
		Log.i("previous date--------", ""+date_of_refills);
		Log.i("next_date--------", ""+next_date);
		Log.i("next_odo_rading--------", ""+next_odo_rading);
		Log.i("next_mileage--------", ""+next_mileage);	
		Log.i("no of row updated--------", ""+no);		
		Log.i("id of  updated row--------", ""+next_id);
		Log.i("carid--------", ""+carId);
		
		return next_mileage;
	}
	
	
	public void getPreviousRcordInfo(int fillUpId,String date_of_refills) {
			
		FuelRefillsAdapter fuelRefillsAdapter = new FuelRefillsAdapter(
				getApplicationContext());
		/*Cursor rowCursor = fuelRefillsAdapter.getFuelRefillUsingDate(
				date_of_refills, carId,fillUpId);*/
		Cursor rowCursor = fuelRefillsAdapter.getFuelRefillUsingDate(
				date_of_refills, carId);

		 prev_date_odometer_reading = 0; prev_volume = 0.0f;prev_fuel_level=0.0f;
		if (rowCursor != null && rowCursor.getCount() > 0) {

			rowCursor.moveToFirst();

			if (rowCursor.getString(rowCursor.getColumnIndex("date_of_refill")) != null) {
				
				prev_date_odometer_reading = rowCursor.getFloat(rowCursor
						.getColumnIndex("odometer_reading"));
				prev_volume = rowCursor.getFloat(rowCursor
						.getColumnIndex("volume"));
				prev_fuel_level = rowCursor.getFloat(rowCursor
						.getColumnIndex("current_fuel_level"));
				
				Log.i("prev_date_odometer_reading::",""+prev_date_odometer_reading);
				Log.i("prev_volume::",""+prev_volume);
				Log.i("prev_fuel_level::",""+prev_fuel_level);
				Log.i("date_of_refills::",""+date_of_refills);
								
			}
		}
		
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle bundle = new Bundle();
		int position=(int)arg3;
		bundle.putInt("fillupId",listAdapter.getItem(position).getId());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		bundle.putString("dateOfRefill",dateFormat.format(listAdapter.getItem(position).getDateOfRefill()));
		bundle.putString("odometer",dc.format(listAdapter.getItem(position).getOdometerReading()));
		bundle.putString("volume",dc.format(listAdapter.getItem(position).getVolume()));
		bundle.putString("price",dc.format(listAdapter.getItem(position).getPrice()));
		bundle.putString("totalCost",dc.format(listAdapter.getItem(position).getTotalCost()));
		bundle.putString("fuelLevel",dc.format(listAdapter.getItem(position).getCurrentFuelLevel()));
		bundle.putString("note",String.valueOf(listAdapter.getItem(position).getNote()));
		bundle.putString("currency_type",String.valueOf(listAdapter.getItem(position).getCurrencyType()));
		
		bundle.putFloat("tankCapacity",tankCapacity);
		bundle.putInt("carId", carId);
		bundle.putBoolean("edit", true);
		
		
		bundle.putString("distance_in", distance_in);
		bundle.putString("volume_in", volume_in);
		bundle.putString("consumption_in", consumption_in);
		
		/*Intent myIntent;
		myIntent = new Intent(Fillups.this, AddFillup.class);
		myIntent.putExtras(bundle);
		
		startActivityForResult(myIntent, SHOW_ADDFILLUP);*/
		Intent myIntent = new Intent(getParent(), AddFillup.class);
		myIntent.putExtras(bundle);
        TabGroupActivity parentActivity = (TabGroupActivity)getParent();
        parentActivity.startChildActivity("AddFillup", myIntent);
		
	}
}
