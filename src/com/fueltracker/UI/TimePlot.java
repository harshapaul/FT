package com.fueltracker.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class TimePlot extends AbstractDemoChart 
{
	private XYSeriesRenderer r;
	private int gMax,gMin;
	//private long[] dateArray={1009843200000L,1041379200000L,1072915200000L,1104537600000L};
	public GraphicalView execute(Context context) 
	{
		//return ChartFactory.getTimeChartIntent(context, getDateDemoDataset(), getDemoRenderer(), null);
		return ChartFactory.getTimeChartView(context, getDateDemoDataset(), getDemoRenderer(), null);
	}

	public String getDesc() 
	{
		// TODO Auto-generated method stub
		return "Time Chart demo using ChartEngine";
	}

	public String getName() 
	{
		// TODO Auto-generated method stub
		return "Time Chart";
	}
	
	private XYMultipleSeriesRenderer getDemoRenderer() 
	{
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(12);
	    renderer.setChartTitleTextSize(15);
	    renderer.setLabelsTextSize(12);
	    renderer.setLegendTextSize(13);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.rgb(239, 239, 239));
	    renderer.setPointSize(3f);
	    renderer.setXTitle("Date");
	    renderer.setYTitle("Consumption");
	    renderer.setChartTitle(Analysis.graphTypeName);
	    renderer.setDisplayChartValues(true);
	    renderer.setZoomEnabled(false, false);
	    renderer.setMargins(new int[] {30, 30, 25, 0});
	    renderer.setMarginsColor(Color.rgb(239, 239, 239));
	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setShowGrid(true);
	    /*for(int i=0;i<3;i++)
	    {
	    	int red=(int)(Math.random()*255);
            int green=(int)(Math.random()*255);
            int blue=(int)(Math.random()*255);
            r=new XYSeriesRenderer();
            r.setColor(Color.rgb(red, green, blue));
            setrendererProp(i);
            renderer.addSeriesRenderer(r);
        }*/
	    for(int i=0;i<3;i++)
	    {
	    	r=new XYSeriesRenderer();
            if(i==0)
            {
            	r.setColor(Color.rgb(51, 153, 204));
            }
            else if(i==1)
            {
            	r.setColor(Color.rgb(51, 153, 0));
            }
            else if(i==2)
            {
            	r.setColor(Color.rgb(204, 0, 0));
            }
            setrendererProp(i);
            renderer.addSeriesRenderer(r);
        }
	    renderer.setAxesColor(Color.rgb(56, 81, 112));
	    renderer.setLabelsColor(Color.rgb(56, 81, 112));
	    renderer.setXLabelsAngle(45);
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
	
	private XYMultipleSeriesDataset getDateDemoDataset() 
	{
	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    //final int nr = Analysis.refillDates.length;//commented bcos new array is used
	    final int nr=Analysis.graphValues.length;
	    
	    //long value = new Date().getTime() - 3 * TimeChart.DAY;
	    //System.out.println("TimePlot values: "+value);
	    //Random r = new Random();
	    for (int i = 0; i < 3; i++) 
	    {
	      if(i==0)
	      {
	    	  TimeSeries series1 = new TimeSeries("Consumption");
		      for (int k = 0; k < nr; k++) 
		      {
		        //series.add(new Date(value + k * TimeChart.DAY / 4), 20 + r.nextInt() % 100);
		    	  String str[]=Analysis.graphValues[k].split(":");
		    	  series1.add(new Date(Long.valueOf(str[0])), Double.valueOf(str[1]));
		    	  //series1.add(new Date(Analysis.refillDates[k]),Analysis.fuelEntered[k]);
		    	  //System.out.println("Date(Analysis.refillDates[k])"+new Date(Analysis.refillDates[k]));
		      }
		      dataset.addSeries(series1);
	      }
	      else if(i==1)
	      {
	    	  TimeSeries series2 = new TimeSeries("Best");
	    	  for(int k=0;k < nr; k++)
	    	  {
	    		  //series2.add(new Date(Analysis.refillDates[k]), 50);
	    		  String str[]=Analysis.graphValues[k].split(":");
	    		  series2.add(new Date(Long.valueOf(str[0])),100);
	    	  }
	    	  dataset.addSeries(series2);
	      }
	      else if(i==2)
	      {
	    	  TimeSeries series3 = new TimeSeries("Worst");
	    	  for(int k=0;k < nr; k++)
	    	  {
	    		  //series3.add(new Date(Analysis.refillDates[k]), 10);
	    		  String str[]=Analysis.graphValues[k].split(":");
	    		  series3.add(new Date(Long.valueOf(str[0])),10);
	    	  }
	    	  dataset.addSeries(series3);
	      }
	    }
	    
	    return dataset;
	}

}
