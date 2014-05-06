package  com.fueltracker.asynctask;



import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fueltracker.FT_DB.CarAdapter;
import com.fueltracker.FT_DB.ConfigurationAdapter;
import com.fueltracker.FT_DB.FuelRefillsAdapter;

/**
 * @author : ajay.sahani
 * @date : 10th Aug  2011
 * @purpose : to handle all request from this class only
 * @ModifiedBy : 
 * @ModificationDate: 
 * @Modification : 
 * */
public class FuelTrackerAsync extends AsyncTask<Object, Object, Object>{

	private HTTPResponseListener listener = null;
	private FuelTrackerProgressBar jPBar=null;
	private String request_type=null;
	private String request_for=null;
	

	public FuelTrackerAsync(HTTPResponseListener listener,FuelTrackerProgressBar jPBar)
	{
		this.listener = listener;
		this.jPBar=jPBar;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
	}
	
	@Override
	protected Object doInBackground(Object... data) {
		// TODO Auto-generated method stub
		
		try{
			request_type=(String)data[0];
			request_for=(String)data[1];
			
			if(request_for.contentEquals(FuelTrackerQueryCommand.CARS_TABLE_REQUEST)){
				return processCarTableRequest(request_type,data);
				
			}else if(request_for.contentEquals(FuelTrackerQueryCommand.FUEL_REFILLS_TABLE_REQUEST)){
				return processFuelRefillsTableRequest(request_type,data);
				
			}else if(request_for.contentEquals(FuelTrackerQueryCommand.CONFIGURATION_TABLE_REQUEST)){
				return processConfigurationTableRequest(request_type,data);
			}
			
		
		}catch(Exception error){
			error.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to process configuration request
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:
	 * @Params			:to know abt parameter check configurationAdapter class's insertInConfigurationTable() method's argument's name
	 * */
	private Object processConfigurationTableRequest(String request_type,Object[] data) {
		// TODO Auto-generated method stub
		Context context=(Context)data[2];
		ConfigurationAdapter configurationAdapter=new ConfigurationAdapter(context);
		
		if (request_type.contentEquals(FuelTrackerQueryCommand.INSERT_IN_CONFIGURATION_TABLE)){
			return configurationAdapter.insertInConfigurationTable((String)data[3], (String)data[4],(String) data[5],(String) data[6],(Integer)data[7]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.UPDATE_CONFIGURATION_TABLE)){
			return configurationAdapter.updateConfigurationTable((Integer)data[3],(String)data[4], (String)data[5],(String) data[6],(String) data[7],(Integer)data[8]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.DELETE_CONFIGURATION_ACCOUNT)){
			return configurationAdapter.deleteConfigurationAccount((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_CONFIGURATION_INFO)){
			return configurationAdapter.getConfigurationInfo((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_ALL_CONFIGURATION_INFO)){
			return configurationAdapter.getAllConfigurationInfo();
			
		}
		return null;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to process fuel refill request
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:
	 * @Params			:to know abt parameter check FuelRefillAdapter class's insertInFuelRefillsTable() method's argument's name
	 * */
	private Object processFuelRefillsTableRequest(String request_type,Object[] data) {			
		// TODO Auto-generated method stub
		Context context=(Context)data[2];
		FuelRefillsAdapter fuelRefillsAdapter=new FuelRefillsAdapter(context);
		
		if (request_type.contentEquals(FuelTrackerQueryCommand.INSERT_IN_FUEL_REFILS_TABLE)){
			return fuelRefillsAdapter.insertInFuelRefillsTable((String)data[3],(Float)data[4], (Float)data[5], (String)data[6], (Float)data[7], (String)data[8], (Float)data[9],(Float)data[10], (Integer)data[11],(Float)data[12], (String)data[13]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.UPDATE_FUEL_REFILS_TABLE)){
			return fuelRefillsAdapter.updateFuelRefillsTable((Integer)data[3],(String)data[4],(Float)data[5], (Float)data[6], (String)data[7], (Float)data[8], (String)data[9], (Float)data[10], (Float)data[11], (Integer)data[12],(Float)data[13], (String)data[14]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.DELETE_FUEL_REFILS_ACCOUNT)){
			return fuelRefillsAdapter.deleteFuelRefillsAccount((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO)){//using car id
			return fuelRefillsAdapter.getFuelRefillsInfo((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_INFO_USINGFUELID)){
			return fuelRefillsAdapter.getFuelRefillsInfo((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_ALL_FUEL_REFILS_INFO)){
			return fuelRefillsAdapter.getAllFuelRefillsInfo();
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_LATEST_ODOMETER_READING)){
			return fuelRefillsAdapter.getLatestOdometerReading();
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_USINGDATE)){// getStatsOdoDateinfo
			//return fuelRefillsAdapter.getFuelRefillUsingDate((String)data[3],(Integer)data[4],(Integer)data[5]);
			return fuelRefillsAdapter.getFuelRefillUsingDate((String)data[3],(Integer)data[4]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_STATS_VOLUME_DATA)){
			return fuelRefillsAdapter.getStatsData((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_STATS_ODO_DATE_DATA)){
			return fuelRefillsAdapter.getStatsOdoDateinfo((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_FUEL_REFILS_ACCUMULATED_INFO)){
			return fuelRefillsAdapter.getAccumulatedinfo((Integer)data[3], (Integer)data[4],(Integer)data[5]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.DEL_FULE_REFIL_USING_CAR_ID)){
			return fuelRefillsAdapter.delFulRefilUsingCarID((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.DEL_ALL_FULE_REFILL)){
			return fuelRefillsAdapter.delAllFuleRefill();
			
		}
		return null;
	}
	
	/**
	 * @author			: 	ajay.sahani
	 * @date			:	10th aug 2011
	 * @purpose			:	to process configuration request
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:
	 * @Params			:to know abt parameter check carAdapter class's insertInCarTable() method's argument's name
	 * */
	
	private Object processCarTableRequest(String request_type,Object... data) {
		// TODO Auto-generated method stub
		Context context=(Context)data[2];
		CarAdapter carAdapter=new CarAdapter(context);
		
		if (request_type.contentEquals(FuelTrackerQueryCommand.INSERT_IN_CARS_TABLE)){
			return carAdapter.insertInCarsTable((String)data[3], (String)data[4],(String) data[5],(String) data[6],(String) data[7],(String) data[8],(Float)data[9]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.UPDATE_CARS_TABLE)){
			return carAdapter.updateCarsTable((Integer)data[3],(String)data[4], (String)data[5],(String) data[6],(String) data[7],(String) data[8],(String) data[9],(Float)data[10]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.DELETE_CARS_ACCOUNT)){
			return carAdapter.deleteCarAccount((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_CARS_INFO)){
			return carAdapter.getCarInfo((Integer)data[3]);
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.GET_ALL_CARS_INFO)){
			return carAdapter.getAllCarInfo();
			
		}else if(request_type.contentEquals(FuelTrackerQueryCommand.DELETE_ALL_CAR)){
			return carAdapter.deleteAllCar();
			
		}
		return null;
	}
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		listener.onHTTPResponseComplete(result,request_type);
		if(jPBar !=null)
			jPBar.cancelDialog();
	}

}
