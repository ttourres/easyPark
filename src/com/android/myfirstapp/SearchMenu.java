package com.android.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SearchMenu extends Activity
{
	// debug
	//Log log;
	
	// Boutons
	private Button _b3;
	private Button _b4;
	// LocationManager
	private LocationManager _lm;
	private String locationProvider = LocationManager.GPS_PROVIDER;
	// Listener sur le bouton
	private OnClickListener _bListener3 = new OnClickListener() 
	{
			public void onClick(View v)
			{
				
				if (!_lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
				{
					Intent intentGPS = new Intent(SearchMenu.this, GPS.class);
					startActivity(intentGPS); 
				}
				else
				{
					
					//_lm.getGpsStatus(null);
					Location loc;
					loc = _lm.getLastKnownLocation(locationProvider);
				
					if(loc == null)
					{
						
						Toast.makeText(SearchMenu.this, "Coordonnees GPS introuvables", Toast.LENGTH_SHORT).show();
						loc = SearchMenu.this._lm.getLastKnownLocation("network");
					}
					else
					{		
						//Toast.makeText(SearchMenu.this, "La position", Toast.LENGTH_SHORT).show();
						//Toast.makeText(SearchMenu.this, loc.getLatitude() + ";" + loc.getLongitude(), Toast.LENGTH_SHORT).show();
						Intent intent;
						intent = new Intent(SearchMenu.this, Map.class);
						Bundle bundle = new Bundle();
						bundle.putDouble("LATITUDE", loc.getLatitude());
						bundle.putDouble("LONGITUDE", loc.getLongitude());
						bundle.putInt("RAYON", 16);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			}
	};
	
	// Listener sur le bouton
	private OnClickListener _bListener4 = new OnClickListener() 
	{
			public void onClick(View v)
			{
				Intent intent;
				intent = new Intent(SearchMenu.this, SearchAdress.class);
				startActivity(intent);
			}
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_menu);
        
        // Initialisation du LocalManager
        _lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _lm.requestLocationUpdates(locationProvider, 0, 0, new MyLocationListener());
        
        
        // Initialisation des boutons
        _b3 = (Button) findViewById(R.id.b3);
        _b3.setOnClickListener(_bListener3);
        
        _b4 = (Button) findViewById(R.id.b4);
        _b4.setOnClickListener(_bListener4);
        
        
    } // onCreate(Bundle savedInstanceState)
    
} // class Activity1