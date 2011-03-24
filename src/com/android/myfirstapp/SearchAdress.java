package com.android.myfirstapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.android.tools.Test;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchAdress extends Activity
{
Log log;
	
	// Boutons
	private Button _bCheck;
	
	// Listener sur le bouton
	private OnClickListener _bListenerbCheck = new OnClickListener() 
	{
			public void onClick(View v)
			{
				
				EditText editTextAdress = (EditText) findViewById(R.id.getAddress);
				String Myaddress = editTextAdress.getText().toString() + " Marseille";
				Address address = null;
				
				try{
					Geocoder geocoder = new Geocoder(getBaseContext(), Locale.FRANCE);
					List<Address> listAdd = geocoder.getFromLocationName(Myaddress, 1);
					address = listAdd.get(0);
					
					if (address != null)
					{
						//Toast.makeText(SearchAdress.this, address.getLatitude() + ";" + address.getLongitude(), Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SearchAdress.this, Map.class);
						Bundle bundle = new Bundle();
						bundle.putDouble("LATITUDE", address.getLatitude());
						bundle.putDouble("LONGITUDE", address.getLongitude());
						bundle.putInt("RAYON", 16);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else
						Toast.makeText(SearchAdress.this, "Adresse Introuvable", Toast.LENGTH_LONG).show();		
				}
				catch (IndexOutOfBoundsException e) {
					Toast.makeText(SearchAdress.this, "Adresse introuvable", Toast.LENGTH_LONG).show();
				}
				catch (Exception e) {
					Toast.makeText(SearchAdress.this, "Erreur de GoogleMap", Toast.LENGTH_LONG).show();
				}
				
			}
	};
		
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_adress);
        
        // Initialisation des boutons
        _bCheck = (Button) findViewById(R.id.bCheck);
        _bCheck.setOnClickListener(_bListenerbCheck);
        
    } // onCreate(Bundle savedInstanceState)
    
} // class Activity1