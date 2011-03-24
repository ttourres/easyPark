package com.android.myfirstapp;

import java.util.ArrayList;
import java.util.List;
import com.android.tools.ParkingDBAdapter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;


public class Map extends MapActivity {

	final static int ACTUALISER = 1;
	final static int VUE = 2;
	final static int CARTE = 21;
	final static int SATELLITE = 22;
	final static int RECHERCHER = 3;
	
	private MapView mapView;
	private ArrayList<Parking> listParkings;
	private MapController mapControl;
	private MapLocationOverlay overlay;
	
	private ParkingDBAdapter db;
	
	public GeoPoint myPosition = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
        
		try{
		setContentView(R.layout.carte);
        
        
        
        // creation du connecteur à la BD
        db = new ParkingDBAdapter(Map.this);
        
        // Mise en place de la carte
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapControl = mapView.getController();
        
      //récuperation des parametres si ils sont présent
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null)
		{
			double latitude = bundle.getDouble("LATITUDE");
			double longitude = bundle.getDouble("LONGITUDE");
			int rayon = bundle.getInt("RAYON");
			
			myPosition  = new GeoPoint((int)(latitude * 1E6), (int)(longitude* 1E6));
			
			//Toast.makeText(Map.this, latitude + ";" + longitude, Toast.LENGTH_LONG).show();
			
			mapControl.setCenter(myPosition);
	        mapControl.setZoom(rayon);
		}
		else
		{
			GeoPoint centreInitial  = new GeoPoint((int)(43.3 * 1E6), (int)(5.4* 1E6));
			mapControl.setCenter(centreInitial);
	        mapControl.setZoom(13);
		}
        
        overlay = new MapLocationOverlay(this);
		mapView.getOverlays().add(overlay);
		}
		catch (Exception e) {
			Toast.makeText(Map.this, "marche pas", Toast.LENGTH_SHORT);
		}
		
    }
	
	public List<Parking> getParkings() {
		if (listParkings == null) {
			listParkings = new ArrayList<Parking>();
		
	        // ouvrir la BD en lecture
	        db.openR();
	        
	        listParkings = db.getParkings();
	        // fermeture de la BD
	        db.close();
	        
	    	
		}
		return listParkings;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		menu.add(0, ACTUALISER, 0, R.string.map_refresh);
		SubMenu menuVue = menu.addSubMenu(R.string.map_view);
		menuVue.add(0, CARTE, 0, R.string.map_map);
		menuVue.add(0, SATELLITE, 0, R.string.map_satellite);		
		menu.add(0, RECHERCHER, 0, R.string.map_search);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){

		switch (item.getItemId()){
		
		case ACTUALISER:
			try{
			db.openR();
			db.refresh();
	        // Mise � jour de la liste de parking
			listParkings = db.getParkings();
	        db.close();
	        // Refresh de la carte
	        mapView.invalidate();
	        mapView.requestLayout();
			}
			catch (Exception e) {
				Toast.makeText(Map.this, "Erreur de connexion", Toast.LENGTH_LONG).show();
			}
	        return true;
		
		case CARTE :
			mapView.setSatellite(false);
			return true;	
			
		case SATELLITE :
			mapView.setSatellite(true);
			return true;
		
		case RECHERCHER :
			Intent intent = new Intent(Map.this, SearchMenu.class);
			startActivity(intent);
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}

	
