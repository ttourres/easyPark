package com.android.myfirstapp;

import java.util.ArrayList;

import com.android.tools.ParkingDBAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GPS extends Activity
{
	// Boutons
	private Button b_oui;
	private Button b_non;
	
	// Listener sur les bouton
	private OnClickListener _bListenerOui = new OnClickListener() 
	{
			public void onClick(View v)
			{
				// On affiche les parametres 
				Intent intentSet = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intentSet);
				
				finish();
			}
	};
	
	private OnClickListener _bListenerNon = new OnClickListener() 
	{
			public void onClick(View v)
			{
				finish();
				// Retour
				
				/*Intent intent;
				intent = new Intent(GPS.this, SearchMenu.class);
				startActivity(intent);*/
			}
	};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_menu);
        
        // Initialisation des boutons
        b_oui= (Button) findViewById(R.id.b_oui);
        b_oui.setOnClickListener(_bListenerOui);
        
        b_non = (Button) findViewById(R.id.b_non);
        b_non.setOnClickListener(_bListenerNon);
        
    } // onCreate(Bundle savedInstanceState)
    
} // class MyFirstApp