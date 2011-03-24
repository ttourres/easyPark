package com.android.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InfoParking extends Activity{
	
	private Button b_goto;
	private Button b_back;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.info_parking);
		
		Intent i = getIntent();
		
		Resources r = getResources();
		String infos = r.getString(R.string.info_adress) + " : " + i.getStringExtra("adress") + "\n";
		infos += r.getString(R.string.info_availibilty) + " : " + i.getIntExtra("availabity", 0) + "\n";
		infos += r.getString(R.string.info_capacity) + " : " + i.getIntExtra("capacity", 0) + "\n";
		
		TextView txtViewNameP = (TextView) findViewById(R.id.info_name);
		TextView txtViewInfo = (TextView) findViewById(R.id.info);
		txtViewNameP.setText(i.getStringExtra("name"));
		txtViewInfo.setText(infos);
		
		
		
		/*
		b_goto = (Button) findViewById(R.id.b_goto);
	    b_goto.setOnClickListener(bListenerGoto);
	    */
	    
	    b_back = (Button) findViewById(R.id.b_back);
	    b_back.setOnClickListener(bListenerBack);
		
	}
	
	private OnClickListener bListenerGoto = new OnClickListener() 
	{
			public void onClick(View v)
			{
				/*
				Intent intent;
				intent = new Intent(InfoParking.this, Map.class);
				startActivity(intent);
				*/
			}
	};
	
	private OnClickListener bListenerBack = new OnClickListener() 
	{
			public void onClick(View v)
			{
				finish();
			}
	};
			

}
