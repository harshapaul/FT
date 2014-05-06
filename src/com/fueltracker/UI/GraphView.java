package com.fueltracker.UI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fueltracker.FT_DB.FuelRefillsAdapter;

public class GraphView extends Activity implements OnClickListener 
{
	
	private GraphicalView gView=null;
	//private LinearLayout viewLayout;
	private FrameLayout frLayout;
	private Button btn;
	public static int screenOrient;
	private String[] fuelString={"Fuel consumption","Accumulated","Total Mileage","Total Fuel"};
	private FuelRefillsAdapter fuelRefillsAdapter;
	private Calendar getdate;
	private DateFormat dateFormat ;
	//for menu
	public static final int ANALYSIS = Menu.FIRST;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, ANALYSIS, 0, "Fuel consumption");
        return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		switch(item.getItemId())
		{
		case ANALYSIS: 
			getAlertView();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.graphview);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		viewLayout=(FrameLayout) findViewById(R.id.viewLayout);
		btn=(Button) findViewById(R.id.moewGraph);
		btn.setOnClickListener(this);
 		viewLayout.removeViewInLayout(gView);
		gView=null;
		gView=new TimePlot().execute(getApplicationContext());
		
		viewLayout.addView(gView);
		//viewLayout.addView(btn);
		setResult(RESULT_OK);*/
		
		fuelRefillsAdapter=new FuelRefillsAdapter(GraphView.this);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//for dynamically layout construction
		gView=null;
		gView=new TimePlot().execute(getApplicationContext());
		
		/*btn=new Button(GraphView.this);
		btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		btn.setGravity(Gravity.BOTTOM);
		btn.setText("=>>");
		btn.setOnClickListener(this);*/
		frLayout=new FrameLayout(GraphView.this);
		frLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		frLayout.addView(gView);
		//frLayout.addView(btn);
		setContentView(frLayout);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setResult(RESULT_OK);
	}
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		setResult(RESULT_OK);
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		screenOrient=newConfig.orientation;
		if(newConfig.orientation==getResources().getConfiguration().ORIENTATION_PORTRAIT)
		{
			onBackPressed();
			onDestroy();
		}
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		finishActivity(RESULT_OK);
	}
	private void getAlertView()
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(GraphView.this);
		builder.setTitle("Select one graph");
		builder.setItems(fuelString, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int position) 
			{
				Toast.makeText(getApplicationContext(), fuelString[position], Toast.LENGTH_SHORT).show();
				getCarRefillInfo(position);
			}
		});
		AlertDialog alt=builder.create();
		alt.show();
	}
	public void onClick(View arg0) 
	{
		getAlertView();
	}
	
	private void getCarRefillInfo(int pos)
	{
		switch(pos)
		{
		case 0:
			forConsumption();
			break;
		case 1:
			forAccumulated();
			break;
		case 2:
			forTotalMilage();
			break;
		case 3:
			forTotalfuel(); 
			break;
		}
		frLayout.removeViewInLayout(gView);
		gView=null;
		gView=new TimePlot().execute(getApplicationContext());
		//gView.setBackgroundResource(R.drawable.activity_background);
		frLayout.addView(gView);
	}
	private void forConsumption()
	{
		Cursor cursorConsum=fuelRefillsAdapter.getFuelRefillsInfo(Analysis.analysisSelectedCarId);
		Analysis.refillDates=new long[cursorConsum.getCount()];
		Analysis.fuelEntered=new float[cursorConsum.getCount()];
		getdate=Calendar.getInstance();
		int recCount=0;
		if(cursorConsum!=null)
		{
			while(cursorConsum!=null && cursorConsum.moveToNext())
			{
				String dtrefill=cursorConsum.getString(cursorConsum.getColumnIndex("date_of_refill"));
				//System.out.println("dtrefill: "+dtrefill);
				String[]dtTime=dtrefill.split(" ");
				try {
					System.out.println("dateFormat.parse(dtrefill): "+dateFormat.parse(dtTime[0]));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try 
				{
					getdate.setTime(dateFormat.parse(dtTime[0]));
				} 
				catch (ParseException e) 
				{
					e.printStackTrace();
				}
				//String[]dt=dtrefill.split(" ");
				Analysis.refillDates[recCount]=getdate.getTimeInMillis();
				
				float fuelFilled=cursorConsum.getFloat(cursorConsum.getColumnIndex("volume"));
				Analysis.fuelEntered[recCount]=fuelFilled;
				recCount++;
			}
		}
	}
	private void forAccumulated()
	{
		int recCount=0;
		getdate=Calendar.getInstance();
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(Analysis.analysisSelectedCarId);
		if(cursor!=null)
		{
			Analysis.refillDates=new long[cursor.getCount()];
			Analysis.fuelEntered=new float[cursor.getCount()];
			int table_index=cursor.getColumnIndex("monthyear");
			int table_index2=cursor.getColumnIndex("totalcost");
			while(cursor.moveToNext())
			{
				System.out.println("<-----monthyear---->"+cursor.getString(table_index));
				String dt=cursor.getString(table_index);
				String dtm=dt.substring(0, 2);
				String dty=dt.substring(2, dt.length());
				String pDate=dty+"-"+dtm+"-01";
				System.out.println("proper date: "+pDate);
				try 
				{
					getdate.setTime(dateFormat.parse(pDate));
				}
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Analysis.refillDates[recCount]=getdate.getTimeInMillis();
				System.out.println("refillDates[recCount]:"+recCount+": "+Analysis.refillDates[recCount]);
				Analysis.fuelEntered[recCount]=cursor.getFloat(table_index2);
				System.out.println("fuelEntered[recCount]:"+recCount+": "+Analysis.fuelEntered[recCount]);
				recCount++;
			}
		}
	}
	private void forTotalMilage()
	{
		int recCount=0;
		getdate=Calendar.getInstance();
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(Analysis.analysisSelectedCarId);
		if(cursor!=null)
		{
			Analysis.refillDates=new long[cursor.getCount()];
			Analysis.fuelEntered=new float[cursor.getCount()];
			int table_index=cursor.getColumnIndex("monthyear");
			int table_index3=cursor.getColumnIndex("totalmileage");
			while(cursor.moveToNext())
			{
				System.out.println("<-----monthyear---->"+cursor.getString(table_index));
				String dt=cursor.getString(table_index);
				String dtm=dt.substring(0, 2);
				String dty=dt.substring(2, dt.length());
				String pDate=dty+"-"+dtm+"-01";
				System.out.println("proper date: "+pDate);
				try 
				{
					getdate.setTime(dateFormat.parse(pDate));
				}
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Analysis.refillDates[recCount]=getdate.getTimeInMillis();
				System.out.println("refillDates[recCount]:"+recCount+": "+Analysis.refillDates[recCount]);
				Analysis.fuelEntered[recCount]=(float) cursor.getFloat(table_index3);
				System.out.println("fuelEntered[recCount]:"+recCount+": "+Analysis.fuelEntered[recCount]);
				recCount++;
			}
		}
	}
	private void forTotalfuel()
	{
		int recCount=0;
		getdate=Calendar.getInstance();
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(getApplicationContext());
		Cursor cursor=fuelRefillsAdapter.getAvgCostPermonth(Analysis.analysisSelectedCarId);
		if(cursor!=null)
		{
			Analysis.refillDates=new long[cursor.getCount()];
			Analysis.fuelEntered=new float[cursor.getCount()];
			int table_index=cursor.getColumnIndex("monthyear");
			int table_index4=cursor.getColumnIndex("totalvolume");
			while(cursor.moveToNext())
			{
				System.out.println("<-----monthyear---->"+cursor.getString(table_index));
				String dt=cursor.getString(table_index);
				String dtm=dt.substring(0, 2);
				String dty=dt.substring(2, dt.length());
				String pDate=dty+"-"+dtm+"-01";
				System.out.println("proper date: "+pDate);
				try 
				{
					getdate.setTime(dateFormat.parse(pDate));
				}
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Analysis.refillDates[recCount]=getdate.getTimeInMillis();
				//System.out.println("refillDates[recCount]:"+recCount+": "+Analysis.refillDates[recCount]);
				Analysis.fuelEntered[recCount]=cursor.getFloat(table_index4);
				//System.out.println("fuelEntered[recCount]:"+recCount+": "+Analysis.fuelEntered[recCount]);
				recCount++;
			}
		}
	}
}
