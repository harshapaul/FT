package com.fueltracker.UI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.achartengine.GraphicalView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

import com.fueltracker.FT_DB.CarAdapter;
import com.fueltracker.FT_DB.ConfigurationAdapter;
import com.fueltracker.FT_DB.FuelRefillsAdapter;
import com.fueltracker.asynctask.FuelTrackerAsync;
import com.fueltracker.asynctask.FuelTrackerProgressBar;
import com.fueltracker.asynctask.FuelTrackerQueryCommand;
import com.fueltracker.asynctask.HTTPResponseListener;
import com.fueltracker.model.AnalyseData;
import com.fueltracker.model.VehicleItem;
import com.fueltracker.utils.DataConversion;
import com.fueltracker.utils.Stats;
/**
 * @UiAuthor		: 	prashant.daware
 * @date			:	8/10/2011
 * @purpose			:	analysis screen and get option to see in graph
 * @ModifiedBy		:	
 * @ModificationDate:	23/08/2011
 * @Modification	:	changed for avg_per_cost_kg function
 * */
public class Analysis extends ExpandableListActivity implements HTTPResponseListener, OnItemSelectedListener, OnClickListener
{
	private ViewFlipper vf;//for animation
	private Spinner spinnerCars,spinnerFuelGraphs;
	private Button plotGraph;
	private String currencyType;
	private String array_cars[] = new String[3];
	private int spinnerPosition;
	public static int fuelGraphPosition;
	private GraphicalView gView=null;//for graph plotting
	private LinearLayout viewLayout;
	private SimpleExpandableListAdapter expListAdapter;
	static final int PLOT_GRAPH = 0;//for alert dialog
	private String[] strings;// = {"Swift","Zen","Alto","Santro","Manza"};
	//private int arr_images[] = { R.drawable.spinner_car1,R.drawable.spinner_car2, R.drawable.spinner_car3, R.drawable.spinner_car4, R.drawable.spinner_car5,R.drawable.spinner_car6,R.drawable.spinner_car7};
	private int[] carID;
	private VehicleItem vi1;
	private VehicleItem pertiCarInfo=null;
	private float TotalVolume,firstReading,lastReading,totalCost;
	private int no_of_Fillups;
	private String dist_in,volume_in,consumtion_in;
	private final String TAG="Analysis";
	private String[] fuelString={"Fuel consumption","Accumulated","Total Mileage","Total Fuel"};
	static final String groupTitles[]={"Fuel consumption","Accumulated (fill ups and other costs)","Totals (fuel/fill ups)","Average (Fuel/Fill ups)"};
	static final String childTitles[][]={
		{
			"Average fuel consumption"
		},
		{
			"Accumulate avg. cost p.m."
		},
		{
			"Total avg. mileage",
			"Total fuel"
		},
		{
			"Average fuel cost per km"
		}
										
	};
	
	static final String childTitles1[][]={
		{
			"N/A"
		},
		{
			"N/A"
		},
		{
			"N/A",
			"N/A"
		},
		{
			"N/A"
		}
										
	};
	static String childTitles2[][]=null;
	private Calendar getdate;
	private DateFormat dateFormat ;
	public static long refillDates[]=null;
	public static float fuelEntered[]=null;
	public static String graphValues[]=null;
	public static String graphTypeName="";
	private int selectedCarId;
	private boolean validCarInfor=false;
	private LinearLayout buttonLayout;
	private boolean layoutOrient;
	public static int analysisSelectedCarId;
	private int screenRotationCount=0;
	private ImageButton expandList;
	private boolean expanded=false;
	private boolean frmFillup=true;
	private boolean isGraph=false;
	private boolean availFillups=false;
	private int ctt=0;
	private boolean isDisplay=false;
	private ArrayList<VehicleItem> vehicleListAdapter;
	//static final String childTitles1[][]=new String[3][1];
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	to show alert for plotting graph
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:   	
	 * @Modification	:	
	 * */
	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
		case PLOT_GRAPH:
			return new AlertDialog.Builder(Analysis.this)
			.setTitle("Do you want to Plot Graph?").setPositiveButton("Plot", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					spinnerFuelGraphs.setVisibility(View.VISIBLE);
					FuelTrackerAsync ftAsync=getAsynClassObject();				
					ftAsync.execute(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),spinnerPosition);
					spinnerFuelGraphs.setEnabled(true);
					showAnimation();
					plotGraph.setText("Values");
					/*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					buttonLayout.setVisibility(View.INVISIBLE);
					buttonLayout.setVisibility(View.GONE);*/
				}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface arg0, int arg1) 
				{
					
				}
			}).create();
		}
		return null;
	}
	public void onCreate(Bundle savedInstanceState) 
	 {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.analysis);        
	        Log.i(TAG,"getResources().getConfiguration().orientation: "+getResources().getConfiguration().orientation);
	      
	        Log.i(TAG,"From vehicles record selected is " + Vehicles.RECORD_ID_FORANALYSIS);
	        
	        vf=(ViewFlipper) findViewById(R.id.layoutswitcher);
			plotGraph=(Button)findViewById(R.id.plotGraph);
			
			viewLayout=(LinearLayout) findViewById(R.id.viewLayout);
			buttonLayout=(LinearLayout) findViewById(R.id.ButtonLayout);
			
			spinnerCars=(Spinner) findViewById(R.id.spinnerCars);
			
			spinnerFuelGraphs=(Spinner)findViewById(R.id.graph_type);
			ArrayAdapter adptGraphs=new ArrayAdapter(Analysis.this,R.layout.spinner_item_graph,fuelString);
			adptGraphs.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
			spinnerFuelGraphs.setAdapter(adptGraphs);
			spinnerFuelGraphs.setEnabled(false);
			//spinnerFuelGraphs.setVisibility(View.INVISIBLE);
			
			
			FuelTrackerAsync ftAsync=getAsynClassObject();				
			ftAsync.execute(FuelTrackerQueryCommand.GET_ALL_CARS_INFO,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext());
			/*array_cars[0]="Swift";
			array_cars[1]="Zen";
			array_cars[2]="Alto";*/
			/*ArrayAdapter adptCars=new ArrayAdapter(Analysis.this,android.R.layout.simple_spinner_item,array_cars);
			adptCars.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
			spinnerCars.setAdapter(adptCars);*/
			//spinnerCars.setAdapter(new MyAdapter(Analysis.this, R.layout.spinner_row, strings));
			/*spinnerCars.setOnItemSelectedListener(new OnItemSelectedListener() 
			{

				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
				{
					Log.i(TAG,"spinnerCars.getItemIdAtPosition(0): "+position+": "+spinnerCars.getItemIdAtPosition(position));
					Log.i(TAG,"spinnerCars.getItemAtPosition(0): "+position+": "+spinnerCars.getItemAtPosition(position));
					if(strings.length>0)
					{
						String carInfo=getConfiguration();
						//String carInfo="NA";
						Log.i(TAG,"String CarINfo: "+carInfo);
						if(carInfo.equalsIgnoreCase("NA"))
						{
							Log.i(TAG,"NNNNNNNNAAAAAAAAA");
						}
						else
						{
							Log.i(TAG,"parent.getCount(): "+parent.getCount());
							String[]cInfo=carInfo.split("~");
							if(cInfo[1].length()>0)
							{
								for(int ct=0;ct<parent.getCount();ct++)
								{
									if(spinnerCars.getItemAtPosition(ct).toString().equalsIgnoreCase(cInfo[1]))
									{
										spinnerCars.setSelection(ct);
										spinnerPosition=Integer.parseInt(cInfo[0]);
									}
								}
							}
							Log.i(TAG,"spinnerPosition: "+spinnerPosition);
							
							FuelTrackerAsync ftAsync=getAsynClassObject();				
							ftAsync.execute(FuelTrackerQueryCommand.GET_CARS_INFO,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext(),spinnerPosition);
						}
					}
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});*/
			
			spinnerFuelGraphs.setOnItemSelectedListener(new OnItemSelectedListener() 
			{
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
				{
					FuelTrackerAsync ftAsync=getAsynClassObject();				
					ftAsync.execute(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),spinnerPosition);
					
					fuelGraphPosition=spinnerFuelGraphs.getSelectedItemPosition();
					//plotgraph(fuelGraphPosition);
				}
				public void onNothingSelected(AdapterView<?> arg0) 
				{
					
				}
			});
			
			plotGraph.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View arg0) 
				{
					Log.i(TAG, "Fillups are available: "+availFillups);
					if(availFillups==true)
					{
						if(validCarInfor==true)
						{
							System.out.println("arg0.getId():***************** "+arg0.getId());
							String str=plotGraph.getText().toString();
							if(str.equalsIgnoreCase("Graph"))
							{
								//showDialog(PLOT_GRAPH);
								spinnerFuelGraphs.setVisibility(View.VISIBLE);
								Log.i(TAG, "Spinner postion on clik graph button: "+spinnerPosition);
								FuelTrackerAsync ftAsync=getAsynClassObject();				
								ftAsync.execute(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),spinnerPosition);
								spinnerFuelGraphs.setEnabled(true);
								showAnimation();
								plotGraph.setText("Values");
								//spinnerCars.setEnabled(false);
								expandList.setEnabled(false);
								expandList.setVisibility(View.GONE);
								isGraph=true;
							}
							else if(str.equalsIgnoreCase("Values"))
							{
								plotGraph.setText("Graph");
								spinnerFuelGraphs.setVisibility(View.GONE);
								//spinnerCars.setEnabled(true);
								spinnerFuelGraphs.setEnabled(false);
								expandList.setEnabled(true);
								expandList.setVisibility(View.VISIBLE);
								//spinnerFuelGraphs.setVisibility(View.INVISIBLE);
								showAnimation();
								/*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
								buttonLayout.setVisibility(View.VISIBLE);*/
								isGraph=false;
							}
						}
					}
					else
					{
						AlertDialog.Builder alt_bld = new AlertDialog.Builder(Analysis.this);
						alt_bld.setMessage("No fillups available");
						alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface arg0, int arg1) 
							{
								
							}
						});
						AlertDialog alt=alt_bld.create();
						alt.setTitle("Alert");
						alt.setIcon(android.R.drawable.ic_dialog_alert);
						alt.show();
						//Toast.makeText(Analysis.this, "No Fillups are available", Toast.LENGTH_LONG).show();
					}
				}
			});
			expandList=(ImageButton)findViewById(R.id.expandList);
			expandList.setOnClickListener(this);
	 }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		
		getScreenOrient();
		Log.i(TAG,"getScreenOrient(): "+layoutOrient);
		Log.i(TAG,"newConfig.orientation: "+getResources().getConfiguration().orientation);
		Log.i(TAG,"Orientation from GraphView: "+GraphView.screenOrient);
		if((newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) && (isGraph==true))
        {
			screenRotationCount++;
			Log.i(TAG,"screenRotationCount: "+screenRotationCount);
        	/*gView=null;
    		gView=new TimePlot().execute(getApplicationContext());
            addViewToDialogBox();*/
        	Intent mygraphIntent=new Intent(Analysis.this,GraphView.class);
        	startActivityForResult(mygraphIntent, 0);
        }
        
        
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		Configuration con=new Configuration();
		//con.setToDefaults();
		con.orientation=Configuration.ORIENTATION_PORTRAIT;
		onConfigurationChanged(con);
	}
	@Override
	protected void onPause() 
	{
		super.onPause();
		Configuration con=new Configuration();
		//con.setToDefaults();
		con.orientation=Configuration.ORIENTATION_PORTRAIT;
		onConfigurationChanged(con);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode==RESULT_OK)
		{
			onRestart();
			//Toast.makeText(Analysis.this, "Back to analysis", Toast.LENGTH_SHORT).show();
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	public String getConfiguration()
	{
		ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
		Cursor rowCursor=configurationAdapter.getAllConfigurationInfo();
		if(rowCursor!=null && rowCursor.getCount()>0)
		{
			rowCursor.moveToFirst();
			selectedCarId=rowCursor.getInt(rowCursor.getColumnIndex("sel_car_id"));
			Log.i(TAG,"selectedCarId: "+selectedCarId);
		
			if(selectedCarId!=-1)
			{
				CarAdapter carAdpt=new CarAdapter(getApplicationContext());
				Cursor carCursor=carAdpt.getCarInfo(selectedCarId);
				Log.i(TAG, "CarAdptCounter: "+carCursor.getCount());
				if(carCursor!=null && carCursor.getCount()>0)
				{
					carCursor.moveToFirst();
					//Log.i(TAG,carCursor.getInt(carCursor.getColumnIndex("id")));
					Log.i(TAG,carCursor.getString(carCursor.getColumnIndex("make")));
					Log.i(TAG,carCursor.getString(carCursor.getColumnIndex("model")));
					Log.i(TAG,String.valueOf(carCursor.getInt(carCursor.getColumnIndex("id")))+"~"+carCursor.getString(carCursor.getColumnIndex("make"))+" "+carCursor.getString(carCursor.getColumnIndex("model")));
					return String.valueOf(carCursor.getInt(carCursor.getColumnIndex("id")))+"~"+carCursor.getString(carCursor.getColumnIndex("make"))+" "+carCursor.getString(carCursor.getColumnIndex("model"));
				}
				else
				{
					Log.i(TAG, "vehicleListAdapter.size(): "+vehicleListAdapter.size());
					//int pos=-1;
					if(vehicleListAdapter.size()>0)
					{
						/*for(int i=0;i<vehicleListAdapter.size();i++)
						{
							Log.i(TAG, "vehicleListAdapter.get(i).getId() in Analysis: "+vehicleListAdapter.get(i).getId());
							if(vehicleListAdapter.get(i).getId().compareTo(selectedCarId)==0)
							{
								Log.i(TAG, "Pos in Analaysis"+pos);
								pos=i;
								break;
							}
						}*/
						CarAdapter carAdpt1=new CarAdapter(getApplicationContext());
						Cursor carCursor1=carAdpt1.getCarInfo(vehicleListAdapter.get(0).getId());
						Log.i(TAG, "CarAdptCounter: "+carCursor1.getCount());
						if(carCursor1!=null && carCursor1.getCount()>0)
						{
							carCursor1.moveToFirst();
							//Log.i(TAG,carCursor.getInt(carCursor.getColumnIndex("id")));
							Log.i(TAG,carCursor1.getString(carCursor1.getColumnIndex("make")));
							Log.i(TAG,carCursor1.getString(carCursor1.getColumnIndex("model")));
							Log.i(TAG,String.valueOf(carCursor1.getInt(carCursor1.getColumnIndex("id")))+"~"+carCursor1.getString(carCursor1.getColumnIndex("make"))+" "+carCursor1.getString(carCursor1.getColumnIndex("model")));
							return String.valueOf(carCursor1.getInt(carCursor1.getColumnIndex("id")))+"~"+carCursor1.getString(carCursor1.getColumnIndex("make"))+" "+carCursor1.getString(carCursor1.getColumnIndex("model"));
						}
					}
					
					return "NA";
				}
			}
			else
			{
				return "NA";
			}
			
		}
		else
		{
			return "NA";
		}
	}
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	to create custom spinner list view
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:	
	 * @Modification	:	
	 * */
	public class MyAdapter extends ArrayAdapter<String>
	{

		public MyAdapter(Context context, int textViewResourceId, String[] objects) 
		{
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		public View getCustomView(int position, View convertView, ViewGroup parent) 
		{
				 
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.company);
            label.setText(strings[position]);
         
            /*ImageView icon=(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);*/
 
            return row;
        }
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) 
		{
			 LayoutInflater inflater=getLayoutInflater();
	            View row=inflater.inflate(R.layout.spinner_row, parent, false);
	            TextView label=(TextView)row.findViewById(R.id.company);
	            label.setText(strings[position]);
	            label.setTextColor(Color.BLACK);
	            /*ImageView icon=(ImageView)row.findViewById(R.id.image);
	            icon.setImageResource(arr_images[position]);*/
	 
	            return row;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			return getCustomView(position, convertView, parent);
		}
		
	}
	
	private FuelTrackerAsync getAsynClassObject()
	{
		FuelTrackerProgressBar ftpBar= new FuelTrackerProgressBar(Analysis.this,"Please wait while retrieving records");
		FuelTrackerAsync ftAsync=new FuelTrackerAsync(this, ftpBar);
		return ftAsync;
	}
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	for plot graph and that class return type is graphical view and then add linear layout
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:	
	 * @Modification	:	
	 * */
	private void plotgraph()
	{
		//getScreenOrient();
		viewLayout.removeViewInLayout(gView);
		gView=new GraphicalView(getApplicationContext(), null);
		gView=new TimePlot().execute(getApplicationContext());
		gView.invalidate();
		//gView.setBackgroundResource(R.drawable.activity_background);
		viewLayout.addView(gView);
		
		/*Intent graphIntent=new Intent(Analysis.this,GraphView.class);
		startActivityForResult(graphIntent, 0);*/
	}
	
	private void getScreenOrient()
	{
		int orient=getResources().getConfiguration().orientation;
		switch(orient)
		{
		case Configuration.ORIENTATION_LANDSCAPE:
			layoutOrient=true;
			break;
		case Configuration.ORIENTATION_PORTRAIT:
			layoutOrient=false;
			break;
		default:
			layoutOrient=false;
			break;
		}
	}
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	animation sequence for animated screen
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:	
	 * @Modification	:	
	 * */
	//showing animation
	private void showAnimation()
	{
		/*vf.setInAnimation(inFromLeftAnimation());
        vf.setOutAnimation(outToRightAnimation());
        vf.setInAnimation(shrink_to_middle());
        vf.setOutAnimation(grow_from_middle());*/
        vf.setInAnimation(Analysis.this, R.anim.hyperspace_in);
        vf.setOutAnimation(Analysis.this, R.anim.hyperspace_out);
        vf.showNext();
	}
	//gives information when we clik on child itmes
	public boolean onChildClick
	(ExpandableListView parent,View v,int groupPosition,int childPosition,long id) 
	{
		Log.i(TAG,"Inside onChildClick at groupPosition = " + groupPosition +" Child clicked at position " + childPosition);
		return true;
    }
	//creating main group list
	private List createGroupList() 
	{
		//String st=spinnerCars.getSelectedItem().toString();  
		ArrayList result = new ArrayList();
		  for( int i = 0 ; i < groupTitles.length ; ++i ) 
		  {
				HashMap m = new HashMap();
			    m.put( "groupTitle",groupTitles[i]);
				result.add( m );
		  }
		  return (List)result;
	}
	//creating child list itmes
	private List createChildList() 
	{
		String str=spinnerCars.getSelectedItem().toString();
		ArrayList result = new ArrayList();
		for( int i = 0 ; i < childTitles.length ; ++i ) 
		{
			// Second-level lists
			   ArrayList secList = new ArrayList();
			    for( int n = 0 ; n < childTitles1[i].length ; n += 1 ) 
			    {
				    HashMap child = new HashMap();
					child.put( "subTitle", childTitles[i][n] );
					//child.put( "values", getCarValue(i,n) );//childTitles1[i][n] );
					child.put( "values",childTitles2[i][n] );
				    //getCarValue(i,n);
				    secList.add( child );
			    }
		   
		  result.add( secList );
		}
		return result;
	}
	/*private String getCarValue(int k,int l)
	{
		String st=null;
		switch (k) 
		{
		case 0:
			st=pertiCarInfo.getConsumption().toString();
			break;
		case 1:
			st=pertiCarInfo.getMake().toString();
			break;
		case 2:
			if(l==0)
			{
				st=pertiCarInfo.getDistance().toString();
			}
			else if(l==1)
			{
				st=pertiCarInfo.getVolume().toString();
			}
			break;
		case 3:
			st=pertiCarInfo.getModel().toString();
			break;
		default:
			break;
		}
		
		return st;
	}*/
	public void  onContentChanged() 
	{
		Log.i(TAG,"onContentChanged");
		super.onContentChanged();
	}
		    /* This function is called on each child click */
		/* This function is called on expansion of the group */
	public void  onGroupExpand  (int groupPosition) 
	{
		try
		{
			Log.i(TAG,"Group exapanding Listener => groupPosition = " + groupPosition);
		}
		catch(Exception e)
		{
			Log.i(TAG," groupPosition Errrr +++ " + e.getMessage());
		}
	}
	
	@Override
	public ExpandableListAdapter getExpandableListAdapter() 
	{
		return super.getExpandableListAdapter();
	}
	public static Animation shrink_to_middle()
	{
		Animation sTom=new ScaleAnimation(3.0f, 1.0f, 0.0f, 3.0f, ScaleAnimation.RELATIVE_TO_PARENT,ScaleAnimation.RELATIVE_TO_PARENT );
		sTom.setDuration(1000);
		sTom.setInterpolator(new AccelerateInterpolator());
		return sTom;
	}
	public static Animation grow_from_middle()
	{
		Animation gFrm=new ScaleAnimation(1.0f, 3.0f, 3.0f, 0.0f, ScaleAnimation.RELATIVE_TO_PARENT,ScaleAnimation.RELATIVE_TO_PARENT );
		gFrm.setDuration(1000);
		gFrm.setInterpolator(new AccelerateInterpolator());
		return gFrm;
	}
	public void onHTTPResponseComplete(Object data, String request) 
	{
		if ( data != null && request.contentEquals(FuelTrackerQueryCommand.GET_ALL_CARS_INFO))
		{
			Cursor cursor = (Cursor)data;
			showVehiclesList(cursor);
		}
		else if(data != null && request.contentEquals(FuelTrackerQueryCommand.GET_CARS_INFO))
		{
			Cursor cursorCarInfo=(Cursor)data;
			Log.i(TAG,"cursorCarInfo count: "+cursorCarInfo.getCount());
			showCarInfo(cursorCarInfo);
		}
		else if(data != null && request.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO))
		{
			System.out.println("============ in second spinner selection(cursorCarInfo)");
			Cursor cursorRefillInfo=null;
			refillDates=null;
			fuelEntered=null;
			graphValues=null;
			switch(fuelGraphPosition)
			{
			case 0:
				cursorRefillInfo=(Cursor)data;
				Log.i(TAG, "cursorRefillInfo.getCount(): "+cursorRefillInfo.getCount());
				if(cursorRefillInfo.getCount()>0)
				{
					getCarRefillInfo(cursorRefillInfo,fuelGraphPosition);
				}
				else
				{
					graphValues=new String[1];
					graphValues[0]="0:0";
				}
				break;
			case 1:
				getCarRefillInfo(null,fuelGraphPosition);
				break;
			case 2:
				getCarRefillInfo(null,fuelGraphPosition);
				break;
			case 3:
				getCarRefillInfo(null,fuelGraphPosition);
				break;
			default:
				cursorRefillInfo=(Cursor)data;
				if(cursorRefillInfo.getCount()>0)
				{
					getCarRefillInfo(cursorRefillInfo,fuelGraphPosition);
				}
				else
				{
					graphValues=new String[1];
					graphValues[0]="0:0";
				}
				break;
			}
			//plotgraph(fuelGraphPosition);
			if((graphValues!=null)&&(graphValues.length>0))
			{
				Log.i(TAG, "Yessssssssssssssssssssssssssssssss");
				plotgraph();
			}
			else
			{
				Log.i(TAG, "n00000000000000000000000000000000");
				gView=null;
				//gView.invalidate();
				viewLayout.removeViewInLayout(gView);
			}
		}
	}
	
	
	
	public void showCarInfo(Cursor cursor)
	{
		pertiCarInfo=new VehicleItem();
		ConfigurationAdapter cAdpt=new ConfigurationAdapter(getApplicationContext());
		Cursor cursorConfig=cAdpt.getAllConfigurationInfo();
		if(cursorConfig!=null && cursorConfig.getCount()>0)
		{
			cursorConfig.moveToFirst();
			selectedCarId=cursorConfig.getInt(cursorConfig.getColumnIndex("sel_car_id"));
		}
		
		 
		String[]currencyType1=cursorConfig.getString(cursorConfig.getColumnIndex("currency")).split("-");
		currencyType=currencyType1[0].trim();
		Log.i(TAG,"=========currencyType====================: "+currencyType);
		dist_in="";
		volume_in="";
		consumtion_in="";
		/*String arrayNames[]=cursor.getColumnNames(); 
		for(String a:arrayNames) 
		{
			 Log.i(TAG,"Col names: "+ a); 
		}*/
		Log.i(TAG, "outside showCarInfo");
		if((cursor!=null) && (cursor.getCount()>0))
		{
			cursor.moveToFirst();
			pertiCarInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			pertiCarInfo.setMake(cursor.getString(cursor.getColumnIndex("make")));
			pertiCarInfo.setModel(cursor.getString(cursor.getColumnIndex("model")));
			pertiCarInfo.setDistance(cursor.getString(cursor.getColumnIndex("distance_in")));
			pertiCarInfo.setVolume(cursor.getString(cursor.getColumnIndex("volume_in")));
			pertiCarInfo.setConsumption(cursor.getString(cursor.getColumnIndex("consumption_in")));
			//--------------------
			dist_in=cursor.getString(cursor.getColumnIndex("distance_in"));
			Log.i(TAG,"dist_in: "+dist_in);
			volume_in=cursor.getString(cursor.getColumnIndex("volume_in"));
			Log.i(TAG,"volume_in: "+volume_in);
			consumtion_in=cursor.getString(cursor.getColumnIndex("consumption_in"));
			Log.i(TAG,"consumtion_in: "+consumtion_in);
			
			Log.i(TAG,"Car id: "+pertiCarInfo.getId());
			//--------------------
			childTitles2=new String [4][2];
			//childTitles2[0][0]=avgFuelConsumption()+" "+volume_in+"/p.m.";//pertiCarInfo.getConsumption().toString();
			childTitles2[0][0]=getFuelConsumptionPerMonth()+" "+volume_in+"/p.m.";//pertiCarInfo.getConsumption().toString();
			childTitles2[1][0]=getAccumulatedInfo()+" "+currencyType;//pertiCarInfo.getDistance().toString();
			childTitles2[2][0]=totalMileage(pertiCarInfo.getId())+" "+consumtion_in;//pertiCarInfo.getVolume().toString();
			childTitles2[2][1]=String.valueOf(TotalVolume)+" "+volume_in;//pertiCarInfo.getMake().toString();
			childTitles2[3][0]=avg_fuel_cost_per_km()+" "+currencyType;//pertiCarInfo.getModel().toString();
			//--------------------
			Log.i(TAG, "in showCarInfo");
			AnalyseData adata=new AnalyseData();
			adata.setAvgFuelConsumption(Float.valueOf(getFuelConsumptionPerMonth()));
			adata.setAvgCostPerMonth(Float.valueOf(getAccumulatedInfo()));
			adata.setTotalMileage(Float.valueOf(totalMileage(pertiCarInfo.getId())));
			adata.setTotalFuel(TotalVolume);
			adata.setAvgFuelCostPerKM(Float.valueOf(avg_fuel_cost_per_km()));
			//-------------------
			expListAdapter=new SimpleExpandableListAdapter(
					getApplicationContext(), 
					createGroupList(), 
					R.layout.group_row, 
					new String[] { "groupTitle" }, 
					new int[] { R.id.childname }, 
					createChildList(), 
					R.layout.child_row, 
					new String[] { "subTitle", "values" }, //
					new int[] { R.id.childname , R.id.rgb } //
					);
			setListAdapter(expListAdapter);
			Log.i(TAG, "expandbale list is filled");
			Log.i(TAG, "isDisplay: "+isDisplay);
			if(adata.getAvgFuelConsumption()>0.00f || adata.getAvgCostPerMonth()>0.00f || adata.getTotalMileage()>0.00f || adata.getTotalFuel()>0.00f || adata.getAvgFuelCostPerKM()>0.00f)
			{
				availFillups=true;
				Log.i(TAG, "------------------------------------------------------");
				FuelTrackerAsync ftAsync=getAsynClassObject();				
				ftAsync.execute(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO,FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST,getApplicationContext(),pertiCarInfo.getId());
				fuelGraphPosition=0;
				spinnerFuelGraphs.setSelection(0);
			}
			else
			{
				availFillups=false;
				if(isDisplay==true && plotGraph.getText().toString().equalsIgnoreCase("Values"))
				{
					
					Log.i(TAG, "CTT: "+ctt);
					Log.i(TAG, "1111111111111111111Before Alert");
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
					alt_bld.setMessage("No fillups available for "+pertiCarInfo.toString())
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface arg0, int arg1) 
						{
							if(plotGraph.getText().toString().equalsIgnoreCase("Graph"))
							{
								
							}
							else
							{
								plotGraph.setText("Graph");
								spinnerFuelGraphs.setVisibility(View.GONE);
								spinnerFuelGraphs.setEnabled(false);
								expandList.setEnabled(true);
								expandList.setVisibility(View.VISIBLE);
								showAnimation();
								isGraph=false;
							}
						}
					});
					AlertDialog alt=alt_bld.create();
					alt.setTitle("Alert");
					alt.setIcon(android.R.drawable.ic_dialog_alert);
					alt.show();
					isDisplay=false;
				}	
				
			}
			if(selectedCarId!=-1)
			{
				//update
				ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
				configurationAdapter.updateConfigurationTable(1, null, null, null, null, pertiCarInfo.getId());
			}
			else
			{
				//insert
				
				ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(getApplicationContext());
				String[] arrayConsume=getResources().getStringArray(R.array.array_consum);
				String[] arrayCurrency=getResources().getStringArray(R.array.array_currency);
				String[] arrayDistance=getResources().getStringArray(R.array.array_dist);
				String[] arrayVolume=getResources().getStringArray(R.array.array_fuel);
				configurationAdapter.insertInConfigurationTable(arrayCurrency[0], arrayVolume[0], arrayDistance[0], arrayConsume[0], pertiCarInfo.getId());
				selectedCarId=pertiCarInfo.getId();
			}
			
		}
		
		
	}
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	to get Accumulated information related to selected car
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:
	 * @Modification	:	
	 * */
	/*private String getAccumulatedInfo()
	{
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Calendar cal = Calendar.getInstance();
	    int day = cal.get(Calendar.DATE);
	    int month = cal.get(Calendar.MONTH) + 1;//8,9,10
	    int year = cal.get(Calendar.YEAR);//2011
	 
	    Cursor cursor=fuelRefillsAdapter.getAccumulatedinfo(pertiCarInfo.getId(), year, month);
	    if(cursor!=null)
	    {
		    int table_index=cursor.getColumnIndex("max(date_of_refill)");
			int table_index2=cursor.getColumnIndex("min(date_of_refill)");
			int table_index3=cursor.getColumnIndex("sum(volume)");
			cursor.moveToFirst();
			Log.i(TAG,"<----- max(date_of_refill)---->"+cursor.getString(table_index));
			Log.i(TAG,"<-----min(date_of_refill)---->"+cursor.getString(table_index2));
			Log.i(TAG,"<-----sum(volume)---->"+cursor.getFloat(table_index3));
			int noOfDays=0;
			if((cursor.getString(table_index)!=null) && (cursor.getString(table_index)!=null))
			{
				noOfDays=getDaysNo(cursor.getString(table_index).toString(),cursor.getString(table_index2).toString());
			}
			else
			{
				noOfDays=1;
			}
			Stats statsAccumlated= Stats.getStatsInstance();
			Log.i(TAG,"getAccumulatedInfo: "+statsAccumlated.getAverageCostPerMonth(noOfDays, TotalVolume));
		    if(String.valueOf(statsAccumlated.getAverageCostPerMonth(noOfDays, TotalVolume)).equalsIgnoreCase("NaN"))
		    {
		    	return "0.0";
			}
		    else
		    {
		    	//return String.valueOf(statsAccumlated.getAverageCostPerMonth(noOfDays, TotalVolume));
		    	return String.format("%.2f",statsAccumlated.getAverageCostPerMonth(noOfDays, TotalVolume));
		    }
	    }
	    else
	    {
	    	return "N/A";
	    }
	}*/
	
	private String getAccumulatedInfo(){
		
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Log.i(TAG,"car_id"+pertiCarInfo.getId());
		Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(pertiCarInfo.getId());
		float total_cost1=0.0f;
		float avg_cost_per_mnth=0.0f;
		int no_of_mnth=0;
		if(cursor!=null)
		{
			no_of_mnth=cursor.getCount();
			Log.i(TAG,"getAccumulatedInfo no of months: "+no_of_mnth);
			int table_index2=cursor.getColumnIndex("totalcost");
			
			while(cursor.moveToNext())
			{
				total_cost1+=cursor.getFloat(table_index2);
				Log.i(TAG,"cursor.getLong(table_index2): "+cursor.getLong(table_index2));
				
			}
			totalCost=total_cost1;
			Log.i(TAG,"getAccumulatedInfo total_cost: "+total_cost1);
			avg_cost_per_mnth=(total_cost1/no_of_mnth);
			if(String.valueOf(avg_cost_per_mnth).equalsIgnoreCase("NaN"))
			{
				return "0.00";
			}
			else
			{
				return String.format("%6.2f",DataConversion.getStatsInstance().roundTwoDecimals(avg_cost_per_mnth));
			}
		}
		else
		{
			return "0.00";
		}
	}
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	to get No of days in between two dates
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:	pass 2 dates
	 * @Modification	:	
	 * */
	private int getDaysNo(String LastDate,String FirstDate)
	{
		int setEndDateYY = 0,setEndDateMM = 0, setEndDateDD = 0, setStartDateYY = 0, setStartDateMM = 0, setStartDateDD = 0;
		String[]str1=LastDate.split(" ");
		String[]strLast =str1[0].split("-");
		setEndDateYY=Integer.parseInt(strLast[0]);
		setEndDateMM=Integer.parseInt(strLast[1]);
		setEndDateDD=Integer.parseInt(strLast[2]);
		
		String[]str2=FirstDate.split(" ");
		String[]strFirst =str2[0].split("-");
		setStartDateYY=Integer.parseInt(strFirst[0]);
		setStartDateMM=Integer.parseInt(strFirst[1]);
		setStartDateDD=Integer.parseInt(strFirst[2]);
		
		int diffyr = Math.abs(setEndDateYY - setStartDateYY);
		int diffmon = Math.abs(setEndDateMM - setStartDateMM);
		int diffdate = Math.abs(setEndDateDD - setStartDateDD);
		Log.i(TAG,"Days: " + diffdate);
		Log.i(TAG,"Months: " + diffmon);
		Log.i(TAG,"Year: " + diffyr);
		int noOfDays = diffdate + (diffmon * 30) + (diffyr * 365);
		Log.i(TAG,"No of Days in two dates: " + noOfDays);
		
		return noOfDays;
	}
	private String avg_fuel_cost_per_km()
	{
		Stats obj1=Stats.getStatsInstance();
		//float tem=obj1.getAverageFuelCostKM(lastReading, firstReading, TotalVolume, totalCost, no_of_Fillups);
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(Analysis.this);
		Cursor cursorFuelCostKM=fuelRefillsAdapter.getStatsOdoDateinfo(pertiCarInfo.getId());
		if(cursorFuelCostKM!=null && cursorFuelCostKM.getCount()>0)
		{
			cursorFuelCostKM.moveToFirst();
			int table_index1=cursorFuelCostKM.getColumnIndex("odometer_reading");
			lastReading=cursorFuelCostKM.getFloat(table_index1);
			Log.i(TAG,"lastReading: "+lastReading);
			
			cursorFuelCostKM.moveToLast();
			int table_index2=cursorFuelCostKM.getColumnIndex("odometer_reading");
			firstReading=cursorFuelCostKM.getFloat(table_index2);
			Log.i(TAG,"firstReading: "+firstReading);
		}
		Log.i(TAG,"totalCost: "+totalCost);
		//float tem=totalCost/(totalCost-firstReading);
		float tem=Stats.getStatsInstance().getAverageCostPerMonth(totalCost, lastReading, firstReading);
		Log.i(TAG,"avg_fuel_cost_per_km: "+tem);
		if((String.valueOf(tem).equalsIgnoreCase("NaN")) || (String.valueOf(tem).equalsIgnoreCase("Infinity")))
		{
			return "0.00";
		}
		else
		{
			return String.format("%6.2f", DataConversion.getStatsInstance().roundTwoDecimals(tem));
		}
		/*Log.i(TAG,"avg_fuel_cost_per_km: "+tem);
		Log.i(TAG,"lastReading: "+lastReading);
		Log.i(TAG,"firstReading: "+firstReading);
		Log.i(TAG,"TotalVolume: "+TotalVolume);
		Log.i(TAG,"totalCost: "+totalCost);
		Log.i(TAG,"no_of_Fillups: "+no_of_Fillups);*/
		/*if(String.valueOf(obj1.getAverageFuelCostKM(lastReading, firstReading, TotalVolume, totalCost, no_of_Fillups)).equalsIgnoreCase("NaN"))
		{
			return "0.0";
		}
		else
		{
			//return String.valueOf(obj1.getAverageFuelCostKM(lastReading, firstReading, TotalVolume, totalCost, no_of_Fillups));
			return String.format("%.2f", obj1.getAverageFuelCostKM(lastReading, firstReading, TotalVolume, totalCost, no_of_Fillups));
		}*/
		
	}
	private String totalMileage(int cID)
	{
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(Analysis.this);
		Cursor mileageCursor=fuelRefillsAdapter.getStatsData(cID);
		float mileage =0;
		if(mileageCursor!=null)
		{
			Log.i(TAG,"mileageCursor.getCount(): "+mileageCursor.getCount());
			
			if(mileageCursor.getCount()>0)
			{
				int no_of_fillup=0;
				float total_mileage=0.0f;
				if ( mileageCursor.getCount() != 0)
				{	
					mileageCursor.moveToFirst();
					
					no_of_fillup=mileageCursor.getInt(mileageCursor.getColumnIndex("count(id)"));
					Log.i(TAG,"count(id)------------"+no_of_fillup );
					
					total_mileage=mileageCursor.getFloat(mileageCursor.getColumnIndex("sum(mileage)"));
					Log.i(TAG,"sum(mileage)------------"+total_mileage );
					
					if(total_mileage>0 && (no_of_fillup-1)>0)
					{
						mileage = total_mileage/(no_of_fillup-1);
					}
					
				}
				Log.i(TAG,"mileageTesting: "+mileage);
				//Math.ceil(convertConsumptionType(mileage, consumtion_in));
				return String.format("%6.2f", DataConversion.getStatsInstance().roundTwoDecimals(convertConsumptionType(mileage, consumtion_in)));
			}
			else
			{
				return "00.00";
			}
		}
		else 
		{
			return "N/A";
		}
	}
	
	
	
	private String getFuelConsumptionPerMonth() {
		// TODO Auto-generated method stub
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.avgFuleConsumpPerMnt(pertiCarInfo.getId());
		float total_volume=0.00f;
		float totalConsum=0.0f;
		int no_of_mnth=0;
		if(cursor!=null)
		{
			no_of_mnth=cursor.getCount();
			int table_index3=cursor.getColumnIndex("totalvolume");
			while(cursor.moveToNext())
			{
				total_volume+=cursor.getLong(table_index3);
			}
			
			//System.out.println("<-----total_volume---->"+total_volume);
			//TotalVolume=(total_volume/no_of_mnth);
			totalConsum=(total_volume/no_of_mnth);
			TotalVolume=(total_volume);
			if(volume_in.contentEquals("gal"))
			{
				TotalVolume=DataConversion.getStatsInstance().getLtrToGln(totalConsum);
			}else if(volume_in.contentEquals("kg")){
				TotalVolume=DataConversion.getStatsInstance().getLtrToKilo(totalConsum);
			}
			
			return String.format("%6.2f", DataConversion.getStatsInstance().roundTwoDecimals(totalConsum));
		}
		else
		{
			return "N/A";
		}
	}
	
	/**
	 * @author			: 	prashant.daware
	 * @date			:	10th aug 2011
	 * @purpose			:	to get Average Fuel Consumption
	 * @ModifiedBy		:	
	 * @ModificationDate:
	 *  @Params			:	pass the car id and we will get avg volume
	 * @Modification	:	
	 * */
	private String avgFuelConsumption()
	{
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(Analysis.this);
		Cursor fuelCursor=fuelRefillsAdapter.getStatsData(pertiCarInfo.getId());
		if(fuelCursor!=null)
		{
			fuelCursor.moveToFirst();
			Log.i(TAG,"fuelCursor count: "+fuelCursor.getCount());
			
			int table_index1=fuelCursor.getColumnIndex("avg(volume)");
			Log.i(TAG,"<-----avg(volume)---->"+fuelCursor.getFloat(table_index1));
			
			int table_index2=fuelCursor.getColumnIndex("sum(volume)");
			Log.i(TAG,"<-----sum(volume)---->"+fuelCursor.getFloat(table_index2));//total_refilled_fuel_volume
			TotalVolume=fuelCursor.getFloat(table_index2);
			if(volume_in.contentEquals("gal")){
				TotalVolume=DataConversion.getStatsInstance().getLtrToGln(TotalVolume);
				
			}else if(volume_in.contentEquals("kg")){
				TotalVolume=DataConversion.getStatsInstance().getLtrToKilo(TotalVolume);
				
			}
			
			int table_index3=fuelCursor.getColumnIndex("count(id)");
			Log.i(TAG,"<-----count(id)---->"+fuelCursor.getInt(table_index3));//total_no_of_fillups
			no_of_Fillups=fuelCursor.getInt(table_index3);
			if(no_of_Fillups>0)
			{
				int table_index4=fuelCursor.getColumnIndex("sum(total_cost)");
				Log.i(TAG,"<-----sum(total_cost)---->"+fuelCursor.getFloat(table_index4));
				totalCost=fuelCursor.getFloat(table_index4);
			}
			else
			{
				totalCost=0.0f;
			}
			//return String.valueOf(fuelCursor.getFloat(table_index1));
			//Math.ceil(convertConsumptionType(fuelCursor.getFloat(table_index1), consumtion_in)); 
			return String.format("%6.2f", DataConversion.getStatsInstance().roundTwoDecimals((convertConsumptionType(fuelCursor.getFloat(table_index1), consumtion_in))));
		}
		else
		{
			return "N/A";
		}
	}
	private void getCarRefillInfo(Cursor refillInfo,int fGraph)
	{
		//Log.i(TAG,"Refill info count: "+refillInfo.getCount());
		dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//Log.i(TAG,"spinnerFuelGraphs.getItemAtPosition(fGraph): "+spinnerFuelGraphs.getItemAtPosition(fGraph));
		graphTypeName=spinnerFuelGraphs.getItemAtPosition(fGraph).toString();
		if(fGraph==0)
		{
			refillDates=new long[refillInfo.getCount()];
			fuelEntered=new float[refillInfo.getCount()];
			graphValues=new String[refillInfo.getCount()];
			getdate=Calendar.getInstance();
			int recCount=0;
			if(refillInfo!=null)
			{
				while(refillInfo!=null && refillInfo.moveToNext())
				{
					String dtrefill=refillInfo.getString(refillInfo.getColumnIndex("date_of_refill"));
					Log.i(TAG,"dtrefill: "+dtrefill);
					String[]dtTime=dtrefill.split(" ");
					/*try {
						Log.i(TAG,"dateFormat.parse(dtrefill): "+dateFormat.parse(dtTime[0]));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					try 
					{
						//getdate.setTime(dateFormat.parse(dtTime[0]));
						getdate.setTime(dateFormat.parse(dtrefill));
					} 
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
					//String[]dt=dtrefill.split(" ");
					refillDates[recCount]=getdate.getTimeInMillis();
					
					float fuelFilled=refillInfo.getFloat(refillInfo.getColumnIndex("volume"));
					fuelEntered[recCount]=fuelFilled;
					graphValues[recCount]=getdate.getTimeInMillis()+":"+fuelFilled;
					recCount++;
				}
				Arrays.sort(graphValues);
			}
			
		}
		else if(fGraph==1)
		{
			float ct=10.0f;
			int recCount=0;
			getdate=Calendar.getInstance();
			FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
			Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(pertiCarInfo.getId());
			if(cursor!=null)
			{
				refillDates=new long[cursor.getCount()];
				fuelEntered=new float[cursor.getCount()];
				graphValues=new String[cursor.getCount()];
				int table_index=cursor.getColumnIndex("monthyear");
				int table_index2=cursor.getColumnIndex("totalcost");
				while(cursor.moveToNext())
				{
					Log.i(TAG,"<-----monthyear---->"+cursor.getString(table_index));
					String dt=cursor.getString(table_index);
					String dtm=dt.substring(0, 2);
					String dty=dt.substring(2, dt.length());
					String pDate=dty+"-"+dtm+"-01 12:00:00";
					Log.i(TAG,"proper date: "+pDate);
					try 
					{
						getdate.setTime(dateFormat.parse(pDate));
					}
					catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					refillDates[recCount]=getdate.getTimeInMillis();
					Log.i(TAG,"refillDates[recCount]:"+recCount+": "+refillDates[recCount]);
					fuelEntered[recCount]=DataConversion.getStatsInstance().roundTwoDecimals(cursor.getFloat(table_index2));
					Log.i(TAG,"fuelEntered[recCount]:"+recCount+": "+fuelEntered[recCount]);
					graphValues[recCount]=getdate.getTimeInMillis()+":"+DataConversion.getStatsInstance().roundTwoDecimals(cursor.getFloat(table_index2));
					Log.i(TAG,"graphValues[recCount]:"+recCount+": "+graphValues[recCount]);
					recCount++;
				}
				/*Arrays.sort(refillDates);
				System.out.println("sorted date: "+Arrays.toString(refillDates));*/
				Arrays.sort(graphValues);
				System.out.println("sorted graph values: "+Arrays.toString(graphValues));
				
			}
			else
			{
				graphValues=new String[1];
				graphValues[0]=0+":"+0;
			}
			
		}
		else if(fGraph==2)
		{
			int recCount=0;
			getdate=Calendar.getInstance();
			FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
			Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(pertiCarInfo.getId());
			if(cursor!=null)
			{
				refillDates=new long[cursor.getCount()];
				fuelEntered=new float[cursor.getCount()];
				graphValues=new String[cursor.getCount()];
				int table_index=cursor.getColumnIndex("monthyear");
				int table_index3=cursor.getColumnIndex("totalmileage");
				while(cursor.moveToNext())
				{
					Log.i(TAG,"<-----monthyear---->"+cursor.getString(table_index));
					String dt=cursor.getString(table_index);
					String dtm=dt.substring(0, 2);
					String dty=dt.substring(2, dt.length());
					String pDate=dty+"-"+dtm+"-01 12:00:00";
					Log.i(TAG,"proper date: "+pDate);
					try 
					{
						getdate.setTime(dateFormat.parse(pDate));
					}
					catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					refillDates[recCount]=getdate.getTimeInMillis();
					Log.i(TAG,"refillDates[recCount]:"+recCount+": "+refillDates[recCount]);
					fuelEntered[recCount]=DataConversion.getStatsInstance().roundTwoDecimals(cursor.getFloat(table_index3));
					Log.i(TAG,"fuelEntered[recCount]:"+recCount+": "+fuelEntered[recCount]);
					graphValues[recCount]=getdate.getTimeInMillis()+":"+DataConversion.getStatsInstance().roundTwoDecimals(cursor.getFloat(table_index3));
					recCount++;
				}
				Arrays.sort(graphValues);
			}
			else
			{
				graphValues=new String[1];
				graphValues[0]=0+":"+0;
			}
		}
		else if(fGraph==3)
		{
			int recCount=0;
			getdate=Calendar.getInstance();
			FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
			Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(pertiCarInfo.getId());
			if(cursor!=null)
			{
				refillDates=new long[cursor.getCount()];
				fuelEntered=new float[cursor.getCount()];
				graphValues=new String[cursor.getCount()];
				int table_index=cursor.getColumnIndex("monthyear");
				int table_index4=cursor.getColumnIndex("totalvolume");
				while(cursor.moveToNext())
				{
					Log.i(TAG,"<-----monthyear---->"+cursor.getString(table_index));
					String dt=cursor.getString(table_index);
					String dtm=dt.substring(0, 2);
					String dty=dt.substring(2, dt.length());
					String pDate=dty+"-"+dtm+"-01 12:00:00";
					Log.i(TAG,"proper date: "+pDate);
					try 
					{
						getdate.setTime(dateFormat.parse(pDate));
					}
					catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					refillDates[recCount]=getdate.getTimeInMillis();
					Log.i(TAG,"refillDates[recCount]:"+recCount+": "+refillDates[recCount]);
					fuelEntered[recCount]=DataConversion.getStatsInstance().roundTwoDecimals(cursor.getFloat(table_index4));
					Log.i(TAG,"fuelEntered[recCount]:"+recCount+": "+fuelEntered[recCount]);
					graphValues[recCount]=getdate.getTimeInMillis()+":"+DataConversion.getStatsInstance().roundTwoDecimals(cursor.getFloat(table_index4));
					recCount++;
				}
				Arrays.sort(graphValues);
			}
			else
			{
				graphValues=new String[1];
				graphValues[0]=0+":"+0;
			}
		}
	}
	
	public void showVehiclesList(Cursor cursor)
	{
		vehicleListAdapter = new ArrayList<VehicleItem>();
		int len=cursor.getCount();
		Log.i(TAG,"Cursor length: "+len);
		strings=null;
		strings=new String[len];
		carID=new int[len];
		int recCount=0;
		while (cursor!=null && cursor.moveToNext()) 
		{
			vi1 = new VehicleItem();	
			vi1.setId(cursor.getInt(cursor.getColumnIndex("id")));
			vi1.setMake(cursor.getString(cursor.getColumnIndex("make")));
			vi1.setModel(cursor.getString(cursor.getColumnIndex("model")));
			vi1.setDistance(cursor.getString(cursor.getColumnIndex("distance_in")));
			vi1.setVolume(cursor.getString(cursor.getColumnIndex("volume_in")));
			vi1.setConsumption(cursor.getString(cursor.getColumnIndex("consumption_in")));
			vi1.setDistCovered("240km");
			vi1.setFillUps("4 Fill-ups");
			vi1.setConsumValue("6.8km/l");
			strings[recCount]=cursor.getString(cursor.getColumnIndex("make"))+" "+cursor.getString(cursor.getColumnIndex("model"));
			carID[recCount]=cursor.getInt(cursor.getColumnIndex("id"));
			vehicleListAdapter.add(vi1);	
			recCount++;
		}
		/*spinnerArrayAdapter = new MyAdapter(Analysis.this, R.layout.spinner_row, strings);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);*/
//		MyAdapter myAdpt=new MyAdapter(context, textViewResourceId, objects)
		Log.i(TAG,"============rec count: "+recCount);
		ArrayAdapter adptCars = null;
		if(recCount==0)
		{
			/*strings=new String[1];
			strings[recCount]="No Cars";
			adptCars=new ArrayAdapter(Analysis.this,R.layout.spinner_item_vehicle,strings);*/
			vi1 = new VehicleItem();
			vi1.setMake("No");
			vi1.setModel("Cars");
			vehicleListAdapter.add(vi1);
			expandList.setEnabled(false);
		}
		adptCars=new ArrayAdapter(Analysis.this,R.layout.spinner_item_vehicle,vehicleListAdapter);
		adptCars.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		spinnerCars.setAdapter(adptCars);
		if(strings.length==0)
		{
			spinnerCars.setEnabled(false);
		}
		else
		{
			spinnerCars.setEnabled(true);
			spinnerCars.setOnItemSelectedListener(Analysis.this);
		}
		//spinnerCars.setAdapter(new MyAdapter(Analysis.this, R.layout.spinner_row, strings));
		
		
		
	}
	ArrayAdapter<VehicleItem> spinnerArrayAdapter;
	public void onHTTPResponseProgress() 
	{
		
	}
	public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
	{
		VehicleItem vitme = null;
		Log.i(TAG,"spinnerCars.getItemIdAtPosition(0): "+position+": "+spinnerCars.getItemIdAtPosition(position));
		Log.i(TAG,"spinnerCars.getItemAtPosition(0): "+position+"~"+spinnerCars.getItemAtPosition(position));
		/*int spinnerCarID=(int) spinnerCars.getItemIdAtPosition(position);
		String spinnerCarName=(String) spinnerCars.getItemAtPosition(position);*/
		Log.i(TAG,"*****************************************************************************************");
		//Log.i(TAG, "frmFillup: "+frmFillup);
		/*if(frmFillup==true)
		{
			Log.i(TAG, "Fillups.fillupCarId: "+Fillups.fillupCarId);
			Log.i(TAG, "Vehicles.RECORD_ID_FORANALYSIS: "+Vehicles.RECORD_ID_FORANALYSIS);
			if(Fillups.fillupCarId != -1)
			{
				spinnerCars.setSelection(Fillups.fillupCarId);
				vitme=(VehicleItem) spinnerCars.getItemAtPosition(Fillups.fillupCarId);
				frmFillup=false;
			}
			else
			{
				spinnerCars.setSelection(Vehicles.RECORD_POSITION_FORANALYSIS);
				vitme=(VehicleItem) spinnerCars.getItemAtPosition(Vehicles.RECORD_POSITION_FORANALYSIS);
			}
		}
		else
		{
			spinnerCars.setSelection(position);
			vitme=(VehicleItem) spinnerCars.getItemAtPosition(position);
		}*/
		Log.i(TAG, "Fillups.frmFillups: "+Fillups.frmFillups);
		Log.i(TAG, "Vehicles.frmAnalaysis: "+Vehicles.frmAnalaysis);
		if(Fillups.frmFillups==true)
		{
			if(Fillups.fillupCarId != -1)
			{
				spinnerCars.setSelection(Fillups.fillupCarId);
				vitme=(VehicleItem) spinnerCars.getItemAtPosition(Fillups.fillupCarId);
				Fillups.frmFillups=false;
			}
		}
		else if(Vehicles.frmAnalaysis==1)
		{
			spinnerCars.setSelection(Vehicles.RECORD_POSITION_FORANALYSIS);
			vitme=(VehicleItem) spinnerCars.getItemAtPosition(Vehicles.RECORD_POSITION_FORANALYSIS);
			Vehicles.frmAnalaysis=0;
		}
		else
		{
			spinnerCars.setSelection(position);
			vitme=(VehicleItem) spinnerCars.getItemAtPosition(position);
		}
		
		if(vitme.toString().equalsIgnoreCase("No Cars"))
		{
			//Toast.makeText(Analysis.this, "No Cars", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(strings.length>0)
			{
				String carInfo=getConfiguration();
				//String carInfo="NA";
				Log.i(TAG,"String CarINfo: "+carInfo);
				if(carInfo.equalsIgnoreCase("NA"))
				{
					Log.i(TAG,"NNNNNNNNAAAAAAAAA");
					validCarInfor=false;
				}
				else
				{
					validCarInfor=true;
					Log.i(TAG,"parent.getCount(): "+parent.getCount());
					String[]cInfo=carInfo.split("~");
					Log.i(TAG, "vitme.getId(): "+vitme.getId());
					if(Integer.parseInt(cInfo[0])!=vitme.getId())
					{
						spinnerPosition=vitme.getId();
						Log.i(TAG,"spinnerPosition in Integer.parseInt(cInfo[0])!=vitme.getId() : "+spinnerPosition);
					}
					else
					{
						if(cInfo[1].length()>0)
						{
							for(int ct=0;ct<parent.getCount();ct++)
							{
								if(spinnerCars.getItemAtPosition(ct).toString().equalsIgnoreCase(cInfo[1]))
								{
									Log.i(TAG, "CT before setselection: "+ct);
									spinnerCars.setSelection(ct);
									spinnerPosition=Integer.parseInt(cInfo[0]);
									analysisSelectedCarId=Integer.parseInt(cInfo[0]);
									Log.i(TAG,"spinnerPosition in Integer.parseInt(cInfo[0]): "+spinnerPosition);
									break;
								}
							}
						}
						
					}
					//Log.i(TAG,"spinnerPosition: "+spinnerPosition);
					
					/*FuelTrackerAsync ftAsync=getAsynClassObject();				
					ftAsync.execute(FuelTrackerQueryCommand.GET_CARS_INFO,FuelTrackerQueryCommand.CARS_TABLE_REQUEST,getApplicationContext(),spinnerPosition);*/
					CarAdapter carAdapter=new CarAdapter(Analysis.this);
					Cursor cursorCarInfo=carAdapter.getCarInfo(spinnerPosition);
					ctt++;
					if(ctt==2)
					{
						isDisplay=true;
						ctt=0;
					}
					showCarInfo(cursorCarInfo);
				}
			}
		}
		
	}
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		
	}
	
	private float convertConsumptionType(float mileage, String consumption_in) {
		// TODO Auto-generated method stub
		
		DataConversion dc=DataConversion.getStatsInstance();
		//float convert_mileage=0.0f;
		if(consumption_in.contentEquals("l/km"))	{
			return dc.getLtr_per_KmFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("mi/l")){
			return dc.getMi_per_ltrFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("l/mi")){
			return dc.getLtr_per_miFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("km/gal")){
			return dc.getKm_per_glnFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("gal/km")){
			return dc.getGln_per_KmFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("mi/gal")){
			return dc.getMi_per_glnFromKm_per_ltr(mileage);
			
		}else if(consumption_in.contentEquals("gal/mi")){
			return dc.getGln_per_miFromKm_per_ltr(mileage);
			
		}
		return  mileage;
	}
	/*public boolean onTouch(View view, MotionEvent mevent) 
	{
		int evt=mevent.getAction();
		if(evt==MotionEvent.ACTION_DOWN)
		{
			plotGraph.setText("Graph");
			spinnerCars.setEnabled(true);
			spinnerFuelGraphs.setEnabled(false);
			showAnimation();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			buttonLayout.setVisibility(View.VISIBLE);
		}
		return false;
	}*/
	private void addViewToDialogBox()
    {
    	AlertDialog.Builder alert=new AlertDialog.Builder(this);
    	final LinearLayout lin=new LinearLayout(this);
    	lin.setOrientation(getResources().getConfiguration().orientation);
    	Display dd=getWindowManager().getDefaultDisplay();
    	lin.addView(gView, new LayoutParams(dd.getWidth(), dd.getHeight()));
    	alert.setView(lin);
    	alert.show();
    }
	public void onClick(View arg0) 
	{
		
		ExpandableListAdapter ela = getExpandableListAdapter();
		ExpandableListView elv = getExpandableListView();
		Drawable dAdd=getResources().getDrawable(R.drawable.delete_btn);
		Drawable dMinus=getResources().getDrawable(R.drawable.add_btn);
		int count = ela.getGroupCount();
		if(expanded==false)
		{
			//expandList.setCompoundDrawablesWithIntrinsicBounds(dAdd, null, null, null);
			//expandList.setBackgroundResource(R.drawable.add_btn);
			//expandList.setBackgroundDrawable(dAdd);
			expandList.setImageDrawable(dAdd);
			for (int i = 0; i <count ; i++)
			{
				elv.expandGroup(i);
			}
			expanded=true;
		}
		else if(expanded==true)
		{
			//expandList.setCompoundDrawablesWithIntrinsicBounds(dMinus, null, null, null);
			//expandList.setBackgroundDrawable(dMinus);
			//expandList.setBackgroundResource(R.drawable.delete_btn);
			expandList.setImageDrawable(dMinus);
			for (int i = 0; i <count ; i++)
			{
				elv.collapseGroup(i);
			}	
			expanded=false;
		}
		
	}
}
