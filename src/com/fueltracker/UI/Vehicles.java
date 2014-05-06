package com.fueltracker.UI;

import java.text.NumberFormat;
import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.fueltracker.FT_DB.ConfigurationAdapter;
import com.fueltracker.FT_DB.FuelRefillsAdapter;
import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;
import com.fueltracker.model.VehicleItem;
import com.fueltracker.utils.DataConversion;
import com.fueltracker.utils.Stats;
/**
 * @UiAuthor		: 	jitendra.chaure
 * @date			:	8/10/2011
 * @purpose			:	to display list of vehicles
 * @ModifiedBy		:	
 * @ModificationDate:	
 * @Modification	:	
 * */
public class Vehicles extends ActivityGroup implements OnClickListener, OnItemClickListener, OnItemLongClickListener, HTTPResponseListener {
	
	private static final int DIALOG_LIST = 1;
	public static Integer RECORD_ID_FORANALYSIS = 0;
	public static Integer RECORD_POSITION_FORANALYSIS = 0;
	public static int frmAnalaysis=0;
	final String TAG = getClass().getName();
	private static final int SHOW_EDITVEH=1,DELETE_ALL=2,SHOW_ANALYSIS=3;
	private ListView vehiclesListView;
	private ArrayList<VehicleItem> vehiclesList = new ArrayList<VehicleItem>();
	private ListAdapter vehicleListAdapter;
	private Button addVehicle;
	private boolean itemLongClickFlag = false;
	private Integer idForDialog = 0;
	public String makeForDialog,modelForDialog;
	private VehicleItem tempVehicleItem = null;
	@Override
    public boolean onSearchRequested() {
		return getParent().onSearchRequested();
    }
	@Override
    protected Dialog onCreateDialog(int id) {
		
		Log.i(TAG, "Dialog make " + makeForDialog);
        switch (id) {
        case DIALOG_LIST:
        	Log.i(TAG, "Dialog make " + makeForDialog);
        	return new AlertDialog.Builder(getParent())
            .setTitle(makeForDialog+" "+modelForDialog)
            .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    
                    String[] items = getResources().getStringArray(R.array.select_dialog_items);
                    if ( items[which].equals("Edit") ){
                    	/*Intent edit = new Intent(Vehicles.this,EditVehicle.class);
    					edit.putExtra("RecordId", idForDialog);
    		    		startActivityForResult(edit, SHOW_EDITVEH);*/
    		    		 Intent previewMessage = new Intent(getParent(), EditVehicle.class);
    		    		 previewMessage.putExtra("RecordId", idForDialog);
    		             TabGroupActivity parentActivity = (TabGroupActivity)getParent();
    		             parentActivity.startChildActivity("EditVehicle", previewMessage);
                    }
                    else if ( items[which].equals("Delete") ){
                    	FuelTrackerAsync ftAsync=getAsynClassObject("Deleting record.");
                    	ftAsync.execute(FuelTrackerQueryCommand.DELETE_CARS_ACCOUNT,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,
                    						getApplicationContext(),idForDialog);
                    	
                    }
                    else if ( items[which].equals("Delete All") ){
                    	showDialog(DELETE_ALL);
                    }
                    else if ( items[which].equals("Analysis")){
                    	//Intent analysisIntent = new Intent(Vehicles.this,Analysis.class);
                    	//analysisIntent.putExtra("car_id",idForDialog );
                    	//startActivityForResult(analysisIntent,SHOW_ANALYSIS);
                    	Log.i(TAG,"record id found to display in record in analysis " + idForDialog);
                    	RECORD_ID_FORANALYSIS = idForDialog;
                    	FuelTracker.tabHost.setCurrentTab(2);
                    	
                    }
                    	
                }
            })
            .create();
        case DELETE_ALL:
			return new AlertDialog.Builder(getParent()).setTitle("Delete")
					.setMessage("Do you want to delete all the Vehicles?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									deleteAllCars();
								
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
	public void deleteAllCars()
	{
		FuelTrackerAsync ftAsync=getAsynClassObject("Deleting all vehicles");
    	ftAsync.execute(FuelTrackerQueryCommand.DELETE_ALL_CAR,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,
    						getApplicationContext());
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.vehicles);
        View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.vehicles, null);
        setContentView(viewToLoad);
        prepareUI();
        
    }		
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == SHOW_EDITVEH)
		{			
			Log.i(TAG,"Back to vehicles screen");
			vehicleListAdapter.items.clear();
			vehicleListAdapter.notifyDataSetChanged();
			this.fillRecords();
		}
	}
	
	private class ListAdapter extends ArrayAdapter<VehicleItem>{

		private ArrayList<VehicleItem> items;
		private VehicleItem o;
		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<VehicleItem> items) {
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
				convertView = mInflater.inflate(R.layout.vehiclesrow, null);
				
				LinearLayout ll= (LinearLayout) convertView.findViewById(R.id.ll);
				if(position!=0 && position%2!=0)
					ll.setBackgroundResource(R.drawable.selector_odd);
				TextView make = (TextView)convertView.findViewById(R.id.carname);
				TextView model = (TextView)convertView.findViewById(R.id.carmodel);				
				TextView dist = (TextView)convertView.findViewById(R.id.dist);
				TextView vol = (TextView)convertView.findViewById(R.id.vol);
				TextView consum = (TextView)convertView.findViewById(R.id.consum);
				TextView distanceCovered = (TextView)convertView.findViewById(R.id.distcovered);
				TextView fillUps = (TextView)convertView.findViewById(R.id.fillups);
				TextView consumVal = (TextView)convertView.findViewById(R.id.consumevalue);
				
				make.setText(o.getMake());
				model.setText(o.getModel());
				dist.setText(o.getDistance());
				vol.setText(o.getVolume());
				consum.setText(o.getConsumption());
				distanceCovered.setText(o.getDistCovered());
				fillUps.setText(o.getFillUps());
				consumVal.setText(o.getConsumValue());
				return convertView;
			}
			return null;
		}		
	}
	
	private FuelTrackerAsync getAsynClassObject(String msg){
		
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(getParent(),msg);
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		
		return ftAsync;
	}
	
	public void fillRecords()
	{
		FuelTrackerAsync ftAsync=getAsynClassObject("Please wait while retreiving records.");
		ftAsync.execute(FuelTrackerQueryCommand.GET_ALL_CARS_INFO,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext());		
		
	}	

    private void prepareUI()
    {
        this.setTitle("Vehicles");
        vehiclesListView = (ListView)findViewById(R.id.vehicleslist);        
        vehicleListAdapter = new ListAdapter(this,R.layout.vehiclesrow,vehiclesList);
        vehiclesListView.setAdapter(vehicleListAdapter);
        fillRecords();
        vehiclesListView.setOnItemClickListener(this);
        //vehiclesListView.setOnItemLongClickListener(this);
        registerForContextMenu(vehiclesListView);
        addVehicle = (Button)findViewById(R.id.addvehicle);
        addVehicle.setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(FuelTracker.tabHost.getWindowToken(), 0);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String make;
		String model;
		make = vehicleListAdapter.getItem(info.position).getMake();
		model = vehicleListAdapter.getItem(info.position).getModel();	
		Log.i(TAG, "info.position: "+info.position);
		RECORD_POSITION_FORANALYSIS=info.position;
		idForDialog = vehicleListAdapter.getItem(info.position).getId();
		tempVehicleItem = (VehicleItem)vehicleListAdapter.items.get(info.position);
		ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
		configurationAdapter.updateConfigurationTable(1, null, null, null, null, idForDialog);
		menu.setHeaderTitle(make+" "+model);
		String[] menuItems = getResources().getStringArray(R.array.select_dialog_items);
		for (int i = 0; i < menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
    }
    
    public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if ( item.getTitle().toString().equals("Edit") ){
        	/*Intent edit = new Intent(Vehicles.this,EditVehicle.class);
			edit.putExtra("RecordId", idForDialog);
    		startActivityForResult(edit, SHOW_EDITVEH);*/
    		 Intent previewMessage = new Intent(getParent(), EditVehicle.class);
    		 previewMessage.putExtra("RecordId", idForDialog);
             TabGroupActivity parentActivity = (TabGroupActivity)getParent();
             parentActivity.startChildActivity("EditVehicle", previewMessage);
        }
        else if ( item.getTitle().toString().equals("Delete") ){
        	FuelTrackerAsync ftAsync=getAsynClassObject("Deleting record.");
        	ftAsync.execute(FuelTrackerQueryCommand.DELETE_CARS_ACCOUNT,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,
        						getApplicationContext(),idForDialog);
        	
        }
        else if ( item.getTitle().toString().equals("Delete All") ){
        	showDialog(DELETE_ALL);
        }
        else if ( item.getTitle().toString().equals("Analysis")){
        	Log.i(TAG,"record id found to display in record in analysis " + idForDialog);
        	RECORD_ID_FORANALYSIS = idForDialog;
        	frmAnalaysis=1;
        	Log.i(TAG, "frmAnalaysis: "+frmAnalaysis);
        	FuelTracker.tabHost.setCurrentTab(2);
        	
        }
        else if ( item.getTitle().toString().equals("FillUps"))
        {
        	FuelTracker.tabHost.setCurrentTab(1);
        }
		return true;
    }

	public void onClick(View arg0) {
		
		switch(arg0.getId()){  
	    	case R.id.addvehicle:  		           
	    	 Log.i(TAG, "Opening Activity ");
    		 /*Intent edit = new Intent(Vehicles.this,EditVehicle.class);
    		 startActivityForResult(edit, SHOW_EDITVEH);  */
	    	 Intent previewMessage = new Intent(getParent(), EditVehicle.class);
	         TabGroupActivity parentActivity = (TabGroupActivity)getParent();
	         parentActivity.startChildActivity("EditVehicle", previewMessage);
	         break;  
	      
	     }  
	}


	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub		
		switch(arg0.getId()){
			case R.id.vehicleslist:
				if ( itemLongClickFlag == false )
				{
					Log.i(TAG,"Item selected : "+ arg0.getId() +" "+ arg1.getId() +" " + arg2 + " " + arg3);
					VehicleItem v = (VehicleItem)vehicleListAdapter.items.get(arg2);
					Log.i(TAG,"Record check : " + v.getId() +  " " + v.getMake() +"_"+ v.getModel());
					/*Intent edit = new Intent(Vehicles.this,EditVehicle.class);
					edit.putExtra("RecordId", v.getId());
		    		startActivityForResult(edit, SHOW_EDITVEH);  */
					Intent previewMessage = new Intent(getParent(), EditVehicle.class);
					previewMessage.putExtra("RecordId", v.getId());
			        TabGroupActivity parentActivity = (TabGroupActivity)getParent();
			        parentActivity.startChildActivity("EditVehicle", previewMessage);
				}
				else if ( itemLongClickFlag == true )
				{
					itemLongClickFlag = false;
				}
				break;
		
		}
		
	}


	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.vehicleslist:
				Log.i(TAG,"Long clicked Item selected : " + arg0.getId() +" "+ arg1.getId() +" " + arg2 + " " + arg3);
				VehicleItem v = (VehicleItem)vehicleListAdapter.items.get(arg2);
				tempVehicleItem = (VehicleItem)vehicleListAdapter.items.get(arg2);
				Log.i(TAG,"Record check in long item click : " + v.getId() +  " " + v.getMake() +"_"+ v.getModel());
				idForDialog = v.getId();
				makeForDialog = v.getMake();
				modelForDialog=v.getModel();
				itemLongClickFlag = true;
				ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
				configurationAdapter.updateConfigurationTable(1, null, null, null, null, idForDialog);
				showDialog(DIALOG_LIST);
				break;
		}		
		return false;
	}


	public void onHTTPResponseComplete(Object data, String request) {
		
		if ( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_ALL_CARS_INFO))
		{
			Cursor cursor = (Cursor)data;
			showVehiclesList(cursor);
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.DELETE_CARS_ACCOUNT))
		{
			Boolean deleteStatus = (Boolean)data;
			Log.i(TAG,"Record delete status " + deleteStatus);
			if ( deleteStatus == true )
			{
				FuelRefillsAdapter fuelRefills = new FuelRefillsAdapter(getApplicationContext());
				fuelRefills.delFulRefilUsingCarID(idForDialog);
				vehicleListAdapter.remove(tempVehicleItem);
				vehicleListAdapter.notifyDataSetChanged();
				
//				vehicleListAdapter.items.clear();
//    			vehicleListAdapter.notifyDataSetChanged();
//    			fillRecords();
			}
		}
		else if ( data != null && request.contentEquals(FuelTrackerQueryCommand.DELETE_ALL_CAR))
		{
			
			FuelRefillsAdapter fuelRefills = new FuelRefillsAdapter(getApplicationContext());
			fuelRefills.delAllFuleRefill();
			vehicleListAdapter.clear();
    		vehicleListAdapter.notifyDataSetChanged();
    			
			
		}
		
	}


	public void onHTTPResponseProgress() {
		// TODO Auto-generated method stub
		
	}        
	
	public void showVehiclesList(Cursor cursor)
	{
		NumberFormat nf = NumberFormat.getNumberInstance( );
		nf.setMinimumFractionDigits(2);		
		nf.setMaximumFractionDigits(2);
		Log.i(TAG,"Get vehicles count : " + cursor.getCount());
		Stats sts = Stats.getStatsInstance();
		while (cursor.moveToNext()) 
		{
			VehicleItem vi1 = new VehicleItem();	
			vi1.setId(cursor.getInt(cursor.getColumnIndex("id")));
			vi1.setMake(cursor.getString(cursor.getColumnIndex("make")));
			vi1.setModel(cursor.getString(cursor.getColumnIndex("model")));
			vi1.setDistance(cursor.getString(cursor.getColumnIndex("distance_in")));
			vi1.setVolume(cursor.getString(cursor.getColumnIndex("volume_in")));
			vi1.setConsumption(cursor.getString(cursor.getColumnIndex("consumption_in")));
			vi1.setNote(cursor.getString(cursor.getColumnIndex("note")));
			vi1.setTank_capacity(cursor.getFloat(cursor.getColumnIndex("tank_capacity")));
			
			FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
			Cursor cursor2 = fuelRefillsAdapter.getStatsOdoDateinfo(cursor.getInt(cursor.getColumnIndex("id")));
			Log.i(TAG,"cursor count " + cursor2.getCount());
			
			Cursor cursor3 = fuelRefillsAdapter.getStatsData(cursor.getInt(cursor.getColumnIndex("id")));
			Log.i(TAG,"cursor3 count " + cursor3.getCount());
			int index_sum;
			int no_of_fillup=0;
			float total_mileage=0.0f;
			float totalFuel=0.0f;
			if ( cursor3.getCount() != 0)
			{	
				index_sum = cursor3.getColumnIndex("sum(volume)");
				cursor3.moveToFirst();
				Log.i(TAG,"Index value of sum(volume) " + index_sum + " " + cursor3.getFloat(index_sum) );
				totalFuel = cursor3.getFloat(index_sum);
			
				no_of_fillup=cursor3.getInt(cursor3.getColumnIndex("count(id)"));
				Log.i(TAG,"count(id)------------"+no_of_fillup );
				
				total_mileage=cursor3.getFloat(cursor3.getColumnIndex("sum(mileage)"));
				Log.i(TAG,"sum(mileage)------------before"+total_mileage );
				//perform conversion get data according to user's configuration
				total_mileage=DataConversion.getStatsInstance().convertConsumptionType(total_mileage,vi1.getConsumption());
				Log.i(TAG,"sum(mileage)------------"+total_mileage );
			}
			if ( cursor2.getCount()!=0 )
			{
				int odoReadingIndex = cursor2.getColumnIndex("odometer_reading");
				cursor2.moveToFirst();
				float firstOdoReading = cursor2.getFloat(odoReadingIndex);
				if(vi1.getDistance().contentEquals("mi"))	{
					firstOdoReading=DataConversion.getStatsInstance().getMilesFromkm(firstOdoReading);
				}
				cursor2.moveToLast();
				//float lastOdoReading = cursor2.getFloat(odoReadingIndex);
				
				vi1.setDistCovered(""+(nf.format(firstOdoReading))+" "+vi1.getDistance());
				vi1.setFillUps(""+cursor2.getCount()+" Fill-ups");
				
				float mileage =0.0f;
				
				if(total_mileage>0 && (no_of_fillup-1)>0){
					mileage = total_mileage/(no_of_fillup-1);
				}
				vi1.setConsumValue(""+DataConversion.getStatsInstance().roundTwoDecimals(mileage) +" " +vi1.getConsumption()); //nf.format(mileage)
			}
			 
			vehicleListAdapter.items.add(vi1);			
		}
		vehicleListAdapter.notifyDataSetChanged();//when item get added in list it will automatically call getView method of list adapter
	}//consumption_in
	
	
	
}