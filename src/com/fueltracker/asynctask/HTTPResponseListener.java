package  com.fueltracker.asynctask;;


public interface HTTPResponseListener 
{
	
	public void onHTTPResponseComplete(Object data, String request);

	
	public void onHTTPResponseProgress();
}
