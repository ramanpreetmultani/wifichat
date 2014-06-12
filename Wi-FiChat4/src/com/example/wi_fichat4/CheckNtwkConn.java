package com.example.wi_fichat4;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckNtwkConn
{
	public boolean CheckInternet(Context context) 
	{
	    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    
	  //android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	
	    // Here if condition check for wifi and mobile network is available or not.
	    // If anyone of them is available or connected then it will return true, otherwise false;
	
	    if (wifi.isConnected()) {
	        return true;
	    } 
	    
	    return false;
	}
}