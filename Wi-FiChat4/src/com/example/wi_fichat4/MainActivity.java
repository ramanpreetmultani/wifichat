package com.example.wi_fichat4;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button submit,aboutus1;
    String group,name;
	EditText groupname,username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		submit = (Button) findViewById(R.id.submit);
		aboutus1 = (Button) findViewById(R.id.aboutus);
		groupname = (EditText) findViewById(R.id.group);
		username = (EditText) findViewById(R.id.name);
	
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				group = groupname.getText().toString();
				name = username.getText().toString();
				
				CheckNtwkConn cd = new CheckNtwkConn();
	        	 
	        	Boolean isInternetPresent = cd.CheckInternet(MainActivity.this);
	        	
	        	if(isInternetPresent)
	        	{
	        		if(!group.matches("")&&!name.matches(""))
	        		{
	        			Intent i=new Intent(MainActivity.this, HelloChord.class);
	        			i.putExtra("name",name);
	        			i.putExtra("groupname", group);
	        			
	        			startActivity(i);
	        		}
	        		else 
	        		{
	        			Toast.makeText(MainActivity.this, "Enter details first", Toast.LENGTH_SHORT).show();
	        		}
	        	}
	        	else
	        	{
	        	    Toast.makeText(MainActivity.this, "Connect to Wifi", Toast.LENGTH_SHORT).show();
	        	}
			}
		});
		
		
		aboutus1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this, AboutUs.class);
				startActivity(i);
			}
		});
	}

}
