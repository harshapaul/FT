package  com.fueltracker.asynctask;



import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;


public class FuelTrackerProgressBar{

	/**
	 * @author			: 	ajay.sahani
	 * @date			:	02nd may 2011
	 * @purpose			:	to show loading screen when ever data fetch
	 * @ModifiedBy		:	
	 * @ModificationDate:	
	 * @Modification	:	
	 * */

	ProgressDialog dialog=null ;
	private String msg=null;
	
	public FuelTrackerProgressBar(Context context,String msg){
		
		this.msg=msg;
		 dialog = new ProgressDialog(context);
        
		 showDialog();
		 
		 if(dialog!=null){
		 dialog.show();
		 }
	}
	
	public ProgressDialog showDialog(){
		   dialog.setMessage(msg);
		   dialog.setIndeterminate(true);
		   dialog.setCancelable(true);
		   return dialog;
	}
	
	public void cancelDialog(){
		dialog.cancel();
	}

}

