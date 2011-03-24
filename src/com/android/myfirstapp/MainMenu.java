package com.android.myfirstapp;

import java.util.ArrayList;

import com.android.tools.ParkingDBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity
{
	// Boutons
	private Button b_map;
	private Button b_search;
	private Button b_list;
	
	// Listener sur les bouton
	private OnClickListener _bListenerMap = new OnClickListener() 
	{
			public void onClick(View v)
			{
				Intent intent = new Intent(MainMenu.this, Map.class);
				startActivity(intent);
			}
	};
	
	private OnClickListener _bListenerSearch = new OnClickListener() 
	{
			public void onClick(View v)
			{
				Intent intent;
				intent = new Intent(MainMenu.this, SearchMenu.class);
				startActivity(intent);
			}
	};
				
	private OnClickListener _bListenerList = new OnClickListener() 
	{
			public void onClick(View v)
			{
				Intent intent;
				intent = new Intent(MainMenu.this, ListParking.class);
				startActivity(intent);
			}
	};
				
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        
        ////////////////////////////////////////////////
        ////////////////////////////////////////////////
        // Initialisation de la Base de Données
        //MyDbSqlite sqlite = new MyDbSqlite(MainMenu.this, "easypark", null, 1);
        //sqlite.executeAppelSOAP();
        //SQLiteDatabase db = sqlite.getReadableDatabase();
        
        
        try{
        	
        
        ParkingDBAdapter db = new ParkingDBAdapter(MainMenu.this);
        db.openR();
        db.close();
        }
        catch (Exception e) {
			Toast.makeText(MainMenu.this, "Impossible de se connecter au seveur", Toast.LENGTH_LONG);
			
		}
	
		////////////////////////////////////////////////
		////////////////////////////////////////////////
        
        
        // Initialisation des boutons
        b_map = (Button) findViewById(R.id.b1);
        b_map.setOnClickListener(_bListenerMap);
        
        b_search = (Button) findViewById(R.id.b2);
        b_search.setOnClickListener(_bListenerSearch);
        
        b_list = (Button) findViewById(R.id.b3);
        b_list.setOnClickListener(_bListenerList);
        
        
    } // onCreate(Bundle savedInstanceState)
    
} // class MyFirstApp