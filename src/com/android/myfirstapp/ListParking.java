package com.android.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import com.android.myfirstapp.ParkingAdapter.ParkingAdapterListener;
import com.android.tools.ParkingDBAdapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class ListParking extends ListActivity implements ParkingAdapterListener{

	private ParkingDBAdapter db;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.liste_parking);
	    
	    
	    try{
	    db = new ParkingDBAdapter(ListParking.this);
	    db.openR();
        
        ArrayList<Parking>listParkings = db.getParkings();
        // fermeture de la BD
        db.close();
    
        if(listParkings != null)
        {
        	ParkingAdapter adapter = new ParkingAdapter(this, listParkings);    
        	adapter.addListener(this);
        	ListView list = this.getListView();
	    	list.setAdapter(adapter);
        }
        else
        {
        	
        }
	    }
	    catch (Exception e) {
			Toast.makeText(ListParking.this, "Erreur de connexion", Toast.LENGTH_LONG);
		}
	}

	public void onClickNom(Parking selectedParking, int position) {
		
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Parking");
		
		Intent intent = new Intent(this, InfoParking.class);
		
		intent = intent.putExtra("name", selectedParking.getName());
		intent = intent.putExtra("adress", selectedParking.getAdress());
		intent = intent.putExtra("availabity", selectedParking.getAvailabity());
		intent = intent.putExtra("capacity", selectedParking.getCapacity());
		
		startActivity(intent);
	}

	
}
