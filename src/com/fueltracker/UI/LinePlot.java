package com.fueltracker.UI;

import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

public class LinePlot extends AbstractDemoChart
{
	private XYSeriesRenderer r;
	final String TAG = getClass().getName();
	public GraphicalView execute(Context context) 
	{
		return ChartFactory.getLineChartView(context, getDemoDataset(), getDemoRenderer());
	}

	public String getDesc() 
	{
		// TODO Auto-generated method stub
		return "Line Chart using ChartEngine";
	}

	public String getName() 
	{
		// TODO Auto-generated method stub
		return "Line Chart";
	}
	private XYMultipleSeriesDataset getDemoDataset() 
	{
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    //final int nr = 6;
	    int nr=Analysis.refillDates.length;
	    System.out.println("Analysis.refillDates.length: "+nr);
	    //int ct=0;
	    int ct=Analysis.fuelEntered.length;
	    Random r = new Random();
	    for (int i = 0; i < 3; i++) 
	    {
	      if(i==0)
	      {
	    	  XYSeries series1 = new XYSeries("Consumption");
	    	  for (int k = 0; k < nr; k++) 
		      {
		        try
		        {
					System.out.println("Analysis.fuelEntered: "+"k: "+k+"    "+Analysis.fuelEntered[k]);
					System.out.println("Analysis.refillDates: "+"k: "+k+"    "+Analysis.refillDates[k]);
		        	series1.add(k, Analysis.fuelEntered[k]);
		        }
		        catch (NumberFormatException e) 
		        {
					e.printStackTrace();
				}
		      }
	    	  dataset.addSeries(series1);
	      }
	      else if(i==1)
	      {
	    	  XYSeries series2 = new XYSeries("Best");
	    	  for(int k=0;k < nr; k++)
	    	  {
	    		  series2.add(k, 50);
	    	  }
	    	  dataset.addSeries(series2);
	      }
	      else if(i==2)
	      {
	    	  XYSeries series3 = new XYSeries("Wrost");
	    	  for(int k=0;k < nr; k++)
	    	  {
	    		  series3.add(k, 10);
	    	  }
	    	  dataset.addSeries(series3);
	      }
	      
	    }
	    return dataset;
	}
	private XYMultipleSeriesRenderer getDemoRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(12);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setMargins(new int[] {20, 30, 15, 0});
	    renderer.setXTitle("Date");
	    renderer.setYTitle("Consumption");
	    /*r = new XYSeriesRenderer();
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.SQUARE);
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(Color.WHITE);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);*/
	    /*r = new XYSeriesRenderer();
	    r.setPointStyle(PointStyle.CIRCLE);
	    r.setColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);*/
	    for(int i=0;i<3;i++)
	    {
	    	/*int red=(int)(Math.random()*255);
            int green=(int)(Math.random()*255);
            int blue=(int)(Math.random()*255);*/
            r=new XYSeriesRenderer();
            //r.setColor(Color.rgb(red, green, blue));
            if(i==0)
            {
            	r.setColor(Color.YELLOW);
            }
            else if(i==1)
            {
            	r.setColor(Color.GREEN);
            }
            else if(i==2)
            {
            	r.setColor(Color.RED);
            }
            setrendererProp(i);
            renderer.addSeriesRenderer(r);
        }
	    renderer.setAxesColor(Color.DKGRAY);
	    renderer.setLabelsColor(Color.LTGRAY);
	    return renderer;
	  }
	
	private void setrendererProp(int n)
	{
		if(n==0)
		{
			r.setPointStyle(PointStyle.DIAMOND);
		    //r.setFillBelowLine(true);
		    //r.setFillBelowLineColor(Color.WHITE);
		    r.setFillPoints(true);
		}
		else if(n==1)
		{
			//r.setPointStyle(PointStyle.CIRCLE);
			r.setFillPoints(true);
		}
		else if(n==2)
		{
			//r.setPointStyle(PointStyle.CIRCLE);
			r.setFillPoints(true);
		}
	}
}
